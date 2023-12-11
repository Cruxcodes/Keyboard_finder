package eu.abdullah.cst3130.scrapers;

import eu.abdullah.cst3130.hibernate.HibernateMapping;
import eu.abdullah.cst3130.models.ComparisonAnnotation;
import eu.abdullah.cst3130.models.KeyboardAnnotation;
import eu.abdullah.cst3130.models.KeyboardDetailsAnnotation;
import eu.abdullah.cst3130.utilities.KeyboardModelNormalizer;
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
                    String modelValue = extractedNameAndModel[1] + " " + extractedNameAndModel[2] + " " + extractedNameAndModel[3];
                    KeyboardModelNormalizer keyboardModelNormalizer = new KeyboardModelNormalizer();
                    String model = keyboardModelNormalizer.normalizeModelName(modelValue);
                    String name = extractedNameAndModel[0] + " " + model;

                    /**Setting all the values of the keyboard and keyboard details*/
                    keyboardAnnotation.setImage(productElements.get(j).findElement(By.cssSelector("[data-qa ='component ck-img']")).getAttribute("src"));
                    keyboardAnnotation.setName(name);
                    keyboardAnnotation.setModel(model);

                    detailsAnnotation.setName(name);
                    detailsAnnotation.setBrand(extractedNameAndModel[0]);
                    keyboardAnnotation.setBrand(extractedNameAndModel[0]);
                    detailsAnnotation.setModel(model);
//                    detailsAnnotation.setWebsite_url(productElements.get(j).findElement(By.cssSelector("a.js-gtm-product-link")).getAttribute("href"));
                    detailsAnnotation.setShortDescription(productElements.get(j).findElement(By.cssSelector("h6.h5")).getText());
//                    detailsAnnotation.setPrice(keyPrice);

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


                    int exists = hibernateMapping.isKeyboardExisting(keyboardAnnotation);
                    if (exists == -10) {
                        // Keyboard doesn't exist, add keyboard and details to the database
                        hibernateMapping.addKeyboard(keyboardAnnotation);
                        detailsAnnotation.setKeyboardId(keyboardAnnotation.getId());
                        hibernateMapping.addKeyboardDetails(detailsAnnotation);
                        hibernateMapping.addComparisonIfNotExisting(detailsAnnotation.getId(), keyPrice,productElements.get(j).findElement(By.cssSelector("a.js-gtm-product-link")).getAttribute("href"));

                    } else {
                        hibernateMapping.addComparisonIfNotExisting(exists, keyPrice, productElements.get(j).findElement(By.cssSelector("a.js-gtm-product-link")).getAttribute("href"));
                    }
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
