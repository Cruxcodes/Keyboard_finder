package eu.abdullah.cst3130.scrapers;

import org.openqa.selenium.By;
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
        driver.get("https://www.argos.co.uk/browse/technology/laptops-and-pcs/gaming-laptops/c:30279/");


        List<WebElement> elementList = driver.findElements(By.cssSelector("[data-test='component-product-card-title']"));
        List<WebElement> productList = driver.findElements(By.cssSelector("[data-test='component-product-card']"));
        for (WebElement element : productList){
//            System.out.println(element.findElements());
        }
        List<Object> productName = new ArrayList<>();
//            System.out.println(elementList.size());
        for (int i = 0; i < elementList.size(); i++) {
            System.out.println(elementList.get(i).getText()
            );
        }
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
    }
}
