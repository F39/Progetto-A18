package Controllers;

import DatabaseManagement.User;

import javax.ws.rs.*;
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
    @Produces(MediaType.APPLICATION_JSON)
    public User login(User user){

        if(checkCredentials(user.getUsername(), user.getPassword())){
            System.out.println();
            return user;

        }
        //return user;
        return null;
    }

    @POST
    @Path("/token/{tokenid}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response checkToken(User user, @PathParam("tokenid") String token){
        System.out.println("User " + user.getUsername() +  " token: " + token);
        if(checkDBToken(user.getUsername(), token)){
            return Response.status(Status.OK).build();
        }
        return Response.status(Status.BAD_REQUEST).build();
    }

    private boolean checkDBToken(String user, String token) {
        // TODO : check on db/list if token is rightù
        return true;
    }

    private boolean addUser(User user) {

        // TODO : add user to db
        return true;
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
