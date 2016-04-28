package server;

import java.net.*;
import java.io.*;

import game.Game;
import game.Player;
import network.Database;

public class SocketIOHandler{

    public static final int INIT  = 0;          //start of connection
    public static final int LOGIN = 1;          //login or register new account
    public static final int LOBBY = 2;          //lobby with list of games
    public static final int ROOM  = 3;          //pregame and postgame lobby
    public static final int GAME  = 4;          //in-game

    public String output = null;
    private int state;
    private ServerMultiThread _thread;


    public SocketIOHandler(ServerMultiThread thread){
        _thread = thread;
        state = INIT;
    }

    public String processInput(String input){
        String [] s = splitString(input);
        String output = "";
        switch (state){
            case INIT:
                state = LOGIN;
                output = "ACK_CONN,LOGIN";
                break;
            case LOGIN:
                if(s[0].equals("LOGIN")){
                    if(Database.auth(s[1],s[2])) {
                        output = "ACK_LOGIN,SUCCESS,LOBBY";
                        state = LOBBY;
                        _thread.setPlayer(s[1]);
                        ServerInit.allThreads.put(s[1], _thread);
                    }
                    else {
                        output = "ACK_LOGIN,FAILURE,LOGIN";
                    }
                    //s[1] contains username
                    //s[2] contains password (in plaintext for now; may implement encryption later)
                }
                else if(s[0].equals("REGISTER")) {
                    state = LOGIN;
                    System.out.println("Register request");
                    switch (Database.newUser(s[1], s[2])) {
                        case 0:
                            output = "ACK_REGISTER,SUCCESS,LOGIN";
                            break;
                        case 1:
                            output = "ACK_REGISTER,FAILURE,LOGIN";
                            break;
                        case 2:
                            output = "ACK_REGISTER,EXISTS,LOGIN";
                            break;
                    }
                }
                else output = "BAD_VALUE,LOGIN";    //invalid data received from
                break;
            case LOBBY:
                if (s[0].equals("GAMES")) {
                    output = "ACK_GAMES,SUCCESS,";
                    for (Game g : ServerInit.gameRooms.values()) {
                        output +=  g.toString();
                    }
                    output += "ROOM";
                }
                //List of all games
                if(s[0].equals("CREATE")) {
                    //Current user does not have a game, assumes all input is valid
                    if (_thread.getGame() == null) {
                        int num = Integer.parseInt(s[2]);
                        Game g = new Game(s[1], _thread.getPlayer(), num, s[3]);
                        _thread.setGame(g);
                        ServerInit.gameRooms.put(s[1], g);
                        output = "ACK_CREATE,SUCCESS," + s[1] + ",ROOM";
                        state = ROOM;
                    }
                    else {
                        output = "ACK_CREATE,FAILURE,GAME";
                        state = LOBBY;
                    }
                    //s[1] contains room/game name
                    //s[2] contains max_players - default = 4; max = 30 (so whole class can play at once)
                    //s[3] contains password (optional)
                }
                break;
            case ROOM:
                //Waiting screen for game

                break;
            case GAME:
                Game g = _thread.getGame();
                if (s[0].equals("GAME_START")) {
                    output = "ACK_START,"+g.getGameName()+","+g.getPlayers()+","+g.getBoard()+",GAME";
                }
                if (s[0].equals("UPDATE")) {
                    output = "ACK_UPDATE,UPDATE,GAME";
                }
                //s[1] contains the player number of the lock
                if (s[0].equals("LOCK")) {
                    g.lock(Integer.parseInt(s[1]));
                    output = "ACK_LOCK,"+g.getLock()+",GAME";
                }
                //s[1], s[2], s[3] contains the three cards, s[4] contains the player number
                if (s[0].equals("REPLACE")) {
                    boolean success = g.replace(s[1], s[2], s[3], Integer.parseInt(s[4]));
                    output = "ACK_REPLACE,"+success+","+g.getBoard()+",GAME";
                }
                break;

            default:
                System.out.println("UNKNOWN STATE!!");
                break;
        }
        return output;
    }

    public int getState(){
        return state;
    }
    public void setState(int newState){
        state = newState;
    }

    private String [] splitString(String s){
        return s.split(",");
    }
}