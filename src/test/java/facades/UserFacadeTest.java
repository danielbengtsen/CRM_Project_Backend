package facades;

import entities.Role;
import entities.User;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import security.errorhandling.AuthenticationException;
import utils.EMF_Creator;

public class UserFacadeTest 
{

    private static EntityManagerFactory emf;
    private static UserFacade facade;
    private static final User SALESPERSON = new User("salesperson", "test");

    public UserFacadeTest() {}

    @BeforeAll
    public static void setUpClass() 
    {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        facade = UserFacade.getUserFacade(emf);
    }
    
    @BeforeEach
    public void setUp() 
    {
        EntityManager em = emf.createEntityManager();
        try 
        {
            em.getTransaction().begin();
            
            em.createQuery("delete from User").executeUpdate();
            em.createQuery("delete from Role").executeUpdate();

            Role salespersonRole = new Role("salesperson");
            Role adminRole = new Role("admin");
            SALESPERSON.addRole(salespersonRole);
            User admin = new User("admin", "test");
            admin.addRole(adminRole);
            em.persist(salespersonRole);
            em.persist(adminRole);
            em.persist(SALESPERSON);
            em.persist(admin);
            em.getTransaction().commit();
        } finally 
        {
            em.close();
        }
    }

    @Test
    public void getVeryfiedUserTest() throws AuthenticationException 
    {
        
        User expected = SALESPERSON;
        User result = facade.getVeryfiedUser(SALESPERSON.getUserName(), "test");

        assertEquals(expected.getUserPass(), result.getUserPass());
    }

}
