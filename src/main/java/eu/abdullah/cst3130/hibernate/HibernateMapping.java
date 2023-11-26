package eu.abdullah.cst3130.hibernate;

import eu.abdullah.cst3130.models.KeyboardAnnotation;
import eu.abdullah.cst3130.models.KeyboardDetailsAnnotation;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

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
    public void init(){
        try {
            //Create a builder for the standard service registry
            StandardServiceRegistryBuilder standardServiceRegistryBuilder = new StandardServiceRegistryBuilder();

            //Load configuration from hibernate configuration file
            standardServiceRegistryBuilder.configure("hibernate.cfg.xml");

            //Create the registry that will be used to build the session factory
            StandardServiceRegistry registry = standardServiceRegistryBuilder.build();

            try {
                //Create the session factory - this is the goal of the init method.
                sessionFactory = new MetadataSources( registry ).buildMetadata().buildSessionFactory();
            }
            catch (Exception e) {
                    /* The registry would be destroyed by the SessionFactory,
                        but we had trouble building the SessionFactory, so destroy it manually */
                System.err.println("Session Factory build failed.");
                e.printStackTrace();
                StandardServiceRegistryBuilder.destroy( registry );
            }

            //Ouput result
            System.out.println("Session factory built.");

        }
        catch (Throwable ex) {
            // Make sure you log the exception, as it might be swallowed
            System.err.println("SessionFactory creation failed." + ex);
        }
    }


    /** Closes Hibernate down and stops its threads from running */
    public void shutDown(){
        sessionFactory.close();
    }

    /** Adding new keyboard brand to the database*/
    public void addKeyboard(KeyboardAnnotation keyboardAnnotation){
//    public void addKeyboard(){
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        session.save(keyboardAnnotation);
        session.getTransaction().commit();
        session.close();
    }

    /**Adding keyboard details */

    public void addKeyboardDetails(KeyboardDetailsAnnotation detailsAnnotation){
        try {
            Session session = sessionFactory.getCurrentSession();
            session.beginTransaction();
            session.save(detailsAnnotation);
            session.getTransaction().commit();
            session.close();

        } catch (Exception e) {
            e.printStackTrace(); // Log or handle the exception appropriately
        }
    }
}
