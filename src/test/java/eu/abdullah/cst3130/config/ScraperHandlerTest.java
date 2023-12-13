package eu.abdullah.cst3130.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ScraperHandlerTest {

    private ScraperHandler scraperHandler;
    private List<Thread> scraperList;

    @BeforeEach
    public void setUp() {
        scraperHandler = new ScraperHandler();
        scraperList = new ArrayList<>();
        // Add threads to the scraperList
        for (int i = 0; i < 5; i++) {
            Thread thread = new Thread(() -> {
                // Simulate some work in the thread
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            scraperList.add(thread);
        }
        // Set the scraperList in the ScraperHandler
        ScraperHandler.setScraperList(scraperList);
    }

    @Test
    public void getScraperList_ReturnsCorrectList() {
        List<Thread> retrievedList = ScraperHandler.getScraperList();
        assertEquals(scraperList, retrievedList);
    }

    @Test
    public void startThreads_ThreadsStartedSuccessfully() {
        scraperHandler.startThreads();
        for (Thread thread : scraperList) {
            assertTrue(thread.isAlive());
        }
    }

    @Test
    public void joinThreads_ThreadsJoinedSuccessfully() {
        scraperHandler.startThreads();
        scraperHandler.joinThreads();
        for (Thread thread : scraperList) {
            assertFalse(thread.isAlive());
        }
    }

}