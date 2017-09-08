package com.oose.xqiu3.hareandhounds;

/**
 * Created by xqiu3 on 17/9/7.
 **/

import com.oose.xqiu3.hareandhounds.pojo.FailReasonResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;

import static spark.Spark.*;

public class GameController {
    private static final String API_CONTEXT = "/hareandhounds/api/games";
    private final GameService gameService;
    private final Logger logger = LoggerFactory.getLogger(GameController.class);

    public GameController(GameService gameService){
        this.gameService = gameService;
        setupEndpoints();
    }

    private void setupEndpoints(){
        post(API_CONTEXT,"application/json",(request, response) -> {
            try{
                response.status(201);
                return gameService.startGame(request.body());
            }catch (GameService.MissArgumentException ex){
                logger.error("wrong argument!");
                response.status(400);
            }
            return Collections.EMPTY_MAP;
        },new JsonTransformer());

        get(API_CONTEXT+"/:id/state","application/json",(request, response) -> {
            try{
                return gameService.describeState(request.params(":id"));
            }catch (GameService.InvalidGameException ex){
                logger.error("invalid game id!");
                response.status(404);
            }
            return Collections.EMPTY_MAP;

        },new JsonTransformer());

        get(API_CONTEXT+"/:id/board","application/json",(request, response) -> {
            try{
                return gameService.describeBoard(request.params(":id"));
            }catch(GameService.InvalidGameException ex){
                logger.error("invalid game id!");
                response.status(404);
            }
            return Collections.EMPTY_MAP;
        },new JsonTransformer());

        put(API_CONTEXT+"/:id","application/json",(request, response) -> {
            try{
                return gameService.joinGame(request.params(":id"));
            }catch(GameService.InvalidGameException ex){
                logger.error("invalid game id!");
                response.status(404);
            }catch(GameService.PlayerAlreadyJoinedException ex){
                logger.error("Second player already joined");
                response.status(410);
            }
            return Collections.EMPTY_MAP;
        },new JsonTransformer());

        post(API_CONTEXT+"/:id/turns","application/json",(request, response) -> {
            try{
                gameService.movePiece(request.params(":id"),request.body());
            }catch(GameService.InvalidGameException ex){
                logger.error("invalid game id!");
                response.status(404);
                FailReasonResponse res = new FailReasonResponse();
                res.reason = "INVALID_GAME_ID";
                return res;
            }catch(GameService.InvalidPlayerException ex){
                logger.error("invalid player id!");
                response.status(404);
                FailReasonResponse res = new FailReasonResponse();
                res.reason = "INVALID_PLAYER_ID";
                return res;
            }catch(GameService.IncorrectTurnException ex){
                logger.error("incorrect turn!");
                response.status(422);
                FailReasonResponse res = new FailReasonResponse();
                res.reason = "INCORRECT_TURN";
                return res;
            }catch(BoardOperation.GameIllegalMoveException ex){
                logger.error("illegal move!"+ex.getMessage());
                response.status(422);
                FailReasonResponse res = new FailReasonResponse();
                res.reason = "ILLEGAL_MOVE";
                return res;
            }
            return Collections.EMPTY_MAP;
        },new JsonTransformer());
    }
}
