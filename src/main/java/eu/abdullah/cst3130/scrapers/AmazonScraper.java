package eu.abdullah.cst3130.scrapers;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.List;

public class AmazonScraper extends Thread{
//    https://www.amazon.co.uk/s?bbn=340831031&rh=n%3A13978270031&language=en_GB&_encoding=UTF8&brr=1&content-id=amzn1.sym.3642feb9-f459-485e-b5f9-8e67b609eacd&pd_rd_r=21371497-293a-4ee5-9948-f58f32e7a7d2&pd_rd_w=iSaBC&pd_rd_wg=3vPcg&pf_rd_p=3642feb9-f459-485e-b5f9-8e67b609eacd&pf_rd_r=H2MDJXQHT6M4F19CK301&rd=1&ref=Oct_d_odnav_428654031
ChromeOptions chromeOptions;

    public AmazonScraper(ChromeOptions chromeOptions) {
        this.chromeOptions = chromeOptions;
    }

        String[] brandNames = {"Logitech","Razer","Steelseries"};

    @Override
    public void run() {
        WebDriver driver = new ChromeDriver(this.chromeOptions);
        driver.get("https://www.amazon.co.uk/s?bbn=340831031&rh=n%3A13978270031&language=en_GB&_encoding=UTF8&brr=1&content-id=amzn1.sym.3642feb9-f459-485e-b5f9-8e67b609eacd&pd_rd_r=21371497-293a-4ee5-9948-f58f32e7a7d2&pd_rd_w=iSaBC&pd_rd_wg=3vPcg&pf_rd_p=3642feb9-f459-485e-b5f9-8e67b609eacd&pf_rd_r=H2MDJXQHT6M4F19CK301&rd=1&ref=Oct_d_odnav_428654031");

//        List<WebElement> elementList = driver.findElements(By.cssSelector("[data-qa='ck-product-box']"));
        List<WebElement> productElement = driver.findElements( By.cssSelector("span.a-size-base-plus"));
//        List<WebElement> products = driver.findElements(By.cssSelector("h6.h5"));
        System.out.println(productElement);
//        for (int i = 0; i < productElement.size(); i++) {
//            System.out.println(productElement.get(i).getAttribute("data-qa"));
//            System.out.println(productElement.get(i).getAttribute(""));
////            System.out.println(products.get(i).getText());
//        }

//        System.out.println(products);
        for (int i = 0; i < productElement.size(); i++) {
            System.out.println(productElement.get(i).getText());
        }
//        for (WebElement element : productElement) {
////            System.out.println(element);
//            // Find the specific div element within the product element by its CSS selector
//            WebElement divElement = element.findElement(By.cssSelector("h6.h5"));
//
//            // Get the text content of the div element
//            String divText = divElement.getText();
//
//            System.out.println("Value of the div element: " + divText);
//        }
//        String dataQa = productElement.getAttribute("data-qa");
//        System.out.println("data-qa: " + dataQa);
//        WebElement productNameElement = productElement.findElement(By.cssSelector("[data-qa='ck-product-box--title-link']"));
//        String productName = productNameElement.getText();
//        System.out.println("Product Name: " + productName);
//
//        // Extract product price
//        WebElement priceElement = productElement.findElement(By.cssSelector("[data-qa='price-current']"));
//        String price = priceElement.getText();
//        System.out.println("Price: " + price);
//
//        // Extract availability status
//        WebElement availabilityElement = productElement.findElement(By.cssSelector("[data-qa='availability_status_available']"));
//        String availabilityStatus = availabilityElement.getText();
//        System.out.println("Availability Status: " + availabilityStatus);
//
//        // Extract product description
//        WebElement descriptionElement = productElement.findElement(By.className("ck-product-box-short-description"));
//        String description = descriptionElement.getText();
//        System.out.println("Description: " + description);

//        List<Object> productName = new ArrayList<>();
////            System.out.println(elementList.size());
//        for (int i = 0; i < elementList.size(); i++) {
////                console.log(elementList[i]);
////                System.out.println(elementList.get(i));
//            System.out.println(elementList.get(i).getText()
////                    elementList.get(i).findElements(By.className("co-product__anchor"))
//            );
////            try {
////                Thread.sleep(3000);
////            } catch (Exception ex) {
////                ex.printStackTrace();
////            }
//        }
        try {
            Thread.sleep(3000);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
//        WebElement element = driver.findElement(By.cssSelector("[data-test='product-group-card-0']"));
//        String dataTestValue = element.getAttribute("data-test");
//        System.out.println("data-test value: " + dataTestValue);


//            System.out.println(driver.getPageSource());


//        for (int i = 0; i < 5; i++) {
//            System.out.println(i);
//
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//        }


        driver.quit();
    }
}
