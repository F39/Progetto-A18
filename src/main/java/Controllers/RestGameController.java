package Controllers;

import Application.Connect4Application;
import DatabaseManagement.User;
import GameLogic.MatchFlowState;
import Logger.Logger;
import Utils.*;

import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Singleton
@Path("/game")
public class RestGameController {

    private GameControllerInt gameControllerInt;
    private Logger logger;

    public RestGameController() {
        gameControllerInt = Connect4Application.gameController;
        logger = Logger.getInstance();
    }

    @POST
    @Path("/newgame")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response newGame(@HeaderParam("token") String token, CommandNewGame command) {
        if (checkAuthToken(token, command.getUsername())) {
            logger.log("New game request received");
            gameControllerInt.newGame(command);
            return Response.ok().build();
        }
        logger.log("Invalid new game request received");
        return Response.status(Response.Status.FORBIDDEN).build();
    }

    @POST
    @Path("/accept")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response accept(@HeaderParam("token") String token, CommandAcceptMatch command){
        if (checkAuthToken(token, command.getUsername())) {
            logger.log("Accept request received");
            gameControllerInt.accept(command);
            return Response.ok().build();
        }
        logger.log("Invalid accept request received");
        return Response.status(Response.Status.FORBIDDEN).build();
    }

    @POST
    @Path("/move")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response move(@HeaderParam("token") String token, CommandMove command) {
        if (checkAuthToken(token, command.getUsername())) {
            gameControllerInt.move(command);
            return Response.ok().build();
        }
        return Response.status(Response.Status.FORBIDDEN).build();
    }

    @POST
    @Path("/pause")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response pause(@HeaderParam("token") String token, CommandPause command) {
        if (checkAuthToken(token, command.getUsername())) {
            logger.log("Pause request received");
            gameControllerInt.pause(command);
            return Response.ok().build();
        }
        logger.log("Invalid pause request received");
        return Response.status(Response.Status.FORBIDDEN).build();
    }

    @POST
    @Path("/quit")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response quit(@HeaderParam("token") String token, CommandQuit command) {
        if (checkAuthToken(token, command.getUsername())) {
            logger.log("Quit request received");
            gameControllerInt.quit(command);
            return Response.ok().build();
        }
        logger.log("Invalid quit request received");
        return Response.status(Response.Status.FORBIDDEN).build();
    }

    @POST
    @Path("/poll")
    @Produces(MediaType.APPLICATION_JSON)
    public Response poll(@HeaderParam("token") String token, String username) {
        if (checkAuthToken(token, username)) {
            UserController.getOnline().get(token).poll();
            AbstractCommand response;
            for (AbstractCommand command : gameControllerInt.getCommandsOut()) {
                if (username.equals(command.getUsername())) {
                    response = command;
                    gameControllerInt.deleteCommandOut(command);
                    return Response.ok(response).build();
                }
            }
            response = new CommandOut(username, 0, MatchFlowState.running, -2);
            return Response.ok(response).build();
        }
        logger.log("Invalid poll request received");
        return Response.status(Response.Status.FORBIDDEN).build();
    }

    private boolean checkAuthToken(String token, String username) {
        List<String> tokens = new ArrayList<>(UserController.getOnline().keySet());
        if(tokens.contains(token)){
            User checkUser = UserController.getOnline().get(token).getUser();
            if (checkUser == null) {
                return false;
            }
            if (username.equals(checkUser.getUsername())) {
                return true;
            }
        }

        logger.log("Invalid token request received");
        return false;
    }

}