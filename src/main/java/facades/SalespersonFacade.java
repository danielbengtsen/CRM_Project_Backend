
package facades;

import dto.ContactDTO;
import entities.Contact;
import errorhandling.API_Exception;
import errorhandling.Messages;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;


public class SalespersonFacade 
{
    private static EntityManagerFactory emf;
    private static SalespersonFacade instance;

    private static final Messages MESSAGES = new Messages();

    private SalespersonFacade() {}

    /**
     *
     * @param _emf
     * @return the instance of this facade.
     */
    public static SalespersonFacade getSalespersonFacade(EntityManagerFactory _emf) 
    {
        if (instance == null) 
        {
            emf = _emf;
            instance = new SalespersonFacade();
        }
        return instance;
    }
    
    
    public ContactDTO createContact(String name, String email, String company, String jobtitle, String phone) throws API_Exception 
    {
        EntityManager em = emf.createEntityManager();
        
        if(name.isEmpty() || email.isEmpty() || company.isEmpty() || jobtitle.isEmpty() || phone.isEmpty())
        {
            throw new API_Exception(MESSAGES.MISSING_INPUT, 400);
        }
        
        Contact contact = new Contact(name, email, company, jobtitle, phone);
        ContactDTO contactDTO = new ContactDTO(contact);
        
        try 
        {
            em.getTransaction().begin();
                em.persist(contact);
            em.getTransaction().commit();
        } finally 
        {
            em.close();
        }
        return contactDTO;
    }
}
