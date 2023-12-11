//package eu.abdullah.cst3130.config;
//
//import eu.abdullah.cst3130.hibernate.HibernateMapping;
//import eu.abdullah.cst3130.scrapers.AmazonScraper;
//import eu.abdullah.cst3130.scrapers.OverClockers;
//import org.hibernate.Hibernate;
//import org.hibernate.SessionFactory;
//import org.hibernate.boot.MetadataSources;
//import org.hibernate.boot.registry.StandardServiceRegistry;
//import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Configuration
//public class AppConfig {
//
//    SessionFactory sessionFactory;
//
//    /**
//     * ScraperHandler Bean
//     *
//     * @return scraperHandler
//     */
//    @Bean
//    public ScraperHandler scraperHandler() {
//        ScraperHandler scraperHandler = new ScraperHandler();
//
//        List<Thread> scraperList = new ArrayList();
//        scraperList.add(overClockers());
//        scraperList.add(amazonScraper());
////        scraperList.add(scraper3());
////        scraperList.add(scraper4());
////        scraperList.add(scraper5());
//        ScraperHandler.setScraperList(scraperList);
//
//        // Return Scraper Handler object
//        return scraperHandler;
//    }
//
//    /**
//     * OverClockers Bean
//     *
//     * @return overClockers
//     */
//    @Bean
//    public OverClockers overClockers() {
//        OverClockers overClockers = new OverClockers();
//        return overClockers;
//    }
//
//    /**
//     * TVScraper5 Bean
//     *
//     * @return scraper5
//     */
//    @Bean
//    public AmazonScraper amazonScraper() {
//        AmazonScraper amazonScraper = new AmazonScraper();
//        return amazonScraper;
//    }
//
//    /**
//     * Hibernate Bean
//     *
//     * @return hibernate
//     */
//    @Bean
//    public HibernateMapping hibernate() {
//        HibernateMapping hibernate = new HibernateMapping();
//        hibernate.setSessionFactory(sessionFactory());
//        return hibernate;
//    }
//
//    /**
//     * SessionFactory Bean
//     *
//     * @return sessionFactory
//     */
//    @Bean
//    public SessionFactory sessionFactory() {
//        if (sessionFactory == null) {//Build sessionFatory once only
//            try {
//                //Create a builder for the standard service registry
//                StandardServiceRegistryBuilder standardServiceRegistryBuilder = new StandardServiceRegistryBuilder();
//
//                //Load configuration from hibernate configuration file.
//                //Here we are using a configuration file that specifies Java annotations.
//                standardServiceRegistryBuilder.configure("hibernate.cfg.xml");
//
//                //Create the registry that will be used to build the session factory
//                StandardServiceRegistry registry = standardServiceRegistryBuilder.build();
//                try {
//                    //Create the session factory - this is the goal of the init method.
//                    sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
//                } catch (Exception e) {
//                    /* The registry would be destroyed by the SessionFactory,
//                            but we had trouble building the SessionFactory, so destroy it manually */
//                    System.err.println("Session Factory build failed.");
//                    StandardServiceRegistryBuilder.destroy(registry);
//                }
//                //Ouput result
//                System.out.println("Session factory built.");
//            } catch (Throwable ex) {
//                // Make sure you log the exception, as it might be swallowed
//                System.err.println("SessionFactory creation failed." + ex);
//            }
//        }
//        return sessionFactory;
//    }