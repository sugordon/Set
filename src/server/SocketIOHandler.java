package server;

import java.net.*;
import java.io.*;

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
                    //s[1] contains username
                    //s[2] contains password (in plaintext for now; may implement encryption later)
                    //TODO authenticate user (confirm username exists in db, passwords match, and not already logged in)
                    //TODO add to active thread hashmap with username as key
                    //TODO send login confirmation to client e.g. "ACK_LOGIN,SUCCESS,LOBBY" or "ACK_LOGIN,FAILURE,LOGIN"
                    //TODO change state of thread to LOBBY
                }
                else if(s[0].equals("REGISTER")){
                    //TODO confirm username doesnt exist
                    //TODO add username to db if it doesnt exist
                    //TODO send registration confirmation to client e.g. "ACK_REGISTER,SUCCESS,LOGIN" or "ACK_REGISTER,FAILURE,LOGIN"
                    //TODO do NOT change state of thread.  User must log in again
                }
                else output = "BAD_VALUE";    //invalid data received from
                break;
            case LOBBY:
                if(s[0].equals("CREATE")){
                    //s[1] contains room/game name
                    //s[2] contains max_players - default = 4; max = 30 (so whole class can play at once)
                    //s[3] contains password (optional)
                    //TODO confirm that user does not have an existing game
                    //TODO if user already has an active game:
                        // notify client that game already exists e.g. "ACK_CREATE,FAILURE,GAME_EXISTS,LOBBY"
                    //TODO if user does not have an active game:
                        // attempt to create game
                        // if successful, send confirmation and (maybe) room/game id, e.g. "ACK_CREATE,SUCCESS,game1,ROOM"
                            // change state of thread to ROOM
                        // if unsuccessful, send confirmation and failure notice e.g. "ACK_CREATE,FAILURE,BAD_NAME,LOBBY"
                            // leave state of thread as LOBBY
                }
                break;
            case ROOM:

                break;
            case GAME:

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