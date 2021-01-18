package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nimbusds.jose.shaded.json.JSONObject;
import dto.ContactDTO;
import dto.EmailDTO;
import entities.User;
import errorhandling.API_Exception;
import errorhandling.Messages;
import facades.SalespersonFacade;
import facades.TestDataFacade;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import utils.EMF_Creator;

@Path("salesperson")
public class SalespersonResource 
{
    
    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    @Context
    private UriInfo context;

    @Context
    SecurityContext securityContext;
    
    public static final SalespersonFacade SALESPERSON_FACADE = SalespersonFacade.getSalespersonFacade(EMF);
    
    private static final Messages MESSAGES = new Messages();
    
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String isUp() {
        return String.format("{\"message\":\"%s\"}", MESSAGES.SERVER_IS_UP);
    }

    @POST
    @Path("contacts/create-contact")
    @RolesAllowed("salesperson")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public String createContact(String contactInformation) throws API_Exception
    {
        ContactDTO contactDTO = GSON.fromJson(contactInformation, ContactDTO.class);
        
        return GSON.toJson(SALESPERSON_FACADE.createContact
        (
            contactDTO.getName(), 
            contactDTO.getEmail(), 
            contactDTO.getCompany(), 
            contactDTO.getJobtitle(), 
            contactDTO.getPhone()
        ));
    }
    
    @GET
    @Path("contacts/all")
    @RolesAllowed("salesperson")
    @Produces({MediaType.APPLICATION_JSON})
    public String getAllContacts() throws API_Exception
    {   
        return GSON.toJson(SALESPERSON_FACADE.getAllContacts());
    }
    
    @POST
    @Path("contacts/single")
    @RolesAllowed("salesperson")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public String getSingleContact(String jsonEmail) throws API_Exception
    {
        String email = GSON.fromJson(jsonEmail, EmailDTO.class).getEmail();
        return GSON.toJson(SALESPERSON_FACADE.getSingleContact(email));
    }
    
    @PUT
    @Path("contacts/edit/{oldemail}")
    @RolesAllowed("salesperson")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public String editContact(@PathParam("oldemail") String oldEmail, String contactInformation) throws API_Exception
    {
        ContactDTO contactDTO = GSON.fromJson(contactInformation, ContactDTO.class);
        
        return GSON.toJson(SALESPERSON_FACADE.editContact
        (
            oldEmail,
            contactDTO.getName(), 
            contactDTO.getEmail(), 
            contactDTO.getCompany(), 
            contactDTO.getJobtitle(), 
            contactDTO.getPhone()
        ));
    }
}