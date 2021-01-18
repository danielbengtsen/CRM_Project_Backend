
package rest;

import entities.Contact;
import entities.Role;
import entities.User;
import errorhandling.API_Exception;
import errorhandling.Messages;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import io.restassured.parsing.Parser;
import java.net.URI;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.UriBuilder;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.not;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import utils.EMF_Creator;

public class SalespersonResourceTest {
    
    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";
    private static final User SALESPERSON = new User("salesperson", "test");
    
    private static final String CONTACT_NAME = "Test name";
    private static final String CONTACT_EMAIL = "Testmail@mail.com";
    private static final String CONTACT_COMPANY = "Test company";
    private static final String CONTACT_JOBTITLE = "Test title";
    private static final String CONTACT_PHONE = "Test phone";
    
    private static final String CONTACT_NAME_2 = "Name test";
    private static final String CONTACT_EMAIL_2 = "Mailtest@mail.com";
    private static final String CONTACT_COMPANY_2 = "Company test";
    private static final String CONTACT_JOBTITLE_2 = "Title test";
    private static final String CONTACT_PHONE_2 = "Phone test";

    static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
    private static HttpServer httpServer;
    private static EntityManagerFactory emf;
    
    private static final Messages MESSAGES = new Messages();
    
    static HttpServer startServer() {
        ResourceConfig rc = ResourceConfig.forApplication(new ApplicationConfig());
        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);
    }

    @BeforeAll
    public static void setUpClass() {
        //This method must be called before you request the EntityManagerFactory
        EMF_Creator.startREST_TestWithDB();
        emf = EMF_Creator.createEntityManagerFactoryForTest();

        httpServer = startServer();
        //Setup RestAssured
        RestAssured.baseURI = SERVER_URL;
        RestAssured.port = SERVER_PORT;
        RestAssured.defaultParser = Parser.JSON;
    }

    @AfterAll
    public static void closeTestServer() {
        //Don't forget this, if you called its counterpart in @BeforeAll
        EMF_Creator.endREST_TestWithDB();
        
        httpServer.shutdownNow();
    }

    // Setup the DataBase (used by the test-server and this test) in a known state BEFORE EACH TEST
    //TODO -- Make sure to change the EntityClass used below to use YOUR OWN (renamed) Entity class
    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
                em.createQuery("delete from User").executeUpdate();
                em.createQuery("delete from Role").executeUpdate();
                em.createQuery("delete from Contact").executeUpdate();
                em.createQuery("delete from Opportunity").executeUpdate();

                Contact contact1 = new Contact(CONTACT_NAME, CONTACT_EMAIL, CONTACT_COMPANY, CONTACT_JOBTITLE, CONTACT_PHONE);
                Contact contact2 = new Contact(CONTACT_NAME_2, CONTACT_EMAIL_2, CONTACT_COMPANY_2, CONTACT_JOBTITLE_2, CONTACT_PHONE_2);

                User salesperson = new User(SALESPERSON.getUserName(), SALESPERSON.getUserPass());
                Role salespersonRole = new Role("salesperson");
                salesperson.addRole(salespersonRole);
                em.persist(salespersonRole);
                em.persist(salesperson);
                em.persist(contact1);
                em.persist(contact2);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    //This is how we hold on to the token after login, similar to that a client must store the token somewhere
    private static String securityToken;

    //Utility method to login and set the returned securityToken
    private static void login(String role, String password) {
        String json = String.format("{username: \"%s\", password: \"%s\"}", role, password);
        securityToken = given()
                .contentType("application/json")
                .body(json)
                .when().post("/login")
                .then()
                .extract().path("token");
    }

    private void logOut() {
        securityToken = null;
    }

    @Test
    public void serverIsRunningTest() {
        given().when().get("/salesperson").then().statusCode(200);
    }
    
    public SalespersonResourceTest() {
    }
    
    @Test
    public void createContactTestRest() {
        String jsonRequest = String.format(
                "{ \"name\": \"%s\", "
                + "\"email\": \"%s\","
                + "\"company\": \"%s\","
                + "\"jobtitle\": \"%s\","
                + "\"phone\": \"%s\" }", CONTACT_NAME, CONTACT_EMAIL, CONTACT_COMPANY, CONTACT_JOBTITLE, CONTACT_PHONE);
        
        login(SALESPERSON.getUserName(), SALESPERSON.getUserPass());
        
        given()
                .contentType("application/json")
                .body(jsonRequest)
                .header("x-access-token", securityToken)
                .when().post("/salesperson/contacts/create-contact").then()
                .statusCode(200)
                .body("name", equalTo(CONTACT_NAME))
                .body("email", equalTo(CONTACT_EMAIL))
                .body("company", equalTo(CONTACT_COMPANY))
                .body("jobtitle", equalTo(CONTACT_JOBTITLE))
                .body("phone", equalTo(CONTACT_PHONE));
    }
    
    @Test
    public void missingInput_createContactTestRest() {
        String jsonRequest = String.format(
                "{ \"name\": \"\", "
                + "\"email\": \"%s\","
                + "\"company\": \"%s\","
                + "\"jobtitle\": \"%s\","
                + "\"phone\": \"%s\" }", CONTACT_EMAIL, CONTACT_COMPANY, CONTACT_JOBTITLE, CONTACT_PHONE);
        
        login(SALESPERSON.getUserName(), SALESPERSON.getUserPass());
        
        given()
                .contentType("application/json")
                .body(jsonRequest)
                .header("x-access-token", securityToken)
                .when().post("/salesperson/contacts/create-contact").then()
                .statusCode(400)
                .body("message", equalTo(MESSAGES.MISSING_INPUT));
    }
    
    @Test
    public void notAuthenticated_createContactTestRest() {
        String jsonRequest = String.format(
                "{ \"name\": \"%s\", "
                + "\"email\": \"%s\","
                + "\"company\": \"%s\","
                + "\"jobtitle\": \"%s\","
                + "\"phone\": \"%s\" }", CONTACT_NAME, CONTACT_EMAIL, CONTACT_COMPANY, CONTACT_JOBTITLE, CONTACT_PHONE);
        
        given()
                .contentType("application/json")
                .body(jsonRequest)
                .when().post("/salesperson/contacts/create-contact").then()
                .statusCode(403)
                .body("message", equalTo(MESSAGES.NOT_AUTHENTICADED));
    }
    
    @Test
    public void getAllContactsTestRest() {
        
        login(SALESPERSON.getUserName(), SALESPERSON.getUserPass());
        
        given()
                .contentType("application/json")
                .header("x-access-token", securityToken)
                .when().get("/salesperson/contacts/all").then()
                .statusCode(200).assertThat()
                .body("[0].name", not(equalTo(null)))
                .body("[1].name", not(equalTo(null)));
    }
    
    @Test
    public void notAuthenticated_getAllContactsTestRest() {
        given()
                .contentType("application/json")
                .when().get("/salesperson/contacts/all").then()
                .statusCode(403)
                .body("message", equalTo(MESSAGES.NOT_AUTHENTICADED));
    }
    
    @Test
    public void getSingleContactTestRest() {
        String jsonRequest = String.format(
                "{ \"email\": \"%s\" }", CONTACT_EMAIL);
        
        login(SALESPERSON.getUserName(), SALESPERSON.getUserPass());
        
        given()
                .contentType("application/json")
                .body(jsonRequest)
                .header("x-access-token", securityToken)
                .when().post("/salesperson/contacts/single").then()
                .statusCode(200)
                .body("name", equalTo(CONTACT_NAME))
                .body("email", equalTo(CONTACT_EMAIL))
                .body("company", equalTo(CONTACT_COMPANY))
                .body("jobtitle", equalTo(CONTACT_JOBTITLE))
                .body("phone", equalTo(CONTACT_PHONE));
    }
    
    @Test
    public void missingInput_getSingleContactTestRest() {
        String jsonRequest = "{ \"email\": \"\" }";
        
        login(SALESPERSON.getUserName(), SALESPERSON.getUserPass());
        
        given()
                .contentType("application/json")
                .body(jsonRequest)
                .header("x-access-token", securityToken)
                .when().post("/salesperson/contacts/single").then()
                .statusCode(400)
                .body("message", equalTo(MESSAGES.MISSING_INPUT));
    }
    
    @Test
    public void notAuthenticated_getSingleContactTestRest() {
        String jsonRequest = String.format(
                "{ \"email\": \"%s\" }", CONTACT_EMAIL);
        
        given()
                .contentType("application/json")
                .body(jsonRequest)
                .when().post("/salesperson/contacts/single").then()
                .statusCode(403)
                .body("message", equalTo(MESSAGES.NOT_AUTHENTICADED));
    }
    
}
