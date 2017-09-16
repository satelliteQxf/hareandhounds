package com.oose.xqiu3.hareandhounds;

import com.google.gson.Gson;
import com.oose.xqiu3.hareandhounds.pojo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.UUID;
/**
 * Created by xqiu3 on 17/9/7.
 */
public class GameService {
    private final Logger logger = LoggerFactory.getLogger(GameService.class);
    private Map<Integer,BoardRecord> gameRecord;
    private Map<Integer,Player> gameInfo;
    private BoardOperation operation;
    private Map<String,Integer> gameIdString;
    //gameId acts as a primary key here

    private int autoIncrementNumber = 1;

    public GameService(){
        gameRecord = new HashMap<Integer,BoardRecord>();
        gameInfo = new HashMap<Integer,Player>();
        if(1 != autoIncrementNumber){
            autoIncrementNumber = 1;
        }
        operation = new BoardOperation();
        gameIdString = new HashMap<String,Integer>();
    }

    /**
     * start a game
     * @return startGamePOJO
     */
    public StartGamePOJO startGame(String body) throws MissArgumentException{
        //first determine if it's a empty request
        if(body.equals("")){
            throw new MissArgumentException("missing argument or wrong argument!",null);
        }
        //generate a new game_id
        StartGameRequest request = null;
        try{
            request = new Gson().fromJson(body,StartGameRequest.class);
        }catch (Exception ex){
            throw new MissArgumentException("missing argument or wrong argument!",null);
        }

        String pieceType = request.getPieceType();
        StartGamePOJO sgPOJO = new StartGamePOJO();
        int game_id = autoIncrementNumber++;
        String uuid = UUID.randomUUID().toString();
        gameIdString.put(uuid,game_id);

        sgPOJO.setGameId(uuid);
        sgPOJO.setPieceType(pieceType);

        //create a new boardRecord
        Board board = new Board();
        board.initialize();
        BoardRecord record = new BoardRecord();
        record.setRecord(board);
        gameRecord.put(game_id,record);


        Player players = new Player();
        if(pieceType.equals("HARE")){
            sgPOJO.setPlayerId("2");
            players.setHare(1);
            gameInfo.put(game_id,players);
        }else if(pieceType.equals("HOUND")){
            sgPOJO.setPlayerId("1");
            players.setHound(1);
            gameInfo.put(game_id,players);
        }else{
            throw new MissArgumentException("missing argument or wrong argument!",null);
        }

        return sgPOJO;
    }

    /**
     * Describe the game state
     * @return StatePOJO
     */
    public StatePOJO describeState(String gameId) throws InvalidGameException {
        int game_id = 0;
        StatePOJO stPOJO = new StatePOJO();

        if (!gameIdString.containsKey(gameId)) {
            throw new InvalidGameException("this is an invalid game id", null);
        }
        game_id = gameIdString.get(gameId);
        Board currBoard = gameRecord.get(game_id).getLatestBoard();
        BoardRecord currRecord = gameRecord.get(game_id);
        Player currPlayer = gameInfo.get(game_id);
        //check if we are waiting a second player
        if ((currPlayer.isSetHare() && currPlayer.isSetHound()) == false) {
            stPOJO.setState("WAITING_FOR_SECOND_PLAYER");
        } else if (operation.isHareWinByEscape(currBoard)) {//check if there is a winner
            stPOJO.setState("WIN_HARE_BY_ESCAPE");
        }else if(operation.isHoundWin(currBoard)){
            stPOJO.setState("WIN_HOUND");
        }else if(operation.isHareWinByHoundStall(currRecord)) {
            stPOJO.setState("WIN_HARE_BY_STALLING");
        }else if(currPlayer.getHound() == 2){
            stPOJO.setState("TURN_HOUND");
        }else if(currPlayer.getHare() == 2){
            stPOJO.setState("TURN_HARE");
        }
        return stPOJO;
    }

    public List<SinglePiece> describeBoard(String gameId) throws InvalidGameException{
        int game_id = 0;

        List<SinglePiece> returnList = new ArrayList<SinglePiece>();
        if (!gameIdString.containsKey(gameId)) {
            throw new InvalidGameException("this is an invalid game id", null);
        }
        game_id = gameIdString.get(gameId);
        Board currBoard = gameRecord.get(game_id).getLatestBoard();
        int[][] boardArray = currBoard.getBoard();
        for(int i = 0; i < 5; i++){
            for(int j = 0;j<3;j++){
                if(boardArray[i][j] == 1){
                    SinglePiece piece = new SinglePiece();
                    piece.pieceType = "HOUND";
                    piece.x = i;
                    piece.y = j;
                    returnList.add(piece);
                }else if(boardArray[i][j] == 2){
                    SinglePiece piece = new SinglePiece();
                    piece.pieceType = "HARE";
                    piece.x = i;
                    piece.y = j;
                    returnList.add(piece);
                }
            }
        }
        return returnList;
    }

    public StartGamePOJO joinGame(String gameId) throws InvalidGameException,PlayerAlreadyJoinedException{
        int game_id = 0;
        if (!gameIdString.containsKey(gameId)) {
            throw new InvalidGameException("this is an invalid game id", null);
        }
        game_id = gameIdString.get(gameId);

        StartGamePOJO sgPOJO = new StartGamePOJO();
        sgPOJO.setGameId(gameId);

        //which player to join
        Player currPlayers = gameInfo.get(game_id);
        if(currPlayers.isSetHound() == false){
            //join as hound
            currPlayers.setHound(2);
            sgPOJO.setPlayerId("1");
            sgPOJO.setPieceType("HOUND");
        }else if(currPlayers.isSetHare() == false){
            //join as hare
            currPlayers.setHound(2);
            currPlayers.setHare(1);
            sgPOJO.setPlayerId("2");
            sgPOJO.setPieceType("HARE");
        }else{
            throw new PlayerAlreadyJoinedException("there are already 2 players!",null);
        }
        return sgPOJO;
    }

    public void movePiece(String gameId,String body)throws InvalidGameException,
            BoardOperation.GameIllegalMoveException,IncorrectTurnException,InvalidPlayerException{
        MovePieceRequest request = new Gson().fromJson(body,MovePieceRequest.class);
        //first check if this game already finishes
        StatePOJO currPOJO = describeState(gameId);
        String currState = currPOJO.getState();
        if(currState.equals("WIN_HARE_BY_ESCAPE") ||
                currState.equals("WIN_HARE_BY_STALLING") || currState.equals("WIN_HOUND")){
            throw new IncorrectTurnException("the game ends!",null);
        }
        int playerRequest = 0;
        try{
            playerRequest = Integer.valueOf(request.playerId);
        }catch (Exception ex){
            throw new InvalidPlayerException("this is an invalid player id!",null);
        }
        int game_id = 0;
        if (!gameIdString.containsKey(gameId)) {
            throw new InvalidGameException("this is an invalid game id", null);
        }
        game_id = gameIdString.get(gameId);
        int fromX = Integer.valueOf(request.fromX);
        int fromY = Integer.valueOf(request.fromY);
        int toX = Integer.valueOf(request.toX);
        int toY = Integer.valueOf(request.toY);

        Board currBoard = gameRecord.get(game_id).getLatestBoard();
        Player currPlayers = gameInfo.get(game_id);

        if(playerRequest == 1){
            if(currPlayers.getHound() == 2){
                try {
                    Board afterBoard = operation.moveHound(currBoard,fromX,fromY,toX,toY);
                    //deal some logic after this move finished
                    int[][] boardArray = afterBoard.getBoard();
                    BoardRecord currRecord = gameRecord.get(game_id);
                    currRecord.setRecord(afterBoard);
                    gameRecord.put(game_id,currRecord);
                    //exchange the player
                    currPlayers.setHound(1);
                    currPlayers.setHare(2);

                }catch (BoardOperation.GameIllegalMoveException ex){
                    throw ex;
                }
            }else{
                throw new IncorrectTurnException("Incorrect turn!",null);
            }
        }else if(playerRequest == 2){
            if(currPlayers.getHare() == 2){
                try{
                    Board afterBoard = operation.moveHare(currBoard,fromX,fromY,toX,toY);
                    BoardRecord currRecord = gameRecord.get(game_id);
                    currRecord.setRecord(afterBoard);
                    currPlayers.setHound(2);
                    currPlayers.setHare(1);
                }catch(BoardOperation.GameIllegalMoveException ex){
                    throw ex;
                }
            }else{
                throw new IncorrectTurnException("Incorrect turn!",null);
            }
        }else{
            throw new InvalidPlayerException("Invalid player id!",null);
        }
    }

    public static class MissArgumentException extends Exception{
        public MissArgumentException(String message, Throwable cause){
            super(message,cause);
        }
    }
    public static class InvalidGameException extends Exception{
        public InvalidGameException(String message, Throwable cause){
            super(message, cause);
        }
    }
    public static class PlayerAlreadyJoinedException extends Exception{
        public PlayerAlreadyJoinedException(String message,Throwable cause){
            super(message, cause);
        }
    }
    public static class IncorrectTurnException extends Exception{
        public IncorrectTurnException(String message, Throwable cause){
            super(message, cause);
        }
    }
    public static class InvalidPlayerException extends Exception{
        public InvalidPlayerException(String message,Throwable cause){
            super(message, cause);
        }
    }
}
