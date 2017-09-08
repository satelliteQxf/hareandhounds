package com.oose.xqiu3.hareandhounds;

/**
 * Created by xqiu3 on 17/9/7.
 * let 0 denote the empty position
 * let 1 denote the hound
 * let 2 denote the hare
 */
public class Board {
    private int[][] board = null;

    public Board(){
        board = new int[5][3];//note that board[0][0], board[0][2],board[4][0],board[4][2] can't be into use
        initialize();
    }

    public Board(Board board){
        this.board = new int[5][3];
        int[][] array = board.getBoard();
        for(int i = 0;i<5;i++){
            for(int j = 0;j<3;j++){
                this.board[i][j] = array[i][j];
            }
        }
    }

    public void initialize(){
        //put three hound and 1 hare on the board
        board[0][1] = 1;
        board[1][0] = 1;
        board[1][2] = 1;
        board[4][1] = 2;
    }

    //since there are 4 illegal blanks
    public Boolean isLegal(){
        return (board[0][0] == 0 ||board[0][2] == 0||board[4][0] == 0||board[4][2] == 0);
    }

    //getter
    public int[][] getBoard() {
        return board;
    }
    //setter
    public void setBoard(int fromX,int fromY, int nowX,int nowY, int hareOrHound) {
        this.board[fromX][fromY] = 0;
        this.board[nowX][nowY] = hareOrHound;
    }


    //compare two board
    public Boolean isEqual(Board comparedBoard){
        Boolean returnValue = true;
        for (int i = 0; i < 5; i++){
            for (int j = 0; j < 3; j++){
                if(this.board[i][j] != comparedBoard.getBoard()[i][j]){
                    returnValue = false;
                    break;
                }
            }
            if(!returnValue)
                break;
        }
        return returnValue;
    }

}
