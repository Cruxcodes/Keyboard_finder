package eu.abdullah.cst3130;

import eu.abdullah.cst3130.config.AppConfig;
import eu.abdullah.cst3130.config.ScraperHandler;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * This is the main file for the java application
 *
 * @author Abdullah Ola Mudathir
 */
public class Main {
    public static void main(String[] args) {
        System.setProperty("webdriver.chrome.driver", "C:\\Programs\\ChromeDriver\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe");
        ApplicationContext context = new AnnotationConfigApplicationContext(
                AppConfig.class
        );
        ScraperHandler handler = (ScraperHandler) context.getBean("scraperHandler");
        handler.startThreads();
        handler.joinThreads();
    }
}
