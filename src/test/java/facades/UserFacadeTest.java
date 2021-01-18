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
    private static final User user = new User("user", "password");

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

            Role userRole = new Role("user");
            Role adminRole = new Role("admin");
            user.addRole(userRole);
            User admin = new User("admin", "password");
            admin.addRole(adminRole);
            User both = new User("user_admin", "password");
            both.addRole(userRole);
            both.addRole(adminRole);
            em.persist(userRole);
            em.persist(adminRole);
            em.persist(user);
            em.persist(admin);
            em.persist(both);
            em.getTransaction().commit();
        } finally 
        {
            em.close();
        }
    }

    @Test
    public void getVeryfiedUserTest() throws AuthenticationException 
    {
        
        User expected = user;
        User result = facade.getVeryfiedUser(user.getUserName(), "password");

        assertEquals(expected.getUserPass(), result.getUserPass());
    }

}
