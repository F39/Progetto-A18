package Controllers;

import Application.Connect4Application;
import DatabaseManagement.User;
import GameLogic.MatchFlowState;
import Utils.*;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.print.attribute.standard.Media;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Singleton
@Path("/game")
public class RestGameController{

    //    @Inject
    private GameControllerInt gameControllerInt;


    public RestGameController(){
        gameControllerInt = Connect4Application.gameController;
    }

    @POST
    @Path("/newgame")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response newGame (@HeaderParam("token") String token, CommandNewGame command){
        if(checkAuthToken(token, command.getUsername())){
            gameControllerInt.newGame(command);
            return Response.ok().build();
        }
        return Response.status(Response.Status.FORBIDDEN).build();
    }

    @POST
    @Path("/move")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response move (@HeaderParam("token") String token, CommandMove command){
        if(checkAuthToken(token, command.getUsername())){
            gameControllerInt.move(command);
            return Response.ok().build();
        }
        return Response.status(Response.Status.FORBIDDEN).build();
    }

    @POST
    @Path("/pause")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response pause (@HeaderParam("token") String token, CommandPause command){
        if(checkAuthToken(token, command.getUsername())){
            gameControllerInt.pause(command);
            return Response.ok().build();
        }
        return Response.status(Response.Status.FORBIDDEN).build();
    }

    @POST
    @Path("/quit")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response quit (@HeaderParam("token") String token, CommandQuit command){
        if(checkAuthToken(token, command.getUsername())){
            gameControllerInt.quit(command);
            return Response.ok().build();
        }
        return Response.status(Response.Status.FORBIDDEN).build();
    }

    @POST
    @Path("/poll")
    @Produces(MediaType.APPLICATION_JSON)
    public Response poll(@HeaderParam("token") String token, String username){
        if(checkAuthToken(token, username)){
            UserController.getOnline().get(token).poll();
            AbstractCommand response;
            for (AbstractCommand command : gameControllerInt.getCommandsOut()) {
                if(username.equals(command.getUsername())){
                    response = command;
                    gameControllerInt.deleteCommandOut(command);
                    return Response.ok(response).build();
                }
            }
            response = new CommandOut(username, 0, MatchFlowState.running, -2);
            return Response.ok(response).build();
        }
        return Response.status(Response.Status.FORBIDDEN).build();
    }


    private boolean checkAuthToken(String token, String username) {
        User checkUser = UserController.getOnline().get(token).getUser();
        if(checkUser == null){
            return false;
        }
        if(username.equals(checkUser.getUsername())){
            return true;
        }
        return false;
    }


}