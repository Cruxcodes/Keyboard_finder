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
 * HibernateMapping class manages Hibernate operations needed for modifying the database
 */
public class HibernateMapping {
    private SessionFactory sessionFactory;

    /**
     * Empty Constructor
     */
    public HibernateMapping() {
    }


    /**
     * Runs and sets up the session factory and initializes Hibernate
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
                System.err.println("Session Factory build failed.");
                e.printStackTrace();
                StandardServiceRegistryBuilder.destroy(registry);
            }
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


    /**
     * Adding new keyboard brand to the database
     */
    public void addKeyboard(KeyboardAnnotation keyboardAnnotation) {
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = session.getTransaction();
        if (!transaction.isActive()) {
            session.beginTransaction();
        }
        session.save(keyboardAnnotation);
        session.getTransaction().commit();
        session.close();
    }

    /**
     * Adding keyboard details to the session and database
     */
    public void addKeyboardDetails(KeyboardDetailsAnnotation detailsAnnotation) {
        try {
            Session session = sessionFactory.getCurrentSession();
            Transaction transaction = session.getTransaction();
            if (!transaction.isActive()) {
                session.beginTransaction();
            }
            session.save(detailsAnnotation);
            session.getTransaction().commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace(); // Log or handle the exception appropriately
        }
    }


    /**
     * This checks if the keyboard model is existing by comparing the keyboardAnnotation id to any instance in the database
     *
     * @param keyboardAnnotation The keyboard entity
     * @return if the model exists return the model id else -10
     */

    public int isKeyboardExisting(KeyboardAnnotation keyboardAnnotation) {
        int exists = -10;
        try {
            Session session = sessionFactory.getCurrentSession();
            Transaction transaction = session.getTransaction();
            if (!transaction.isActive()) {
                session.beginTransaction();
            }
            Query query = session.createQuery("FROM KeyboardAnnotation WHERE model = :modelName");
            query.setParameter("modelName", keyboardAnnotation.getModel());
            List<KeyboardAnnotation> keyboardList = query.list();
            if (!keyboardList.isEmpty()) {
                KeyboardAnnotation existingKeyboard = keyboardList.get(0);
                exists = existingKeyboard.getId();
            } else {
                return exists;
            }
        } catch (Exception e) {
            e.printStackTrace(); // Log or handle the exception appropriately
        }
        return exists;
    }


    /**
     * Checks if a keyboard model exists in the database for a given existingKeyboard and color.
     *
     * @param existingKeyboard The existing keyboard ID.
     * @param color            The color of the keyboard detail.
     * @return The ID of the existing keyboard detail if found, otherwise -10.
     */
    public int isKeyboardDetailExisiting(int existingKeyboard, String color) {
        int exists = -10;
        try {
            Session session = sessionFactory.getCurrentSession();
            Transaction transaction = session.getTransaction();
            if (!transaction.isActive()) {
                session.beginTransaction();
            }
            Query detailsQuery = session.createQuery("FROM KeyboardDetailsAnnotation WHERE keyboardId = :id and color = :color");
            detailsQuery.setParameter("id", existingKeyboard);
            detailsQuery.setParameter("color", color);
            List<KeyboardDetailsAnnotation> detailsList = detailsQuery.list();

            if (!detailsList.isEmpty()) {
                KeyboardDetailsAnnotation detailsAnnotation = detailsList.get(0);
                exists = detailsAnnotation.getId();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return exists;
    }


    /**
     * Checks if a comparison entry exists for a keyboard ID and link.
     *
     * @param keyboardId The ID of the keyboard.
     * @param link       The link associated with the comparison.
     * @return True if a comparison entry exists, otherwise false.
     */
    public boolean isExistingInComparison(int keyboardId, String link) {
        try (Session session = sessionFactory.openSession()) {
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


    /**
     * Adds a new comparison entry if it does not already exist.
     *
     * @param keyboardAnnotation The ID of the keyboard.
     * @param price              The price for comparison.
     * @param link               The link associated with the comparison.
     */
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


    /**
     * Adds a new comparison entry to the database
     *
     * @param keyboardId The ID of the keyboard.
     * @param price      The price for comparison.
     * @param link       The link associated with the comparison.
     */
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
