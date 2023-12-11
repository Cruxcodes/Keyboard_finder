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

public class EbayScrapper extends Thread {
    //    https://www.ebay.co.uk/b/bn_450742?Brand=Razer%7CCorsair%7CLogitech%7CSteelSeries&rt=nc&mag=1
    ChromeOptions chromeOptions;

    public EbayScrapper(ChromeOptions chromeOptions) {
        this.chromeOptions = chromeOptions;
    }


    /**
     * This function is run on thread start
     */
    @Override
    public void run() {
        HibernateMapping hibernateMapping = new HibernateMapping();
        hibernateMapping.init();
        WebDriver driver = new ChromeDriver(this.chromeOptions);
        driver.get(" https://www.ebay.co.uk/b/bn_450742?Brand=Razer%7CCorsair%7CLogitech%7CSteelSeries&rt=nc&mag=1");

        try {
            Thread.sleep(3000);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        hibernateMapping.shutDown();
        driver.quit();
    }
}
