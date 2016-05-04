package server;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.*;
import java.io.*;
import java.util.ArrayList;

import game.Game;
import game.Player;
import network.Database;

import javax.swing.*;

public class SocketIOHandler{

    public static final int INIT  = 0;          //start of connection
    public static final int LOGIN = 1;          //login or register new account
    public static final int LOBBY = 2;          //lobby with list of games
    public static final int ROOM  = 3;          //pregame and postgame lobby
    public static final int GAME  = 4;          //in-game

    public String output = null;
    private int state;
    private ServerMultiThread _thread;

    private Timer lt;


    public SocketIOHandler(ServerMultiThread thread){
        _thread = thread;
        state = INIT;
    }

    public String processInput(String input){
        System.out.println("Received message:" + input +" from user " + _thread.getPlayer());

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
//                        System.out.println("SET PLAYER TO "+s[1]);
                        ServerInit.allThreads.put(s[1], _thread);
                    }
                    else {
                        output = "ACK_LOGIN,FAILURE,LOGIN";
                    }
                    //s[1] contains username
                    //s[2] contains password (in plaintext for now; may implement encryption later)
                } else
                if(s[0].equals("REGISTER")) {
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
                break;
            case LOBBY:
                if (s[0].equals("GAMES")) {
                    state = LOBBY;
                    output = "ACK_GAMES,SUCCESS:";
                    for (Game g : ServerInit.gameRooms.values()) {
                        output +=  g.getGameName()+","+g.getOwner()+","+g.getPlayers().size()+","+g.getMaxPlayers()+":";
                    }
                    if (ServerInit.gameRooms.size() == 0) {
                        output += ":";
                    }
                    output += "ROOM";
                } else
                //List of all games
                if(s[0].equals("CREATE")) {
                    //Current user does not have a game, assumes all input is valid
                    if (_thread.getGame() == null) {
                        int num = Integer.parseInt(s[2]);
                        Game g = new Game(s[1], _thread.getPlayer(), num, s[3]);
                        ServerInit.gameRooms.put(s[1], g);
                        output = "ACK_CREATE,SUCCESS," + s[1] + ",ROOM";
                        //state = ROOM;
                        state = LOBBY;
                    }
                    else {
                        output = "ACK_CREATE,FAILURE,GAME";
                        state = LOBBY;
                    }
                    //s[1] contains room/game name
                    //s[2] contains max_players - default = 4; max = 30 (so whole class can play at once)
                    //s[3] contains password (optional)
                } else
                if (s[0].equals("JOIN")) {
//                    System.out.println("RECIEVED JOIN to " + s[1]);
                    Game g = ServerInit.gameRooms.get(s[1]);
//                    System.out.println("GET PLAYER TO "+_thread.getPlayer());
                    g.addPlayer(_thread.getPlayer(), _thread);
                    _thread.setGame(g);
                    output = "ACK_JOIN,SUCCESS,GAME";
                    state = GAME;
                }
                break;
            case ROOM:
                //Waiting screen for game

                break;
            case GAME:
                Game g = _thread.getGame();
                if (s[0].equals("GAME_START")) {

                    output = "ACK_START," + g.getGameName() + ",PLAYERS,";
                    for (Player p : g.getPlayers()) {
                        output += p.getName() + ",";
                    }
                    output += "CARDS,";
//                    System.out.println(g.getBoard());
                    for (game.Card c : g.getBoard()) {
                        output += c.toString() + ",";
                    }
                    output += "GAME";
                } else if (s[0].equals("UPDATE")) {
                    output = "ACK_UPDATE,UPDATE,GAME";
                } else if (s[0].equals("LOCK")) {
                    g.lock(_thread.getPlayer());
                    output = "LOCK,"+_thread.getPlayer()+",GAME";
                    lt = new Timer(Game.LOCKTIME, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            System.out.println("User " +_thread.getPlayer() +" timed out.  Sending failure");
                            sendAll("ACK_REPLACE,FAILURE,"+_thread.getPlayer()+","+",GAME");
                            g.lock(null);
                        }
                    });
                    lt.setRepeats(false);
                    lt.start();
                    this.sendAll(output);
                } else if (s[0].equals("REPLACE")) {
                    lt.stop();
                    int success = g.replace(s[1], s[2], s[3], this._thread.getPlayer());
                    if (success == 0) {
                        output = "ACK_REPLACE,SUCCESS," + _thread.getPlayer()+",";
                        for (game.Card c : g.getBoard()) {
                            output += c.toString() + ",";
                        }
                    } else {
                        output = "ACK_REPLACE,FAILURE,"+this._thread.getPlayer()+",";
                    }
                    output += "GAME";
                    this.sendAll(output);
                }
                break;

            default:
                System.out.println("UNKNOWN STATE!!");
                break;
        }
        if (output.isEmpty()) {
            output = "BAD_VALUE,"+input+","+state;    //invalid data received from
        }
        System.out.println("responding to user " + _thread.getPlayer() + " with message: " + output);
        return output;
    }

    private void sendAll(String output) {
        System.out.println("sending to all other users " + output);
        for (ServerMultiThread th : _thread.getGame().threads) {
            if (th != _thread) {
                th.outStream.println(output);
            } else {
                System.out.println("OTHER");
            }
        }
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