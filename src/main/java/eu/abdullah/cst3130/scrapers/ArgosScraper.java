package eu.abdullah.cst3130.scrapers;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.ArrayList;
import java.util.List;

public class ArgosScraper extends Thread {
    ChromeOptions chromeOptions;

    public ArgosScraper(ChromeOptions chromeOptions) {
        this.chromeOptions = chromeOptions;
    }

    @Override
    public void run() {
        WebDriver driver = new ChromeDriver(this.chromeOptions);
//        driver.get("https://www.argos.co.uk/browse/technology/computer-accessories/pc-keyboards/c:30058/");
        driver.get("https://www.argos.co.uk/browse/technology/computer-accessories/pc-keyboards/c:30058/brands:corsair,logitech,logitech-g,razer,steelseries/");

        try {
            Thread.sleep(5000);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        WebElement cookieElement = driver.findElement(By.cssSelector("[data-test='confirmation-button']"));
        cookieElement.click();

//        JavascriptExecutor js = (JavascriptExecutor) driver;
//        js.executeScript("window.scrollBy(0, 250);");
//        js.executeScript("window.scrollBy(250, 500);");
//        js.executeScript("window.scrollBy(500, 740);");
//        js.executeScript("window.scrollBy(0, document.body.scrollHeight);");


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


//        JavascriptExecutor js = (JavascriptExecutor) driver;
//        long lastHeight = (long) js.executeScript("return document.body.scrollHeight");
//
//        while (true) {
//            js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
//            try {
//                Thread.sleep(2000); // Adjust the delay as needed
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            long newHeight = (long) js.executeScript("return document.body.scrollHeight");
//            if (newHeight == lastHeight) {
//                break;
//            }
//            lastHeight = newHeight;
//        }
        try {
            Thread.sleep(5000);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
//        js.executeScript("window.scrollBy(250, 400);");

        try {
            Thread.sleep(5000);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
//        List<WebElement> elementList = driver.findElements(By.cssSelector("[data-test='component-product-card-title']"));
        List<WebElement> productList = driver.findElements(By.cssSelector("[data-test='component-product-card']"));

        try {
            for (WebElement element : productList) {
//                This is for the link of site
//                element.findElement(By.cssSelector("[data-test='component-product-card-link']")).getAttribute("href")
//                This is the name of the products
                String[] nameOfProduct =element.findElement(By.cssSelector("[data-test='component-product-card-title']")).getText().split(" ");
//              This is the brand name
                // nameOfProduct[0]
//              This is the model name
//              nameOfProduct[1] + nameOfProduct[2]



                System.out.println(element.findElement(By.cssSelector("[data-test='component-product-card-title']")).getText());

//                This is the product image
//                System.out.println(element.findElement(By.cssSelector("[data-test='component-image']")).getAttribute("src"));


            }
        } catch (Exception ex) {

        }
        try {
            Thread.sleep(3000);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
//        WebElement element = driver.findElement(By.cssSelector("[itemprop='priceCurrency']"));
        //s
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
