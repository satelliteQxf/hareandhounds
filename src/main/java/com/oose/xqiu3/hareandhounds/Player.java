package com.oose.xqiu3.hareandhounds;

/**
 * Created by xqiu3 on 17/9/7.
 * To simplify this question, since playerId only be unique within the context of a singer game, so player_id of hound
 * will be 1, and player_id of hare would be 2.
 * if value of any becomes 2, it's its turn.
 */
public class Player {
    private int hound;
    private int hare;

    public Player(){
        hound = -1;
        hare = -1;

    }

    public int getHound() {
        return hound;
    }

    public void setHound(int hound) {
        this.hound = hound;
    }

    public int getHare() {
        return hare;
    }

    public void setHare(int hare) {
        this.hare = hare;
    }
    public Boolean isSetHare(){
        return (getHare() >= 1)?true:false;
    }
    public Boolean isSetHound(){
        return (getHound() >= 1)?true:false;
    }
}
