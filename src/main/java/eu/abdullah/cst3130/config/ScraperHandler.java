package eu.abdullah.cst3130.config;

import java.util.List;

public class ScraperHandler {

    private static List<Thread> scraperList;

    //Empty constructor
    ScraperHandler() {
    }

    //Getter
    public static List<Thread> getScraperList() {
        return scraperList;
    }

    //Setter
    public static void setScraperList(List<Thread> sList) {
        scraperList = sList;
    }

    //Start TVScrapers
    public void startThreads() {
        for (Thread TVScraper : scraperList) {
            TVScraper.start();
        }
    }

    //Join TVScrapers
    public void joinThreads() {
        for (Thread TVScraper : scraperList) {

            try {
                TVScraper.join();
            } catch (InterruptedException ex) {
                System.err.println(ex.getMessage());
            }
        }
    }
}