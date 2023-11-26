package eu.abdullah.cst3130.scrapers;

import eu.abdullah.cst3130.hibernate.HibernateMapping;
import eu.abdullah.cst3130.models.KeyboardAnnotation;
import eu.abdullah.cst3130.models.KeyboardDetailsAnnotation;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.List;

public class AmazonScraper extends Thread {
    //    https://www.amazon.co.uk/s?bbn=340831031&rh=n%3A13978270031&language=en_GB&_encoding=UTF8&brr=1&content-id=amzn1.sym.3642feb9-f459-485e-b5f9-8e67b609eacd&pd_rd_r=21371497-293a-4ee5-9948-f58f32e7a7d2&pd_rd_w=iSaBC&pd_rd_wg=3vPcg&pf_rd_p=3642feb9-f459-485e-b5f9-8e67b609eacd&pf_rd_r=H2MDJXQHT6M4F19CK301&rd=1&ref=Oct_d_odnav_428654031
    ChromeOptions chromeOptions;


    public AmazonScraper(ChromeOptions chromeOptions) {
        this.chromeOptions = chromeOptions;
    }


    /**
     * This function is run on therad start
     */
    @Override
    public void run() {
        HibernateMapping hibernateMapping = new HibernateMapping();
        hibernateMapping.init();
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


        for (int i = 0; i <= numberOfPages; i++) {
            driver.get("https://www.amazon.co.uk/s?i=computers&bbn=340831031&rh=n%3A13978270031%2Cp_89%3ACorsair%7CLogitech+G%7CRazer%7CSteelSeries&dc&page=" + i + "&language=en_GB&_encoding=UTF8&brr=1&content-id=amzn1.sym.3642feb9-f459-485e-b5f9-8e67b609eacd&pd_rd_r=21371497-293a-4ee5-9948-f58f32e7a7d2&pd_rd_w=iSaBC&pd_rd_wg=3vPcg&pf_rd_p=3642feb9-f459-485e-b5f9-8e67b609eacd&pf_rd_r=H2MDJXQHT6M4F19CK301&qid=1701000716&rd=1&rnid=1632651031&ref=sr_pg_2");

//        List<WebElement> elementList = driver.findElements(By.cssSelector("[data-qa='ck-product-box']"));
            try {
                Thread.sleep(3000);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            List<WebElement> productList = driver.findElements(By.cssSelector("div.a-section.a-spacing-base"));

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
                    String name = productTitle[0] + " " + productTitle[1] + " " + productTitle[2] + " " + productTitle[3];
                    String model = productTitle[1] + " " + productTitle[2] + " " + productTitle[3];


                    keyboardAnnotation.setImage(product.findElement(By.cssSelector("img.s-image")).getAttribute("src"));
                    keyboardAnnotation.setName(name);
                    keyboardAnnotation.setModel(model);

                    detailsAnnotation.setWebsite_url(product.findElement(By.cssSelector("a.a-link-normal.s-underline-text")).getAttribute("href"));
                    detailsAnnotation.setShortDescription(detailsDriver.findElement(By.cssSelector("span#productTitle")).getText());
                    detailsAnnotation.setName(name);
                    detailsAnnotation.setBrand(productTitle[0]);
                    detailsAnnotation.setModel(model);
                    detailsAnnotation.setDescription(detailsDriver.findElement(By.cssSelector("ul.a-unordered-list.a-vertical.a-spacing-mini")).getText());
//                System.out.println();

//                This is the image inside the list
                    detailsAnnotation.setImage(detailsDriver.findElement(By.cssSelector("img#landingImage")).getAttribute("src"));

//                This is for the price
                    String priceString = product.findElement(By.cssSelector("span.a-price-whole")).getText() + "." + product.findElement(By.cssSelector("span.a-price-fraction")).getText();
                    float price = Float.parseFloat(priceString);

                    keyboardAnnotation.setPrice(price);
                    detailsAnnotation.setPrice(price);
                    detailsAnnotation.setKeyboardId(keyboardAnnotation.getId());


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

                hibernateMapping.addKeyboard(keyboardAnnotation);
                detailsAnnotation.setKeyboardId(keyboardAnnotation.getId());
                hibernateMapping.addKeyboardDetails(detailsAnnotation);
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
