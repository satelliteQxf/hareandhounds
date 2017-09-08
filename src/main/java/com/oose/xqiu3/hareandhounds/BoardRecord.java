package com.oose.xqiu3.hareandhounds;

import com.sun.org.apache.xpath.internal.operations.Bool;
import sun.applet.resources.MsgAppletViewer;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by xqiu3 on 17/9/7.
 * record every move, and provide tool for judging hound stall
 */
public class BoardRecord {
    private Map<Board,Integer> record ;
    private Board latestBoard;
    public BoardRecord(){
        record = new HashMap<Board,Integer>();
        latestBoard = new Board();
    }

    public Map<Board, Integer> getRecord() {
        return record;
    }

    public void setRecord(Board board) {
        Iterator iter = getRecord().entrySet().iterator();
        Boolean isInserted = false;
        while(iter.hasNext()){
            Map.Entry entry = (Map.Entry)iter.next();
            Board currBoard = (Board)entry.getKey();
            if(board.isEqual(currBoard)){
                getRecord().put(currBoard,(Integer)entry.getValue()+1);
                isInserted = true;
                break;
            }
        }
        if(!isInserted){
            getRecord().put(board,1);
        }
        latestBoard = new Board(board);
    }

    public static Boolean isStall(BoardRecord record){
        Iterator iter = record.getRecord().entrySet().iterator();
        Boolean returnValue = false;
        while(iter.hasNext()){
            Map.Entry entry = (Map.Entry)iter.next();
            int count = (Integer)entry.getValue();
            if(count >= 3){
                returnValue = true;
            }
        }
        return returnValue;
    }
    public Board getLatestBoard(){
        return latestBoard;
    }
}
