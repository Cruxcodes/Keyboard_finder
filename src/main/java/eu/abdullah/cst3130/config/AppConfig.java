package eu.abdullah.cst3130.config;
import eu.abdullah.cst3130.hibernate.HibernateMapping;
import eu.abdullah.cst3130.scrapers.*;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class AppConfig {

    SessionFactory sessionFactory;

    /**
     * Bean to initialize ScraperHandler.
     *
     * @return scraperHandler
     */
    @Bean
    public ScraperHandler scraperHandler() {
        ScraperHandler scraperHandler = new ScraperHandler();

        List<Thread> scraperList = new ArrayList<>();
        scraperList.add(overClockers());
        scraperList.add(amazonScraper());
        scraperList.add(argosScraper());
        scraperList.add(ebayScrapper());
        scraperList.add(newGGScraper());
        ScraperHandler.setScraperList(scraperList);

        // Return Scraper Handler object
        return scraperHandler;
    }

    /**
     * Bean to initialize Overclockers scraper.
     *
     * @return overclockers
     */
    @Bean
    public OverClockers overClockers() {
        return new OverClockers();
    }

    /**
     * Bean to initialize AmazonScraper.
     *
     * @return amazonScraper
     */
    @Bean
    public AmazonScraper amazonScraper() {
        return new AmazonScraper();
    }

    /**
     * Bean to initialize ArgosScraper.
     *
     * @return argosScraper
     */
    @Bean
    public ArgosScraper argosScraper() {
        return new ArgosScraper();
    }


    /**
     * Bean to initialize NewGGScraper.
     *
     * @return newGGScraper
     */
    @Bean
    public NewGGScraper newGGScraper() {
        return new NewGGScraper();
    }

    /**
     * Bean to initialize EbayScrapper.
     *
     * @return ebayScrapper
     */
    @Bean
    public EbayScrapper ebayScrapper() {
        return new EbayScrapper();
    }

    /**
     * Bean to initialize HibernateMapping.
     *
     * @return hibernate
     */
    @Bean
    public HibernateMapping hibernate() {
        HibernateMapping hibernate = new HibernateMapping();
        hibernate.setSessionFactory(sessionFactory());
        return hibernate;
    }

    /**
     * Bean to create SessionFactory.
     *
     * @return sessionFactory
     */
    @Bean
    public SessionFactory sessionFactory() {
        if (sessionFactory == null) {
            try {
                StandardServiceRegistryBuilder standardServiceRegistryBuilder = new StandardServiceRegistryBuilder();
                standardServiceRegistryBuilder.configure("hibernate.cfg.xml");
                StandardServiceRegistry registry = standardServiceRegistryBuilder.build();

                try {
                    sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
                } catch (Exception e) {
                    System.err.println("Session Factory build failed.");
                    StandardServiceRegistryBuilder.destroy(registry);
                }
                System.out.println("Session factory built.");
            } catch (Throwable ex) {
                System.err.println("SessionFactory creation failed." + ex);
            }
        }
        return sessionFactory;
    }
}
