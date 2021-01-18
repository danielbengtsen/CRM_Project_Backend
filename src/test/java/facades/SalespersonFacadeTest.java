package facades;

import dto.ContactDTO;
import entities.Contact;
import entities.Role;
import entities.User;
import errorhandling.API_Exception;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import security.errorhandling.AuthenticationException;
import utils.EMF_Creator;

public class SalespersonFacadeTest 
{

    private static EntityManagerFactory emf;
    private static SalespersonFacade facade;
    private static final User SALESPERSON = new User("salesperson", "test");
    private static final String CONTACT_NAME = "Test name";
    private static final String CONTACT_EMAIL = "Testmail@mail.com";
    private static final String CONTACT_COMPANY = "Test company";
    private static final String CONTACT_JOBTITLE = "Test title";
    private static final String CONTACT_PHONE = "Test phone";

    public SalespersonFacadeTest() {}

    @BeforeAll
    public static void setUpClass() 
    {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        facade = SalespersonFacade.getSalespersonFacade(emf);
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
            em.createQuery("delete from Contact").executeUpdate();
            em.createQuery("delete from Opportunity").executeUpdate();

            User salesperson = new User(SALESPERSON.getUserName(), SALESPERSON.getUserPass());
            Role salespersonRole = new Role("salesperson");
            salesperson.addRole(salespersonRole);
            em.persist(salespersonRole);
            em.persist(salesperson);
            em.getTransaction().commit();
        } finally 
        {
            em.close();
        }
    }

    @Test
    public void createContactTest() throws API_Exception 
    {
        Contact expected = new Contact(CONTACT_NAME, CONTACT_EMAIL, CONTACT_COMPANY, CONTACT_JOBTITLE, CONTACT_PHONE);
        ContactDTO result = facade.createContact(CONTACT_NAME, CONTACT_EMAIL, CONTACT_COMPANY, CONTACT_JOBTITLE, CONTACT_PHONE);

        assertEquals(expected.getName(), result.getName());
        assertEquals(expected.getEmail(), result.getEmail());
        assertEquals(expected.getCompany(), result.getCompany());
        assertEquals(expected.getJobtitle(), result.getJobtitle());
        assertEquals(expected.getPhone(), result.getPhone());
    }

}
