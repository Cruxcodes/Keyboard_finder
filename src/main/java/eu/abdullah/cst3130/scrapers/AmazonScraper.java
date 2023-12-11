package eu.abdullah.cst3130.scrapers;

import eu.abdullah.cst3130.hibernate.HibernateMapping;
import eu.abdullah.cst3130.models.KeyboardAnnotation;
import eu.abdullah.cst3130.models.KeyboardDetailsAnnotation;
import eu.abdullah.cst3130.utilities.KeyboardColor;
import eu.abdullah.cst3130.utilities.KeyboardModelNormalizer;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;


import java.util.List;

public class AmazonScraper extends Thread {
    ChromeOptions chromeOptions;

    public AmazonScraper(ChromeOptions chromeOptions) {
        this.chromeOptions = chromeOptions;
    }

//    ChromeOptions chromeOptions  = new ChromeOptions();
//        chromeOptions.setHeadless(false);
//        chromeOptions.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.97 Safari/537.36");
//    //        options.set
//        System.setProperty("webdriver.chrome.driver", "C:\\Programs\\ChromeDriver\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe");

    /**
     * This function is run on therad start
     */
    @Override
    public void run() {
        HibernateMapping hibernateMapping = new HibernateMapping();
        hibernateMapping.init();
        KeyboardColor keyboardColor = new KeyboardColor();
        WebDriver driver = new ChromeDriver(this.chromeOptions);
        driver.get("https://www.amazon.co.uk/s?i=computers&bbn=340831031&rh=n%3A13978270031%2Cp_89%3ACorsair%7CLogitech+G%7CRazer%7CSteelSeries&dc&language=en_GB&_encoding=UTF8&brr=1&content-id=amzn1.sym.3642feb9-f459-485e-b5f9-8e67b609eacd&pd_rd_r=21371497-293a-4ee5-9948-f58f32e7a7d2&pd_rd_w=iSaBC&pd_rd_wg=3vPcg&pf_rd_p=3642feb9-f459-485e-b5f9-8e67b609eacd&pf_rd_r=H2MDJXQHT6M4F19CK301&qid=1700920911&rd=1&rnid=1632651031&ref=sr_nr_p_89_4&ds=v1%3AFUYc8%2BNFlxitTZuoCq3WtF%2B7LPrRIpkUZH6fD2t192U");

        try {
            Thread.sleep(3000);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        //This is the button for accepting all cookies.
        WebElement acceptCookies = driver.findElement(By.id("sp-cc-accept"));
        acceptCookies.click();

        try {
            Thread.sleep(4000);
        } catch (Exception ex) {
            ex.printStackTrace();
        }


        /**The number of pages are gotten */
        List<WebElement> pages = driver.findElements(By.cssSelector("span.s-pagination-item.s-pagination-disabled"));
        Integer numberOfPages = Integer.parseInt(pages.get(pages.size() - 1).getText());
        String nameOfKeyboard = "";

        for (int i = 0; i <= 10; i++) {
            driver.get("https://www.amazon.co.uk/s?i=computers&bbn=340831031&rh=n%3A13978270031%2Cp_89%3ACorsair%7CLogitech+G%7CRazer%7CSteelSeries&dc&page=" + i + "&language=en_GB&_encoding=UTF8&brr=1&content-id=amzn1.sym.3642feb9-f459-485e-b5f9-8e67b609eacd&pd_rd_r=21371497-293a-4ee5-9948-f58f32e7a7d2&pd_rd_w=iSaBC&pd_rd_wg=3vPcg&pf_rd_p=3642feb9-f459-485e-b5f9-8e67b609eacd&pf_rd_r=H2MDJXQHT6M4F19CK301&qid=1701000716&rd=1&rnid=1632651031&ref=sr_pg_2");

            try {
                Thread.sleep(3000);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            List<WebElement> productList = driver.findElements(By.cssSelector("div.a-section.a-spacing-base"));
            float price = 0;
            for (WebElement product : productList) {
                KeyboardAnnotation keyboardAnnotation = new KeyboardAnnotation();
                KeyboardDetailsAnnotation detailsAnnotation = new KeyboardDetailsAnnotation();
//            This is for the product image
//product.findElement(By.cssSelector("img.s-image")).getAttribute("src")
                //This is for the url link
//            System.out.println(product.findElement(By.cssSelector("a.a-link-normal.s-underline-text")).getAttribute("href"));
                WebDriver detailsDriver = new ChromeDriver(this.chromeOptions);
                detailsDriver.get(product.findElement(By.cssSelector("a.a-link-normal.s-underline-text")).getAttribute("href"));
                try {
                    Thread.sleep(4000);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                try {
                    WebElement detailAcceptCookies = detailsDriver.findElement(By.id("sp-cc-accept"));
                    detailAcceptCookies.click();
                    try {
                        Thread.sleep(4000);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    String[] productTitle = detailsDriver.findElement(By.cssSelector("span#productTitle")).getText().split(" ");
//                This is the brand name
                    String modelValue = productTitle[1] + " " + productTitle[2] + " " + productTitle[3];
                    /** This is where the normalization occurs */
                    KeyboardModelNormalizer keyboardModelNormalizer = new KeyboardModelNormalizer();
                    String model = keyboardModelNormalizer.normalizeModelName(modelValue);
                    String name = productTitle[0] + " " + model;

                    keyboardAnnotation.setImage(product.findElement(By.cssSelector("img.s-image")).getAttribute("src"));
                    keyboardAnnotation.setName(name);
                    keyboardAnnotation.setModel(model);

//                    detailsAnnotation.setWebsite_url(product.findElement(By.cssSelector("a.a-link-normal.s-underline-text")).getAttribute("href"));
                    detailsAnnotation.setShortDescription(detailsDriver.findElement(By.cssSelector("span#productTitle")).getText());
                    detailsAnnotation.setColor(keyboardColor.getColor(detailsDriver.findElement(By.cssSelector("span#productTitle")).getText()));
                    detailsAnnotation.setName(name);
                    detailsAnnotation.setBrand(productTitle[0]);
                    keyboardAnnotation.setBrand(productTitle[0]);
                    detailsAnnotation.setModel(model);
                    detailsAnnotation.setDescription(detailsDriver.findElement(By.cssSelector("ul.a-unordered-list.a-vertical.a-spacing-mini")).getText());
//                System.out.println();

//                This is the image inside the list
                    detailsAnnotation.setImage(detailsDriver.findElement(By.cssSelector("img#landingImage")).getAttribute("src"));

//                This is for the price
                    String priceString = product.findElement(By.cssSelector("span.a-price-whole")).getText() + "." + product.findElement(By.cssSelector("span.a-price-fraction")).getText();
                    price = Float.parseFloat(priceString);


//                    detailsAnnotation.setPrice(price);
                    detailsAnnotation.setKeyboardId(keyboardAnnotation.getId());
                    nameOfKeyboard = detailsDriver.findElement(By.cssSelector("span#productTitle")).getText();

                } catch (Exception e) {
                    // Handle case when element doesn't exist
                    System.err.println(e);
                }

                detailsDriver.quit();


                try {
                    Thread.sleep(4000);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }


                int exists = hibernateMapping.isKeyboardExisting(keyboardAnnotation);
                int detailsExists = hibernateMapping.isKeyboardDetailExisiting(exists, detailsAnnotation.getColor());
                if (exists == -10) {
                    // Keyboard doesn't exist, add keyboard and details to the database
                    hibernateMapping.addKeyboard(keyboardAnnotation);
                    detailsAnnotation.setKeyboardId(keyboardAnnotation.getId());
                    hibernateMapping.addKeyboardDetails(detailsAnnotation);
                    hibernateMapping.addComparisonIfNotExisting(detailsAnnotation.getId(), price, product.findElement(By.cssSelector("a.a-link-normal.s-underline-text")).getAttribute("href"));
                } else if (detailsExists == -10) {
                    detailsAnnotation.setKeyboardId(exists);
                    hibernateMapping.addKeyboardDetails(detailsAnnotation);
                    hibernateMapping.addComparisonIfNotExisting(detailsAnnotation.getId(), price, product.findElement(By.cssSelector("a.a-link-normal.s-underline-text")).getAttribute("href"));
                } else {
                    hibernateMapping.addComparisonIfNotExisting(detailsExists, price, product.findElement(By.cssSelector("a.a-link-normal.s-underline-text")).getAttribute("href"));
                }
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
