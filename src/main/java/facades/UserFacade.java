package facades;

import dto.UserDTO;
import entities.User;
import errorhandling.API_Exception;
import errorhandling.Messages;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import security.errorhandling.AuthenticationException;

public class UserFacade 
{

    private static EntityManagerFactory emf;
    private static UserFacade instance;

    private static final Messages MESSAGES = new Messages();

    private UserFacade() {}

    /**
     *
     * @param _emf
     * @return the instance of this facade.
     */
    public static UserFacade getUserFacade(EntityManagerFactory _emf) 
    {
        if (instance == null) 
        {
            emf = _emf;
            instance = new UserFacade();
        }
        return instance;
    }

    public User getVeryfiedUser(String username, String password) throws AuthenticationException 
    {
        EntityManager em = emf.createEntityManager();
        User user;
        try 
        {
            user = em.find(User.class, username);
            if (user == null || !user.verifyPassword(password)) {
                throw new AuthenticationException(MESSAGES.INVALID_USERNAME_OR_PWD);
            }
        } finally 
        {
            em.close();
        }
        return user;
    }

    public List<UserDTO> getAllUsers() throws API_Exception 
    {
        EntityManager em = emf.createEntityManager();
        List<User> allUsers = new ArrayList();
        List<UserDTO> allUsersDTO = new ArrayList();

        try 
        {
            allUsers = em.createNamedQuery("User.getAllRows").getResultList();
            if (allUsers.isEmpty() || allUsers == null) 
            {
                throw new API_Exception(MESSAGES.NO_USERS_FOUND, 404);
            }

            for (User user : allUsers) 
            {
                allUsersDTO.add(new UserDTO(user));
            }

            return allUsersDTO;
        } finally 
        {
            em.close();
        }
    }

}
