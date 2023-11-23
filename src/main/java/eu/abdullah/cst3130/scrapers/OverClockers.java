package eu.abdullah.cst3130.scrapers;

import eu.abdullah.cst3130.hibernate.HibernateMapping;
import eu.abdullah.cst3130.models.KeyboardAnnotation;
import eu.abdullah.cst3130.models.KeyboardDetailsAnnotation;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.ui.context.Theme;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class OverClockers extends Thread {
    ChromeOptions chromeOptions;

    public OverClockers(ChromeOptions chromeOptions) {
        this.chromeOptions = chromeOptions;
    }

    @Override
    public void run() {
        HibernateMapping hibernateMapping = new HibernateMapping();
        hibernateMapping.init();
        String[] singleBrandNames = {"Logitech", "Razer", "Steelseries",};
        String[] doubleBrandNames = {"Ducky One",};
        WebDriver driver = new ChromeDriver(this.chromeOptions);
        driver.get("https://www.overclockers.co.uk/peripherals/keyboards/gaming-keyboards");
        try {
            sleep(4000);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        WebElement productContainer = driver.findElement(By.cssSelector("div.row.row--listing"));
//            List<WebElement> elementList = productContainer.findElements(By.cssSelector("[data-qa='ck-product-box']"));
        List<WebElement> products = productContainer.findElements(By.cssSelector("div.row"));

        WebElement cookieClick = driver.findElement(By.cssSelector("button#CybotCookiebotDialogBodyLevelButtonLevelOptinAllowAll"));
        cookieClick.click();
        List<WebElement> productElement = driver.findElements(By.tagName("ck-product-box"));
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
                    keyboardAnnotation.setName(extractedNameAndModel[0] + " " + extractedNameAndModel[1] + " " + extractedNameAndModel[2]);
                    detailsAnnotation.setName(extractedNameAndModel[0] + " " + extractedNameAndModel[1] + " " + extractedNameAndModel[2]);
                    detailsAnnotation.setBrand(extractedNameAndModel[0]);
                    keyboardAnnotation.setModel(extractedNameAndModel[1] + " " + extractedNameAndModel[2]);
                    detailsAnnotation.setModel(extractedNameAndModel[1] + " " + extractedNameAndModel[2]);
                    keyboardAnnotation.setImage(productElements.get(j).findElement(By.cssSelector("[data-qa ='component ck-img']")).getAttribute("src"));
                    detailsAnnotation.setWebsite_url(productElements.get(j).findElement(By.cssSelector("a.js-gtm-product-link")).getAttribute("href"));

                    keyboardAnnotation.setPrice(keyPrice);
                    detailsAnnotation.setPrice(keyPrice);
                    WebDriver detailsDriver = new ChromeDriver(this.chromeOptions);
                    /**The single scraping for the details of the keyboard*/
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
                    String shortDescription = detailsDriver.findElement(By.cssSelector("div.ck-product-cta-box-shortdescription")).getText();
                    WebElement detailImage = detailsDriver.findElement(By.cssSelector("[data-swiper-slide-index ='0']"));

                    detailsAnnotation.setDescription(shortDescription);
                    detailsAnnotation.setImage(detailImage.findElement(By.cssSelector("[data-qa ='component ck-img']")).getAttribute("src"));
//
                    detailsDriver.quit();
                    try {
                        sleep(2000);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    /** End of the driver*/
//                    keyboardAnnotation.setPrice();
//                    keyboardAnnotation.setWebsite_url();

                    hibernateMapping.addKeyboard(keyboardAnnotation);
                    detailsAnnotation.setKeyboardId(keyboardAnnotation.getId());

                    hibernateMapping.addKeyboardDetails(detailsAnnotation);


                }
//                System.out.println(extractedNameAndModel[0] + "  " + isBrandPresent);

                continue;
                //          This is the way to get the product name
//                productElements.get(j).findElement(By.cssSelector("h6.h5")).getText();
                ///This is they way to get the product price
//                productElements.get   (j).findElement(By.cssSelector("span.price__amount")).getText();
//                productElements.get   (j).findElement(By.cssSelector("ck-product-accordion__body-content )).getText();
                //This is the way to get the model number
//                productElements.get(j).findElement(By.cssSelector("div.ck-product-box-sku.x-small")).getText()


//                System.out.println(j);
//                System.out.println(
//                        productElements.get(j).findElement(By.cssSelector("h6.h5")).getText() + " " +
//                                productElements.get(j).findElement(By.cssSelector("a.js-gtm-product-link")).getAttribute("href") + " " +
//                                productElements.get(j).findElement(By.cssSelector("span.price__amount")).getText()
//                );
            }

//            List<WebElement> productElementt = driver.findElements(By.tagName("ck-product-box"));
//            List<WebElement> pagination = driver.findElements(By.cssSelector("li.page-item.pagination__item"));
//            System.out.println(productElementt);
//            System.out.println(i);
        }

        /**This is the way to get the link to the place*/
//            productElement.get(39).findElement(By.cssSelector("[data-qa ='ck-product-box--title-link']")).getAttribute("href")
//            productElement.get(39).findElement(By.cssSelector("[data-qa ='component ck-img']")).getAttribute("src") //This is for the image
        /**This is the one for the user name*/
//        products.get(1).findElement(By.cssSelector("h6.h5")).getText()
        //productElement.get(39).findElement(By.cssSelector("[data-qa ='ck-product-box--title-link']")).getText()
        //  products.get(1).findElement(By.cssSelector("a")).getAttribute("href") //This is the way to get the link

        try {
            sleep(4000);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        hibernateMapping.shutDown();
        driver.quit();
    }
}
