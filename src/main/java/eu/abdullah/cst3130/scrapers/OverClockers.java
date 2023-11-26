package eu.abdullah.cst3130.scrapers;

import eu.abdullah.cst3130.hibernate.HibernateMapping;
import eu.abdullah.cst3130.models.KeyboardAnnotation;
import eu.abdullah.cst3130.models.KeyboardDetailsAnnotation;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OverClockers extends Thread {
    ChromeOptions chromeOptions;

    public OverClockers(ChromeOptions chromeOptions) {
        this.chromeOptions = chromeOptions;
    }

    @Override
    public void run() {
        HibernateMapping hibernateMapping = new HibernateMapping();
        hibernateMapping.init();
        String[] singleBrandNames = {"Logitech", "Razer", "Steelseries", "Ducky", "Glorious", "Corsair"};
        WebDriver driver = new ChromeDriver(this.chromeOptions);
        driver.get("https://www.overclockers.co.uk/peripherals/keyboards/gaming-keyboards");
        try {
            sleep(4000);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        WebElement cookieClick = driver.findElement(By.cssSelector("button#CybotCookiebotDialogBodyLevelButtonLevelOptinAllowAll"));
        cookieClick.click();

        //This is the number of pages available in the pagination.
        WebElement pagination = driver.findElement(By.cssSelector("[data-qa='component ck-ajax-load-more-button']"));
        int maxPages = Integer.parseInt(pagination.getAttribute("maxpage"));

        for (int i = 0; i <= maxPages; i++) {
            driver.get("https://www.overclockers.co.uk/peripherals/keyboards/gaming-keyboards?page=" + i);
            try {
                sleep(3000);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            List<WebElement> productElements = driver.findElements(By.tagName("ck-product-box"));
            for (int j = 0; j < productElements.size(); j++) {
                String[] extractedNameAndModel = productElements.get(j).findElement(By.cssSelector("h6.h5")).getText().split(" ");
                boolean isBrandPresent = Arrays.stream(singleBrandNames)
                        .anyMatch(brand -> brand.equalsIgnoreCase(extractedNameAndModel[0]));

                if (isBrandPresent) {
                    KeyboardAnnotation keyboardAnnotation = new KeyboardAnnotation();
                    KeyboardDetailsAnnotation detailsAnnotation = new KeyboardDetailsAnnotation();
                    List<String> price = new ArrayList<>(List.of(productElements.get(j).findElement(By.cssSelector("span.price__amount")).getText().split("")));
                    price.remove(0);
                    float keyPrice = Float.parseFloat(String.join("", price));

                    /**Setting all the values of the keyboard and keyboard details*/
                    keyboardAnnotation.setImage(productElements.get(j).findElement(By.cssSelector("[data-qa ='component ck-img']")).getAttribute("src"));
                    keyboardAnnotation.setName(extractedNameAndModel[0] + " " + extractedNameAndModel[1] + " " + extractedNameAndModel[2]);
                    keyboardAnnotation.setModel(extractedNameAndModel[1] + " " + extractedNameAndModel[2] + " " + extractedNameAndModel[3]);
                    keyboardAnnotation.setPrice(keyPrice);

                    detailsAnnotation.setName(extractedNameAndModel[0] + " " + extractedNameAndModel[1] + " " + extractedNameAndModel[2]);
                    detailsAnnotation.setBrand(extractedNameAndModel[0]);
                    detailsAnnotation.setModel(extractedNameAndModel[1] + " " + extractedNameAndModel[2] + " " + extractedNameAndModel[3]);
                    detailsAnnotation.setWebsite_url(productElements.get(j).findElement(By.cssSelector("a.js-gtm-product-link")).getAttribute("href"));
                    detailsAnnotation.setShortDescription(productElements.get(j).findElement(By.cssSelector("h6.h5")).getText());
                    detailsAnnotation.setPrice(keyPrice);
                    /**The single scraping for the details of the keyboard*/
                    WebDriver detailsDriver = new ChromeDriver(this.chromeOptions);
                    detailsDriver.get(productElements.get(j).findElement(By.cssSelector("a.js-gtm-product-link")).getAttribute("href"));
                    try {
                        sleep(2000);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    WebElement detailCookieClick = detailsDriver.findElement(By.cssSelector("button#CybotCookiebotDialogBodyLevelButtonLevelOptinAllowAll"));
                    detailCookieClick.click();
                    try {
                        sleep(4000);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    WebElement detailDesc = detailsDriver.findElement(By.cssSelector("div#ck-product-accordion__body-category-id-features"));
                    String description = detailDesc.findElement(By.cssSelector("div.ck-product-accordion__body-content")).getText();
                    WebElement detailImage = detailsDriver.findElement(By.cssSelector("[data-swiper-slide-index ='0']"));

                    detailsAnnotation.setDescription(description);
                    detailsAnnotation.setImage(detailImage.findElement(By.cssSelector("[data-qa ='component ck-img']")).getAttribute("src"));
                    detailsDriver.quit();

                    try {
                        sleep(2000);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    /** End of the driver*/

                    hibernateMapping.addKeyboard(keyboardAnnotation);
                    detailsAnnotation.setKeyboardId(keyboardAnnotation.getId());

                    hibernateMapping.addKeyboardDetails(detailsAnnotation);
                }
            }
        }
        try {
            sleep(2000);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        driver.quit();
    }
}
