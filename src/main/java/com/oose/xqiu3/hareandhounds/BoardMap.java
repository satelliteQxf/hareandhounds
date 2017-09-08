package com.oose.xqiu3.hareandhounds;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xqiu3 on 17/9/7.
 * A tool to check if two points are connected
 */
public class BoardMap {
    public class Coordinate{
        public final int X;
        public final int Y;
        public Coordinate(int x, int y){
            this.X = x;
            this.Y = y;
        }
    }
    public class Edge{
        public Coordinate start;
        public Coordinate end;
        public Edge(Coordinate S, Coordinate V){
            this.start = S;
            this.end = V;
        }
    }
    private Coordinate v1 = new Coordinate(0,1);
    private Coordinate v2 = new Coordinate(1,0);
    private Coordinate v3 = new Coordinate(1,1);
    private Coordinate v4 = new Coordinate(1,2);
    private Coordinate v5 = new Coordinate(2,0);
    private Coordinate v6 = new Coordinate(2,1);
    private Coordinate v7 = new Coordinate(2,2);
    private Coordinate v8 = new Coordinate(3,0);
    private Coordinate v9 = new Coordinate(3,1);
    private Coordinate v10 = new Coordinate(3,2);
    private Coordinate v11 = new Coordinate(4,1);

    private List<Edge> boardEdge = new ArrayList<Edge>();
    public BoardMap(){
        boardEdge.add(new Edge(v1,v2));
        boardEdge.add(new Edge(v1,v3));
        boardEdge.add(new Edge(v1,v4));
        boardEdge.add(new Edge(v2,v5));
        boardEdge.add(new Edge(v2,v6));
        boardEdge.add(new Edge(v2,v3));
        boardEdge.add(new Edge(v3,v6));
        boardEdge.add(new Edge(v3,v4));
        boardEdge.add(new Edge(v4,v6));
        boardEdge.add(new Edge(v4,v7));
        boardEdge.add(new Edge(v5,v6));
        boardEdge.add(new Edge(v5,v8));
        boardEdge.add(new Edge(v6,v7));
        boardEdge.add(new Edge(v6,v8));
        boardEdge.add(new Edge(v6,v9));
        boardEdge.add(new Edge(v6,v10));
        boardEdge.add(new Edge(v7,v8));
        boardEdge.add(new Edge(v8,v9));
        boardEdge.add(new Edge(v8,v10));
        boardEdge.add(new Edge(v9,v10));
        boardEdge.add(new Edge(v9,v11));
        boardEdge.add(new Edge(v10,v11));
    }
    public Boolean isConnected(int fromX,int fromY,int toX,int toY){
        Boolean returnValue = false;
        for(int i = 0; i<boardEdge.size();i++){
            Edge currentEdge = boardEdge.get(i);
            if(currentEdge.start.X == fromX && currentEdge.start.Y == fromY
                    && currentEdge.end.X == toX && currentEdge.end.Y == toY){
                returnValue = true;
                break;
            }else if(currentEdge.start.X == toX && currentEdge.start.Y == toY
                    && currentEdge.end.X == fromX && currentEdge.end.Y == fromY){
                returnValue = true;//since its a undirected graph
                break;
            }else{
                continue;
            }
        }
        return returnValue;
    }

}
