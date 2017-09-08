package com.oose.xqiu3.hareandhounds;

/**
 * Created by xqiu3 on 17/9/7.
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static spark.Spark.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class Bootstrap {
    public static final String IP_ADDRESS = "localhost";
    public static final int PORT = 8080;
    private static final Logger logger = LoggerFactory.getLogger(Bootstrap.class);

    public static void main(String[] args) throws Exception{
        //Specify the IP address and Port at which the server should be run
        ipAddress(IP_ADDRESS);
        port(PORT);

        //Specify the sub-directory from which to serve static resources (like html and css)
        staticFileLocation("/public");

        //now to start our web service like this:
        //get("/", (request, response) -> "Hello World");
        GameService model = new GameService();
        new GameController(model);
    }

}
