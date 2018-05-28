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
    public Response signUp(User user) {
        System.out.println(user.getUsername());
        if (addUser(user)) {
            return Response.status(Status.OK).build();
        }
        return Response.status(Status.BAD_REQUEST).build();
    }

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(User user) {
        try {
            if ((user = userRepository.checkUserCredential(user.getUsername(), user.getPassword())) != null) {
                String newToken = generateAuthToken();
                userRepository.updateUserAuthToken(newToken, user.getUsername());
                user.setToken(newToken);
                return Response.ok(new JSONObject("{\"token\":\"" + user.getToken() + "\"}").toString(), MediaType.APPLICATION_JSON).build();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Response.status(Status.BAD_REQUEST).entity("Login failed: the provided credentials are not valid ones.").build();
    }

    private boolean checkDBToken(String user, String token) {
        // TODO : check on db/list if token is rightù
        return true;
    }

    private boolean addUser(User user) {
        try {
            userRepository.create(user);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private String generateAuthToken() {
        UUID authToken = UUID.randomUUID();
        return authToken.toString();
    }
}
