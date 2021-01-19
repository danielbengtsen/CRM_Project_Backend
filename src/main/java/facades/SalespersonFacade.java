
package facades;

import dto.ContactDTO;
import entities.Contact;
import errorhandling.API_Exception;
import errorhandling.Messages;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;


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
    
    public List<ContactDTO> getAllContacts() throws API_Exception
    {
        EntityManager em = emf.createEntityManager();
        
        try 
        {
            List<Contact> allContacts = em.createNamedQuery("Contact.getAllRows").getResultList();
            List<ContactDTO> allContactsDTO = new ArrayList();
            for(Contact c : allContacts)
            {
                allContactsDTO.add(new ContactDTO(c));
            }
            if(allContacts.isEmpty()) 
            {
                throw new API_Exception(MESSAGES.NO_CONTACTS_FOUND, 404);
            }
            return allContactsDTO;
        } finally 
        {
            em.close();
        }
    }
    
    public ContactDTO getSingleContact(String email) throws API_Exception
    {
        EntityManager em = emf.createEntityManager();
        
        if(email.isEmpty())
        {
            throw new API_Exception(MESSAGES.MISSING_INPUT, 400);
        }
        
        ContactDTO contactDTO;
        
        try 
        {
            TypedQuery<Contact> query = em.createQuery("SELECT c FROM Contact c WHERE c.email = :email", Contact.class)
                .setParameter("email", email);
            if(query.getSingleResult() == null)
            {
                throw new API_Exception(MESSAGES.CANNOT_FIND_CONTACT, 404);
            }
            contactDTO = new ContactDTO(query.getSingleResult());  
        } finally 
        {
            em.close();
        }
        return contactDTO;
    }
    
    public ContactDTO editContact(String oldEmail, String name, String email, String company, String jobtitle, String phone) throws API_Exception
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
            TypedQuery<Contact> query = em.createQuery("SELECT c FROM Contact c WHERE c.email = :email", Contact.class)
                .setParameter("email", oldEmail);
            if(query.getSingleResult() == null)
            {
                throw new API_Exception(MESSAGES.CANNOT_FIND_CONTACT, 404);
            }
            
            Contact contactToEdit = query.getSingleResult();

            em.getTransaction().begin();
                contactToEdit.setName(contactDTO.getName());
                contactToEdit.setEmail(contactDTO.getEmail());
                contactToEdit.setCompany(contactDTO.getCompany());
                contactToEdit.setJobtitle(contactDTO.getJobtitle());
                contactToEdit.setPhone(contactDTO.getPhone());
            em.getTransaction().commit();
        } finally 
        {
            em.close();
        }
        return contactDTO;
    }
    
    
    public ContactDTO deleteContact(String email) throws API_Exception
    {
        EntityManager em = emf.createEntityManager();
        
        if(email.isEmpty())
        {
            throw new API_Exception(MESSAGES.MISSING_INPUT, 400);
        }
        
        ContactDTO contactDTO;
        
        try 
        {
            TypedQuery<Contact> query = em.createQuery("SELECT c FROM Contact c WHERE c.email = :email", Contact.class)
                .setParameter("email", email);
            if(query.getSingleResult() == null)
            {
                throw new API_Exception(MESSAGES.CANNOT_FIND_CONTACT, 404);
            }
            Contact c = query.getSingleResult();
            em.getTransaction().begin();
                em.createQuery("DELETE FROM Contact c WHERE c.email = :email").setParameter("email", c.getEmail()).executeUpdate();
            em.getTransaction().commit();
            contactDTO = new ContactDTO(c);  
        } finally 
        {
            em.close();
        }
        return contactDTO;
    }
}
