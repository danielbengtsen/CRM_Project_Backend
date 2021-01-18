
package facades;

import entities.Contact;
import entities.Opportunity;
import entities.Role;
import entities.User;
import errorhandling.Messages;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import utils.EMF_Creator;

public class TestDataFacade 
{
    private static final Messages MESSAGES = new Messages();
    
    public static String setupTestData()
    {
        EntityManagerFactory emf = EMF_Creator.createEntityManagerFactory();
        EntityManager em = emf.createEntityManager();
        
        String salesNameAndRole = "salesperson";
        String adminNameAndRole = "admin";
        
        if(em.find(User.class, salesNameAndRole) != null)
        {
            return MESSAGES.DATABASE_ALREADY_POPULATED;
        } else
        {
           String testPwd = System.getenv("SECRET_PASSWORD");

           User salesperson = new User(salesNameAndRole, testPwd);
           User admin = new User(adminNameAndRole, testPwd);
           
           Contact contact1 = new Contact("Contact 1", "contact1@nicecompany.com", "Nice Company", "Developer", "12345678");
           Contact contact2 = new Contact("Contact 2", "contact2@nicecompany.com", "Companiay", "Programmer", "87654321");
           Opportunity oppor1 = new Opportunity("Nice Company opportunity", 40000);
           Opportunity oppor2 = new Opportunity("Companiay opportunity", 50000);
           
           contact1.addOpportunity(oppor1);
           contact2.addOpportunity(oppor2);

           em.getTransaction().begin();
           Role salesRole = new Role(salesNameAndRole);
           Role adminRole = new Role(adminNameAndRole);
           salesperson.addRole(salesRole);
           admin.addRole(adminRole);
           em.persist(salesRole);
           em.persist(adminRole);
           em.persist(salesperson);
           em.persist(admin);
           em.persist(contact1);
           em.persist(contact2);
           em.getTransaction().commit();
           
           return MESSAGES.DATABASE_POPULATED;
        }
    }
}
