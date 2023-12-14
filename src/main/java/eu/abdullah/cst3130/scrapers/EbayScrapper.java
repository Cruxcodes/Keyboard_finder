package eu.abdullah.cst3130.scrapers;

import eu.abdullah.cst3130.hibernate.HibernateMapping;
import eu.abdullah.cst3130.models.KeyboardAnnotation;
import eu.abdullah.cst3130.models.KeyboardDetailsAnnotation;
import eu.abdullah.cst3130.utilities.KeyboardModelNormalizer;
import eu.abdullah.cst3130.utilities.KeyboardUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.List;

public class EbayScrapper extends Thread {
    //    https://www.ebay.co.uk/b/bn_450742?Brand=Razer%7CCorsair%7CLogitech%7CSteelSeries&rt=nc&mag=1
    ChromeOptions chromeOptions = new ChromeOptions();

    /**
     * This function is run on thread start
     */
    @Override
    public void run() {
        chromeOptions.setHeadless(false);
        chromeOptions.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.97 Safari/537.36");
        HibernateMapping hibernateMapping = new HibernateMapping();
        hibernateMapping.init();
        KeyboardUtil keyboardUtil = new KeyboardUtil();
        WebDriver driver = new ChromeDriver(this.chromeOptions);
        driver.get(" https://www.ebay.co.uk/b/bn_450742?Brand=Razer%7CCorsair%7CLogitech%7CSteelSeries&mag=1&rt=nc&_pgn=1");

        try {
            Thread.sleep(3000);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        WebElement acceptCookies = driver.findElement(By.id("gdpr-banner-accept"));
        acceptCookies.click();

        try {
            Thread.sleep(3000);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        for (int i = 0; i <= 6; i++) {
            driver.get("https://www.ebay.co.uk/b/bn_450742?Brand=Razer%7CCorsair%7CLogitech%7CSteelSeries&mag=1&rt=nc&_pgn=" + i);
            try {
                Thread.sleep(3000);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            List<WebElement> productElements = driver.findElements(By.cssSelector("li.s-item"));

            for (WebElement product : productElements) {
                KeyboardAnnotation keyboardAnnotation = new KeyboardAnnotation();
                KeyboardDetailsAnnotation detailsAnnotation = new KeyboardDetailsAnnotation();

                try {
                    String productUrl = product.findElement(By.cssSelector("a.s-item__link")).getAttribute("href");
                    WebDriver detailsDriver = new ChromeDriver(chromeOptions);
                    detailsDriver.get(productUrl);
                    try {
                        Thread.sleep(3000);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    WebElement acceptDetailCookies = detailsDriver.findElement(By.id("gdpr-banner-accept"));
                    acceptDetailCookies.click();
                    try {
                        Thread.sleep(3000);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    String productName = product.findElement(By.cssSelector("h3.s-item__title")).getText();
                    String[] productTitle = productName.split(" ");


                    String modelValue = "";
//              This is the model name
                    if (!keyboardUtil.getEnding(productTitle[0], productTitle[3])) {
                        modelValue = productTitle[1] + " " + productTitle[2];
                    } else {
                        modelValue = productTitle[1] + " " + productTitle[2] + " " + productTitle[3];
                    }
                    KeyboardModelNormalizer keyboardModelNormalizer = new KeyboardModelNormalizer();
                    String model = keyboardModelNormalizer.normalizeModelName(modelValue);
                    String name = productTitle[0] + " " + model;


                    keyboardAnnotation.setImage(driver.findElement(By.cssSelector("img.s-item__image-img")).getAttribute("src"));
                    keyboardAnnotation.setName(name);
                    keyboardAnnotation.setModel(model);

                    detailsAnnotation.setShortDescription(productName);
                    detailsAnnotation.setColor(keyboardUtil.getColor(productName));
                    keyboardAnnotation.setBrand(productTitle[0]);
                    WebElement bigImage = detailsDriver.findElement(By.cssSelector("div.ux-image-carousel-item.active"));
                    detailsAnnotation.setImage(bigImage.findElement(By.tagName("img")).getAttribute("src"));

                    WebElement priceString = detailsDriver.findElement(By.cssSelector("div.x-price-primary"));
                    float price = keyboardUtil.formatPrice(priceString.findElement(By.cssSelector("span.ux-textspans")).getText());
                    detailsDriver.quit();
                    try {
                        Thread.sleep(3000);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                    int exists = hibernateMapping.isKeyboardExisting(keyboardAnnotation);
                    int detailsExists = hibernateMapping.isKeyboardDetailExisiting(exists, detailsAnnotation.getColor());

                    if (exists == -10) {
                        hibernateMapping.addKeyboard(keyboardAnnotation);
                        detailsAnnotation.setKeyboardId(keyboardAnnotation.getId());
                        hibernateMapping.addKeyboardDetails(detailsAnnotation);
                        hibernateMapping.addComparisonIfNotExisting(detailsAnnotation.getId(), price, productUrl);
                    } else if (detailsExists == -10) {
                        detailsAnnotation.setKeyboardId(exists);
                        hibernateMapping.addKeyboardDetails(detailsAnnotation);
                        hibernateMapping.addComparisonIfNotExisting(detailsAnnotation.getId(), price, productUrl);
                    } else {
                        hibernateMapping.addComparisonIfNotExisting(detailsExists, price, productUrl);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        hibernateMapping.shutDown();
        driver.quit();
    }
}
