
package facades;

import entities.Role;
import entities.User;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import utils.EMF_Creator;

public class TestDataFacade 
{
    public static String setupTestData()
    {
        EntityManagerFactory emf = EMF_Creator.createEntityManagerFactory();
        EntityManager em = emf.createEntityManager();
        
        String salesNameAndRole = "salesperson";
        String adminNameAndRole = "admin";
        
        if(em.find(User.class, salesNameAndRole) != null)
        {
            return "Already populated!";
        } else
        {
           String testPwd = System.getenv("SECRET_PASSWORD");

           User salesperson = new User(salesNameAndRole, testPwd);
           User admin = new User(adminNameAndRole, testPwd);

           em.getTransaction().begin();
           Role salesRole = new Role(salesNameAndRole);
           Role adminRole = new Role(adminNameAndRole);
           salesperson.addRole(salesRole);
           admin.addRole(adminRole);
           em.persist(salesRole);
           em.persist(adminRole);
           em.persist(salesperson);
           em.persist(admin);
           em.getTransaction().commit();
           
           return "Done!";
        }
    }
}
