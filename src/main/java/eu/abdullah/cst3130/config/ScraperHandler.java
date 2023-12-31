package eu.abdullah.cst3130.config;

import java.util.List;

/**
 * Handles a list of scraper threads and manages their execution.
 */

public class ScraperHandler {

    private static List<Thread> scraperList;

    /**
     * Empty constructor for ScraperHandler.
     */
    public ScraperHandler() {
    }

    /**
     * Gets the list of scraper threads.
     * @return The list of scraper threads.
     */
    public static List<Thread> getScraperList() {
        return scraperList;
    }

    /**
     * Sets the list of scraper threads.
     *
     * @param sList The list of scraper threads to be set.
     */
    public static void setScraperList(List<Thread> sList) {
        scraperList = sList;
    }

    /**
     * Starts all the scraper threads.
     * Uses start() to initiate a new thread.
     */
    public void startThreads() {
        for (Thread scraper : scraperList) {
            scraper.start();
        }
    }

    /**
     * Joins all the scraper threads.
     * Uses join() to wait for the threads to finish.
     */
    public void joinThreads() {
        for (Thread scraper : scraperList) {
            try {
                scraper.join();
            } catch (InterruptedException ex) {
                System.err.println(ex.getMessage());
            }
        }
    }
}
