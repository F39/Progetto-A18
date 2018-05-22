package Controllers;

import DatabaseManagement.User;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

@Path("/user")
public class UserController {

    @POST
    @Path("/signup")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response signUp(User user){

        if(addUser(user)){
            return Response.status(Status.OK).build();
        }
        return Response.status(Status.BAD_REQUEST).build();
    }

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String login(User user){

        if(checkCredentials(user.getUser(), user.getPassword())){
            return generateToken();
        }
        //return user;
        return null;
    }

    private boolean addUser(User user) {

        // TODO : add user to db
        return false;
    }

    private String generateToken() {
        // TODO : Discuss about policy to generate token
        return "Token";
    }

    private boolean checkCredentials(String username, String password) {
        // TODO : Query on db to check if user exists
        return true;
    }
}
