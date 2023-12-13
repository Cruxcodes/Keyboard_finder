package eu.abdullah.cst3130.scrapers;

import eu.abdullah.cst3130.hibernate.HibernateMapping;
import eu.abdullah.cst3130.models.KeyboardAnnotation;
import eu.abdullah.cst3130.models.KeyboardDetailsAnnotation;
import eu.abdullah.cst3130.utilities.KeyboardUtil;
import eu.abdullah.cst3130.utilities.KeyboardModelNormalizer;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.ArrayList;
import java.util.List;

public class OverClockers extends Thread {
    ChromeOptions chromeOptions = new ChromeOptions();


    /**
     * This is the run function to start the overClockers thread
     */
    @Override
    public void run() {
        chromeOptions.setHeadless(false);
        chromeOptions.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.97 Safari/537.36");
        KeyboardUtil keyboardColor = new KeyboardUtil();
        HibernateMapping hibernateMapping = new HibernateMapping();
        hibernateMapping.init();
        WebDriver driver = new ChromeDriver(this.chromeOptions);
        driver.get("https://www.overclockers.co.uk/peripherals/keyboards/gaming-keyboards?manufacturer%5B0%5D=CORSAIR&manufacturer%5B1%5D=Glorious&manufacturer%5B2%5D=Logitech&manufacturer%5B3%5D=Razer&manufacturer%5B4%5D=SteelSeries");
        try {
            sleep(4000);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        WebElement cookieClick = driver.findElement(By.cssSelector("button#CybotCookiebotDialogBodyLevelButtonLevelOptinAllowAll"));
        cookieClick.click();

        //This is the number of pages available in the pagination.
        WebElement pagination = driver.findElement(By.cssSelector("[data-qa='component ck-ajax-load-more-button']"));
        int maxPages = 1;
        try {
            maxPages = Integer.parseInt(pagination.getAttribute("maxpage"));
        } catch (Exception ex) {
            System.out.println("This is a problem");
        }

        for (int i = 0; i <= maxPages; i++) {
            driver.get("https://www.overclockers.co.uk/peripherals/keyboards/gaming-keyboards?manufacturer%5B0%5D=CORSAIR&manufacturer%5B1%5D=Glorious&manufacturer%5B2%5D=Logitech&manufacturer%5B3%5D=Razer&manufacturer%5B4%5D=SteelSeries" + i);
            try {
                sleep(3000);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            //Finds the product elements on the page
            List<WebElement> productElements = driver.findElements(By.tagName("ck-product-box"));

            //This loops through the list of products element
            for (int j = 0; j < productElements.size(); j++) {
                String[] extractedNameAndModel = productElements.get(j).findElement(By.cssSelector("h6.h5")).getText().split(" ");

                KeyboardAnnotation keyboardAnnotation = new KeyboardAnnotation();
                KeyboardDetailsAnnotation detailsAnnotation = new KeyboardDetailsAnnotation();
                List<String> price = new ArrayList<>(List.of(productElements.get(j).findElement(By.cssSelector("span.price__amount")).getText().split("")));
                price.remove(0);
                float keyPrice = Float.parseFloat(String.join("", price));

                String modelValue = "";
                if (!keyboardColor.getEnding(extractedNameAndModel[0], extractedNameAndModel[3])) {
                    modelValue = extractedNameAndModel[1] + " " + extractedNameAndModel[2];
                } else {
                    modelValue = extractedNameAndModel[1] + " " + extractedNameAndModel[2] + " " + extractedNameAndModel[3];
                }
                // This is where the normalization occurs

                KeyboardModelNormalizer keyboardModelNormalizer = new KeyboardModelNormalizer();
                String model = keyboardModelNormalizer.normalizeModelName(modelValue);
                String name = extractedNameAndModel[0] + " " + model;

                keyboardAnnotation.setImage(productElements.get(j).findElement(By.cssSelector("[data-qa ='component ck-img']")).getAttribute("src"));
                keyboardAnnotation.setName(name);
                keyboardAnnotation.setModel(model);
                keyboardAnnotation.setBrand(extractedNameAndModel[0]);

                detailsAnnotation.setShortDescription(productElements.get(j).findElement(By.cssSelector("h6.h5")).getText());
                detailsAnnotation.setColor(keyboardColor.getColor(productElements.get(j).findElement(By.cssSelector("h6.h5")).getText()));


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
                WebElement detailImage = detailsDriver.findElement(By.cssSelector("[data-swiper-slide-index ='0']"));
                detailsAnnotation.setImage(detailImage.findElement(By.cssSelector("[data-qa ='component ck-img']")).getAttribute("src"));
                detailsDriver.quit();


                int exists = hibernateMapping.isKeyboardExisting(keyboardAnnotation);
                int detailsExists = hibernateMapping.isKeyboardDetailExisiting(exists, detailsAnnotation.getColor());
                if (exists == -10) {
                    // Keyboard doesn't exist, add keyboard and details to the database
                    hibernateMapping.addKeyboard(keyboardAnnotation);
                    detailsAnnotation.setKeyboardId(keyboardAnnotation.getId());
                    hibernateMapping.addKeyboardDetails(detailsAnnotation);
                    hibernateMapping.addComparisonIfNotExisting(detailsAnnotation.getId(), keyPrice, productElements.get(j).findElement(By.cssSelector("a.js-gtm-product-link")).getAttribute("href"));
                } else if (detailsExists == -10) {
                    detailsAnnotation.setKeyboardId(exists);
                    hibernateMapping.addKeyboardDetails(detailsAnnotation);
                    hibernateMapping.addComparisonIfNotExisting(detailsAnnotation.getId(), keyPrice, productElements.get(j).findElement(By.cssSelector("a.js-gtm-product-link")).getAttribute("href"));
                } else {
                    hibernateMapping.addComparisonIfNotExisting(detailsExists, keyPrice, productElements.get(j).findElement(By.cssSelector("a.js-gtm-product-link")).getAttribute("href"));
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
