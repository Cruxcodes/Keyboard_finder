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

import java.util.List;

public class NewGGScraper extends Thread {
    ChromeOptions chromeOptions = new ChromeOptions();
    private KeyboardUtil keyboardColor;


    /**
     * This is the run function to start the NewGGScraper thread
     */
    @Override
    public void run() {
        chromeOptions.setHeadless(false);
        chromeOptions.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.97 Safari/537.36");
        HibernateMapping hibernateMapping = new HibernateMapping();
        hibernateMapping.init();
        KeyboardUtil keyboardUtil = new KeyboardUtil();
        WebDriver driver = new ChromeDriver(this.chromeOptions);

        driver.get("https://www.newegg.com/global/uk-en/p/pl?N=101584270&PageSize=96");

        try {
            Thread.sleep(3000);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        WebElement cookieClick = driver.findElement(By.cssSelector("button.osano-cm-accept-all.osano-cm-button--type_accept"));
        cookieClick.click();
        try {
            Thread.sleep(3000);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        List<WebElement> productElements = driver.findElements(By.cssSelector("div.item-container"));

        for (WebElement product : productElements) {
            KeyboardAnnotation keyboardAnnotation = new KeyboardAnnotation();
            KeyboardDetailsAnnotation detailsAnnotation = new KeyboardDetailsAnnotation();

            try {
                WebElement productLink = product.findElement(By.cssSelector("a.item-title"));
                String productUrl = productLink.getAttribute("href");

                WebDriver detailsDriver = new ChromeDriver(this.chromeOptions);
                detailsDriver.get(productUrl);

                try {
                    Thread.sleep(4000);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                String[] productTitle = detailsDriver.findElement(By.cssSelector("h1.product-title")).getText().split(" ");
                String modelValue = "";

                if (!keyboardUtil.getEnding(productTitle[0], productTitle[3])) {
                    modelValue = productTitle[1] + " " + productTitle[2];
                } else {
                    modelValue = productTitle[1] + " " + productTitle[2] + " " + productTitle[3];
                }
                KeyboardModelNormalizer keyboardModelNormalizer = new KeyboardModelNormalizer();
                String model = keyboardModelNormalizer.normalizeModelName(modelValue);
                String name = productTitle[0] + " " + model;

                keyboardAnnotation.setImage(product.findElement(By.cssSelector("img.checkedimg2.checkedimg")).getAttribute("src"));
                keyboardAnnotation.setName(name);
                keyboardAnnotation.setModel(model);

                detailsAnnotation.setShortDescription(detailsDriver.findElement(By.cssSelector("h1.product-title")).getText());
                detailsAnnotation.setColor(keyboardColor.getColor(detailsDriver.findElement(By.cssSelector("h1.product-title")).getText()));
                keyboardAnnotation.setBrand(productTitle[0]);
                detailsAnnotation.setImage(detailsDriver.findElement(By.cssSelector("img.product-view-img-original")).getAttribute("src"));

                WebElement priceElement = detailsDriver.findElement(By.cssSelector("div.price-current"));
                String priceWholeNumber = priceElement.findElement(By.tagName("strong")).getText();
                String decimalPriceString = priceElement.findElement(By.tagName("sup")).getText();
                float price = Float.parseFloat(priceWholeNumber + "" + decimalPriceString);

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

                detailsDriver.quit();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try {
            Thread.sleep(3000);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        hibernateMapping.shutDown();
        driver.quit();
    }
}
