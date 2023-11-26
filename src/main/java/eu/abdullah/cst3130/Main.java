package eu.abdullah.cst3130;

//
//import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.chrome.ChromeDriver;
//import org.openqa.selenium.chrome.ChromeOptions;

import eu.abdullah.cst3130.hibernate.HibernateMapping;
import eu.abdullah.cst3130.scrapers.AmazonScraper;
import eu.abdullah.cst3130.scrapers.ArgosScraper;
import eu.abdullah.cst3130.scrapers.OverClockers;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * This is the main file for the java application
 *
 * @author Abdullah Ola Mudathir
 */
public class Main {


    public static void main(String[] args) {
        ChromeOptions options = new ChromeOptions();
        options.setHeadless(false);
        options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.97 Safari/537.36");
        //        options.set
        System.setProperty("webdriver.chrome.driver", "C:\\Programs\\ChromeDriver\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe");


        ArgosScraper argosScraper = new ArgosScraper(options);
        OverClockers overClockers = new OverClockers(options);
        AmazonScraper amazonScraper = new AmazonScraper(options);
        argosScraper.start();
//        overClockers.start();
//        amazonScraper.start();
//

//        try {
//            hibernateMapping.shutDown();
//        } catch (Exception ex) {
//            System.out.println(ex);
//        }
//        HibernateMapping hibernateMapping =  new HibernateMapping();
//        hibernateMapping.init();
//
////        hibernateMapping.addKeyboard();
////        hibernateMapping.addKeyboard();
////        hibernateMapping.addKeyboard();
//        hibernateMapping.shutDown();
    }
}
