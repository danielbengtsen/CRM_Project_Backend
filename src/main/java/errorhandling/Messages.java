package errorhandling;


public class Messages 
{
    
    public final String INVALID_USERNAME_OR_PWD = "Invalid username or password! Please try again";
    public final String NOT_AUTHORIZED = "You are not authorized to perform the requested operation";
    public final String RESOURCE_NOT_FOUND = "Resource Not Found";
    public final String NOT_AUTHENTICADED = "Not authenticated - do login";
    public final String TOKEN_INVALID_OR_EXPIRED = "Token not valid (timed out?)";
    public final String TOKEN_EXPIRED = "Your Token is no longer valid";
    public final String TOKEN_CANNOT_EXTRACT_USER = "User could not be extracted from token";
    public final String NO_USERS_FOUND = "No users found";
    
    public Messages() {}
    
}
