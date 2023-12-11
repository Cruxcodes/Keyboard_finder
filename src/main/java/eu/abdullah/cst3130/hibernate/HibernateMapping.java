package eu.abdullah.cst3130.hibernate;

import eu.abdullah.cst3130.models.ComparisonAnnotation;
import eu.abdullah.cst3130.models.KeyboardAnnotation;
import eu.abdullah.cst3130.models.KeyboardDetailsAnnotation;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;

import java.util.List;

/**
 * This is the hibernate mapping instance
 */
public class HibernateMapping {
    private SessionFactory sessionFactory;

    /**
     * Empty Constructor
     */
    public HibernateMapping() {
    }

    /**
     * Sets up the session factory and calls this method first*
     */
    public void init() {
        try {
            //Create a builder for the standard service registry
            StandardServiceRegistryBuilder standardServiceRegistryBuilder = new StandardServiceRegistryBuilder();

            //Load configuration from hibernate configuration file
            standardServiceRegistryBuilder.configure("hibernate.cfg.xml");

            //Create the registry that will be used to build the session factory
            StandardServiceRegistry registry = standardServiceRegistryBuilder.build();

            try {
                //Create the session factory - this is the goal of the init method.
                sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            } catch (Exception e) {
                    /* The registry would be destroyed by the SessionFactory,
                        but we had trouble building the SessionFactory, so destroy it manually */
                System.err.println("Session Factory build failed.");
                e.printStackTrace();
                StandardServiceRegistryBuilder.destroy(registry);
            }

            //Ouput result
            System.out.println("Session factory built.");

        } catch (Throwable ex) {
            // Make sure you log the exception, as it might be swallowed
            System.err.println("SessionFactory creation failed." + ex);
        }
    }


    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    /**
     * Closes Hibernate down and stops its threads from running
     */
    public void shutDown() {
        sessionFactory.close();
    }

    public String extractSiteNameFromUrl(String url) {
        String siteName = "";

        if (url.contains("overclockers.co.uk")) {
            siteName = "Overclockers";
        } else if (url.contains("amazon.co.uk")) {
            siteName = "Amazon";
        }
        // Add more conditions based on your URL patterns for other sites

        return siteName;
    }


    /**
     * Adding new keyboard brand to the database
     */
    public void addKeyboard(KeyboardAnnotation keyboardAnnotation) {
//    public void addKeyboard(){
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = session.getTransaction();

        if (!transaction.isActive()) {
            session.beginTransaction();
            // Your transactional operations here// Commit the transaction after successful operations
        }
        session.save(keyboardAnnotation);
        session.getTransaction().commit();
        session.close();
    }

    /**
     * Adding keyboard details
     */

    public void addKeyboardDetails(KeyboardDetailsAnnotation detailsAnnotation) {
        try {
            Session session = sessionFactory.getCurrentSession();
            Transaction transaction = session.getTransaction();

            if (!transaction.isActive()) {
                session.beginTransaction();
                // Your transactional operations here// Commit the transaction after successful operations
            }

            session.save(detailsAnnotation);
            session.getTransaction().commit();
            session.close();

        } catch (Exception e) {
            e.printStackTrace(); // Log or handle the exception appropriately
        }
    }


    /**This checks if the keyboard model is existing */
    public int isKeyboardExisting(KeyboardAnnotation keyboardAnnotation) {
        int exists = -10;

        try {
            Session session = sessionFactory.getCurrentSession();
            Transaction transaction = session.getTransaction();

            if (!transaction.isActive()) {
                session.beginTransaction();
                // Your transactional operations here// Commit the transaction after successful operations
            }

            // Check if the keyboard exists
            Query query = session.createQuery("FROM KeyboardAnnotation WHERE model = :modelName");
            query.setParameter("modelName", keyboardAnnotation.getModel());
            List<KeyboardAnnotation> keyboardList = query.list();

            if (!keyboardList.isEmpty()) {
                KeyboardAnnotation existingKeyboard = keyboardList.get(0);
                exists = existingKeyboard.getId();
                // Check if the keyboard details exist for the existingKeyboard
            } else {
                return exists;
                // Keyboard does not exist
                // Handle the scenario accordingly
            }


//            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace(); // Log or handle the exception appropriately
        }

        return exists;
    }



    public int isKeyboardDetailExisiting(int existingKeyboard,String color) {
        int exists = -10;
        try {
            Session session = sessionFactory.getCurrentSession();
            Transaction transaction = session.getTransaction();

            if (!transaction.isActive()) {
                session.beginTransaction();
                // Your transactional operations here// Commit the transaction after successful operations
            }

            Query detailsQuery = session.createQuery("FROM KeyboardDetailsAnnotation WHERE keyboardId = :id and color = :color");
            detailsQuery.setParameter("id", existingKeyboard);
            detailsQuery.setParameter("color", color);
            List<KeyboardDetailsAnnotation> detailsList = detailsQuery.list();

            if (!detailsList.isEmpty()) {
                KeyboardDetailsAnnotation detailsAnnotation = detailsList.get(0);
                exists = detailsAnnotation.getId();
            } else {
                // Keyboard exists but details do not exist
                // Handle the scenario accordingly
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return exists;
    }

    /**
     * This checks if the keyboard in the details has a comparison
     */
    public boolean isExistingInComparison(int keyboardId, String link) {
        try (Session session = sessionFactory.openSession()) {
            // Check if there is any entry in the ComparisonAnnotation table with the given keyboard ID and link
            Query<Long> query = session.createQuery("SELECT COUNT(*) FROM ComparisonAnnotation WHERE keyboardId = :keyboardId AND link = :link", Long.class);
            query.setParameter("keyboardId", keyboardId);
            query.setParameter("link", link);

            Long count = query.uniqueResult();
            return count != null && count > 0;
        } catch (Exception e) {
            e.printStackTrace(); // Handle or log the exception appropriately
            return false;
        }
    }

    public void addComparisonIfNotExisting(int keyboardAnnotation, float price, String link) {
        boolean isExisting = isExistingInComparison(keyboardAnnotation, link); // Check if comparison exists for the keyboard and link

        if (!isExisting) {
            // If the comparison doesn't exist, proceed to add it
            addComparison(keyboardAnnotation, price, link);
        } else {
            // Handle case when the comparison already exists
            System.out.println("Comparison for this model and site already exists. Skipping.");
        }
    }

    private void addComparison(int keyboardId, float price, String link) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();

            ComparisonAnnotation comparison = new ComparisonAnnotation();
            comparison.setKeyboardId(keyboardId);
            comparison.setLink(link);
            comparison.setPrice(price);

            session.save(comparison);

            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace(); // Handle or log the exception appropriately
        }
    }
}
