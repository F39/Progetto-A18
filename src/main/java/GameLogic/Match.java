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

    private long threshold;

    public Match(Player player1, int gameId) {
        this.gameId = gameId;
        players.put(1, player1);
        matchFlowState = MatchFlowState.started1;
        turn = 1;
        board = new Board();
        lastMove = -1;
        moved = false;
        timeout = System.currentTimeMillis();
    }

    public Match(Player player1, Player player2, int gameId, Mode mode) {
        this(player1, gameId);
        players.put(2, player2);
        if (mode == Mode.MultiPlayerTurbo) {
            threshold = 10;
        } else {
            threshold = 60;
        }
    }

    public Match(Player player1, Mode mode, int gameId) {
        this(player1, gameId);
        if (mode.equals(Mode.StrategyRandom)) {
            aiStrategyInt = new AIStrategyRandom();
        } else if (mode.equals(Mode.StrategyNForecasting)) {
            //aiStrategyInt = new AIStrategy4StepForecasting(this, 4, -3, 2, -1);
            aiStrategyInt = new AIStrategy4StepForecasting(this, 5, -4, 3, -1);
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
        if (aiStrategyInt != null) {

        }
        return players;
    }

    public void move(int move) {
        int result;
        timeout = System.currentTimeMillis();
        moved = true;
        lastMove = move;
        result = board.move(move, turn);
        checkResult(result);

        turn -= Math.pow(-1, turn);
        if (aiStrategyInt != null && matchFlowState != MatchFlowState.winner1 && matchFlowState != MatchFlowState.winner2 && matchFlowState != MatchFlowState.tie) {
            lastMove = aiStrategyInt.move();
            result = board.move(lastMove, turn);
            checkResult(result);
            turn -= Math.pow(-1, turn);
        }
    }

    private void checkResult(int result) {
        if (result == 1) {
            matchFlowState = MatchFlowState.winner1;
            return;
        } else if (result == 2) {
            matchFlowState = MatchFlowState.winner2;
            return;
        } else if (result == -1) {
            matchFlowState = MatchFlowState.tie;
            return;
        } else {
            matchFlowState = MatchFlowState.running;
            return;
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
        if (players.get(1).getUsername() == player.getUsername()) {
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
        timeout = System.currentTimeMillis();
        while (!matchFlowState.equals(MatchFlowState.quitted)) {
            if ((matchFlowState.equals(MatchFlowState.winner1)) || (matchFlowState.equals(MatchFlowState.winner2)) || (matchFlowState.equals(MatchFlowState.tie))) {
                break;
            }
            if (System.currentTimeMillis() - timeout > threshold * 1000) {
                if (turn == 1) {
                    matchFlowState = MatchFlowState.winner2;
                } else {
                    matchFlowState = MatchFlowState.winner1;
                }

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

    public AIStrategyInt getAiStrategyInt() {
        return aiStrategyInt;
    }

}
