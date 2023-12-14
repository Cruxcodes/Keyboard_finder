package eu.abdullah.cst3130.scrapers;

import eu.abdullah.cst3130.hibernate.HibernateMapping;
import eu.abdullah.cst3130.models.KeyboardAnnotation;
import eu.abdullah.cst3130.models.KeyboardDetailsAnnotation;
import eu.abdullah.cst3130.utilities.KeyboardUtil;
import eu.abdullah.cst3130.utilities.KeyboardModelNormalizer;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.List;

public class ArgosScraper extends Thread {
    ChromeOptions chromeOptions = new ChromeOptions();

    @Override
    public void run() {
        chromeOptions.setHeadless(false);
        chromeOptions.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.97 Safari/537.36");
        HibernateMapping hibernateMapping = new HibernateMapping();
        hibernateMapping.init();
        WebDriver driver = new ChromeDriver(this.chromeOptions);
        KeyboardUtil keyboardUtil = new KeyboardUtil();
        driver.get("https://www.argos.co.uk/browse/technology/computer-accessories/pc-keyboards/c:30058/brands:corsair,logitech,logitech-g,razer,steelseries/");

        try {
            Thread.sleep(5000);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        WebElement cookieElement = driver.findElement(By.cssSelector("[data-test='confirmation-button']"));
        cookieElement.click();

        try {
            Thread.sleep(5000);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        JavascriptExecutor js = (JavascriptExecutor) driver;
        long pageHeight = (long) js.executeScript("return document.body.scrollHeight");
        long scrollHeight = 0;
        long scrollStep = 500; // Adjust the scroll step size as needed

        while (scrollHeight < pageHeight) {
            js.executeScript("window.scrollBy(0, " + scrollStep + ");");
            try {
                Thread.sleep(500); // Adjust the delay for slower scrolling
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            scrollHeight += scrollStep;
        }

        try {
            Thread.sleep(5000);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        List<WebElement> productList = driver.findElements(By.cssSelector("[data-test='component-product-card']"));

        try {
            for (WebElement element : productList) {
                KeyboardAnnotation keyboardAnnotation = new KeyboardAnnotation();
                KeyboardDetailsAnnotation detailsAnnotation = new KeyboardDetailsAnnotation();
//                This is for the link of site to buy to
//
//                This is the name of the products
                String[] nameOfProduct = element.findElement(By.cssSelector("[data-test='component-product-card-title']")).getText().split(" ");
//              This is the brand name

                String modelValue = "";
//              This is the model name
                if (!keyboardUtil.getEnding(nameOfProduct[0], nameOfProduct[3])) {
                    modelValue = nameOfProduct[1] + " " + nameOfProduct[2];
                } else {
                    modelValue = nameOfProduct[1] + " " + nameOfProduct[2] + " " + nameOfProduct[3];
                }
                /** This is where the normalization occurs */
                KeyboardModelNormalizer keyboardModelNormalizer = new KeyboardModelNormalizer();
                String model = keyboardModelNormalizer.normalizeModelName(modelValue);
                String name = nameOfProduct[0] + " " + model;

                keyboardAnnotation.setImage(element.findElement(By.cssSelector("[data-test='component-image']")).getAttribute("src"));
                keyboardAnnotation.setName(name);
                keyboardAnnotation.setModel(model);

                detailsAnnotation.setShortDescription(element.findElement(By.cssSelector("[data-test='component-product-card-title']")).getText());
                detailsAnnotation.setColor(keyboardUtil.getColor(element.findElement(By.cssSelector("[data-test='component-product-card-title']")).getText()));

                keyboardAnnotation.setBrand(nameOfProduct[0]);
                WebElement price = element.findElement(By.cssSelector("[data-test='component-product-card-price']"));
//                List<String> priceValues = List.of(price.findElement(By.tagName("strong")).getText().split(""));
//                priceValues = priceValues.subList(1, priceValues.size());
//                Float.parseFloat(String.join("", priceValues));
                Float priceValue = keyboardUtil.formatPrice(price.findElement(By.tagName("strong")).getText());
                detailsAnnotation.setKeyboardId(keyboardAnnotation.getId());
                try {
                    WebDriver detailsDriver = new ChromeDriver(this.chromeOptions);
                    detailsDriver.get(element.findElement(By.cssSelector("[data-test='component-product-card-link']")).getAttribute("href"));
                    WebElement detailCookieElement = detailsDriver.findElement(By.cssSelector("[data-test='confirmation-button']"));
                    detailCookieElement.click();

                    try {
                        Thread.sleep(4000);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                    WebElement image = detailsDriver.findElement(By.cssSelector("[data-test='component-media-gallery_activeSlide-0']"));

//                    This is for the internal image
                    detailsAnnotation.setImage(image.findElement(By.tagName("img")).getAttribute("src"));

                    detailsDriver.quit();

                    int exists = hibernateMapping.isKeyboardExisting(keyboardAnnotation);
                    int detailsExists = hibernateMapping.isKeyboardDetailExisiting(exists, detailsAnnotation.getColor());
                    if (exists == -10) {
                        // Keyboard doesn't exist, add keyboard and details to the database
                        hibernateMapping.addKeyboard(keyboardAnnotation);
                        detailsAnnotation.setKeyboardId(keyboardAnnotation.getId());
                        hibernateMapping.addKeyboardDetails(detailsAnnotation);
                        hibernateMapping.addComparisonIfNotExisting(detailsAnnotation.getId(), priceValue, element.findElement(By.cssSelector("[data-test='component-product-card-link']")).getAttribute("href"));
                    } else if (detailsExists == -10) {
                        detailsAnnotation.setKeyboardId(exists);
                        hibernateMapping.addKeyboardDetails(detailsAnnotation);
                        hibernateMapping.addComparisonIfNotExisting(detailsAnnotation.getId(), priceValue, element.findElement(By.cssSelector("[data-test='component-product-card-link']")).getAttribute("href"));
                    } else {
                        hibernateMapping.addComparisonIfNotExisting(detailsExists, priceValue, element.findElement(By.cssSelector("[data-test='component-product-card-link']")).getAttribute("href"));
                    }
                } catch (Exception ex) {
                    System.err.println("This is an error" + ex);
                }


            }
        } catch (Exception ex) {

        }


        try {
            Thread.sleep(3000);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        driver.quit();
    }
}
