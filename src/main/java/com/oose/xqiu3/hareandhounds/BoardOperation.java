package com.oose.xqiu3.hareandhounds;

/**
 * Created by xqiu3 on 17/9/7.
 */
public class BoardOperation{

    private BoardMap map;

    public BoardOperation(){
        map = new BoardMap();
    }

    public Board moveHound(Board originalBoard, int originalX, int originalY, int afterX,int afterY) throws GameIllegalMoveException{
        Board newBoard = new Board(originalBoard);
        int[][] boardArray = newBoard.getBoard();
        if(boardArray[originalX][originalY] == 0 || boardArray[originalX][originalY] == 2){
            throw new GameIllegalMoveException("This is not a valid hound position",null);
        }
        if(boardArray[afterX][afterY] != 0){
            throw new GameIllegalMoveException("The position you want to go is not a valid position",null);
        }
        //check if hound go left
        if(afterX < originalX) {
            throw new GameIllegalMoveException("The Hound cannot go left!",null);
        }

        //check if these 2 positions is connected.
        Boolean isConnected = map.isConnected(originalX,originalY,afterX,afterY);
        if(isConnected){
            newBoard.setBoard(originalX,originalY,afterX,afterY,1);
        }else{
            throw new GameIllegalMoveException("You can only move one step!",null);
        }

        return newBoard;
    }

    public Board moveHare(Board originalBoard, int originalX, int originalY,
                          int afterX,int afterY) throws GameIllegalMoveException{
        Board newBoard = new Board(originalBoard);
        int[][] boardArray = originalBoard.getBoard();
        if(boardArray[originalX][originalY] != 2){
            throw new GameIllegalMoveException("this is not a valid hare position!",null);
        }
        if(boardArray[afterX][afterY] != 0){
            throw new GameIllegalMoveException("this is not a valid hare position!",null);
        }
        //check if connected
        Boolean isConnected = map.isConnected(originalX,originalY,afterX,afterY);
        if(isConnected){
            newBoard.setBoard(originalX,originalY,afterX,afterY,2);
        }else{
            throw new GameIllegalMoveException("you can only move one step!",null);
        }
        return newBoard;
    }

    public Boolean isHoundWin(Board board){
        //If hound wins, it can only trap the hare
        //there are only one way to trap the hare
        int[][] boardArray = board.getBoard();
        return (boardArray[3][0] == 1 && boardArray[3][1] == 1 && boardArray[3][2] == 1 && boardArray[4][1] == 2);
    }

    public Boolean isHareWinByEscape(Board board){
        //If hare wins by escape, there is no hound to the left of hare
        //first find where the hare is
        int[][] boardArray = board.getBoard();
        int hareX = -1;
        for(int i = 0; i < 5;i++){
            for(int j = 0;j<3;j++){
                if(boardArray[i][j] == 2){
                    hareX = i;
                    break;
                }
            }
            if(hareX != -1){
                break;
            }
        }
        int countHound = 0;
        for(int i = 0;i<hareX;i++){
            for(int j = 0;j < 3;j++){
                countHound += boardArray[i][j];
            }
        }

        if (countHound > 0) {
            return false;
        } else return true;
    }
    public Boolean isHareWinByHoundStall(BoardRecord record){
        return BoardRecord.isStall(record);
    }

    public static class GameIllegalMoveException extends Exception {
        public GameIllegalMoveException(String message, Throwable cause){
            super(message,cause);
        }
    }
}
