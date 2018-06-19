package GameLogic;

import GameLogic.ArtificialIntelligence.AIStrategy4StepForecasting;
import GameLogic.ArtificialIntelligence.AIStrategyInt;
import GameLogic.ArtificialIntelligence.AIStrategyRandom;

import java.util.HashMap;
import java.util.Map;

public class Match extends Thread {

    private int gameId;
    private MatchFlowState matchFlowState;
    private int turn;
    private Map<Integer, Player> players = new HashMap<>();
    private Board board;
    private int lastMove;
    private boolean moved;
    private long timeout;
    private AIStrategyInt aiStrategyInt;

    private final long threshold = 60;

    public Match(Player player1, int gameId) {
        this.gameId = gameId;
        players.put(1, player1);
        matchFlowState = MatchFlowState.started1;
        turn = 1;
        board = new Board();
        lastMove = -1;
        moved = false;
    }

    public Match(Player player1, Player player2, int gameId) {
        this(player1, gameId);
        players.put(2, player2);
    }

    public Match(Player player1, Mode mode, int gameId) {
        this(player1, gameId);
        if (mode.equals(Mode.StrategyRandom)) {
            aiStrategyInt = new AIStrategyRandom();
        } else if (mode.equals(Mode.StrategyNForecasting)) {
            aiStrategyInt = new AIStrategy4StepForecasting(this, 4, -3, 2, -1);
        }
    }

    public Board getBoard() {
        return board;
    }

    public int getGameId() {
        return gameId;
    }

    public int getTurn() {
        return turn;
    }

    public Map<Integer, Player> getPlayers() {
        return players;
    }

    public void move(int move) {
        timeout = System.currentTimeMillis();
        moved = true;
        lastMove = move;
        int result = board.move(move, turn);
        if (result == 1) {
            matchFlowState = MatchFlowState.winner1;
        } else if (result == 2) {
            matchFlowState = MatchFlowState.winner2;
        } else if (result == -1) {
            matchFlowState = MatchFlowState.tie;
        } else {
            matchFlowState = MatchFlowState.running;
        }
        turn -= Math.pow(-1, turn);
        if (aiStrategyInt != null) {
            aiStrategyInt.move();
            turn -= Math.pow(-1, turn);
        }
    }

    public MatchFlowState getMatchFlowState() {
        return matchFlowState;
    }

    public void startGame() {
        this.start();
    }

    public void resumeGame(Player player) {
        matchFlowState = MatchFlowState.resumed;
    }

    public void pauseGame(Player player) {
        matchFlowState = MatchFlowState.paused;
    }

    public void quitGame(Player player) {
        matchFlowState = MatchFlowState.quitted;
        if (turn == 1) {
            matchFlowState = MatchFlowState.winner2;
        } else {
            matchFlowState = MatchFlowState.winner1;
        }
    }

    public int getLastMove() {
        if (moved) {
            return lastMove;
        }
        moved = false;
        return -1;
    }

    @Override
    public void run() {
        while (matchFlowState.equals(MatchFlowState.quitted)) {
            if ((matchFlowState.equals(MatchFlowState.winner1)) || (matchFlowState.equals(MatchFlowState.winner2)) || (matchFlowState.equals(MatchFlowState.tie))) {
                break;
            }
            if (System.currentTimeMillis() - timeout > threshold * 1000) {
                break;
            }
            if (matchFlowState.equals(MatchFlowState.paused)) {
                timeout += 100;
            }
            try {
                sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

//    private MatchFlowState whoWin(MatchEndState matchEndState){
//        MatchFlowState matchFlowState;
//        if(turn == 1){
//            if(matchEndState.equals(MatchEndState.move)){
//                matchFlowState = MatchFlowState.winner1;
//            }else{
//                matchFlowState = MatchFlowState.winner2;
//            }
//        }else{
//            if(matchEndState.equals(MatchEndState.move)){
//                matchFlowState = MatchFlowState.winner2;
//            }else{
//                matchFlowState = MatchFlowState.winner1;
//            }
//        }
//        return matchFlowState;
//    }
}
