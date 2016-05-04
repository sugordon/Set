package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by vkuruturi on 4/9/16.
 */
public class ClientInit {

    public static final int INIT  = 0;          //start of connection
    public static final int LOGIN = 1;          //login or register new account
    public static final int LOBBY = 2;          //lobby with list of games
    public static final int ROOM  = 3;          //pregame and postgame lobby
    public static final int GAME  = 4;          //in-game

    public static int STATE = 0;

    //private static String HOST = "sable06.ee.cooper.edu";
    private static String HOST = "localhost";

    private static int PORT = 7100;
    public static Socket sck;

    public static BufferedReader outStream;
    public static PrintWriter inStream;

    public static String response = null;

    public static GUILogin login;
    public static GUILobby lobby;
    public static GUIGame  game;


    public static void initConn(){
        try{
            sck = new Socket(HOST,PORT);
        }
        catch (UnknownHostException e){
            System.err.println("Cannot resolve host " + HOST);
            System.exit(1);
        }
        catch (IOException e){
            e.printStackTrace();
           /* try {
                inStream = new PrintWriter(sck.getOutputStream(),true);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            try {
                outStream = new BufferedReader(new InputStreamReader(sck.getInputStream()));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            */
        }

        try {
            inStream = new PrintWriter(sck.getOutputStream(),true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            outStream = new BufferedReader(new InputStreamReader(sck.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            while((response = outStream.readLine()) != null) {
                if (response.equals("INIT_CONN")) {
                    inStream.println("INIT");
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try{
            while((response = outStream.readLine())!= null){
                if(response.equals("ACK_CONN,LOGIN")) {
                    STATE = LOGIN;
                    break;
                }
            }
        } catch (IOException e){
            e.printStackTrace();
        }

    }
    public static void startGUI(){
        login = new GUILogin();
        login.createAndShowLogin();
        lobby = new GUILobby();
        game = new GUIGame(4, "LOL");
    }

    public static void switchStates(int current, int next){
        STATE = next;
        switch(next){
            case LOGIN:
                login.setVisible(true);
                break;
            case LOBBY:
                lobby.createLobby("ALPACAS");
                lobby.resetTheOneTruePanel();
                lobby.lobby.setVisible(true);
                break;
            case ROOM:
                game.setVisible(true);
                break;
            case GAME:
                System.out.println("SWITCH TO GAME");
                game.createAndShowBoard();
                game.setVisible(true);
        }

        switch(current){
            case LOGIN:
                login.loginscreen.dispose();
                break;
            case LOBBY:
                lobby.setVisible(false);
                lobby.lobby.dispose();
                break;
            case ROOM:
                game.setVisible(false);
                break;
            case GAME:
                game.setVisible(false);
                game.requestFocus();
//                game.gameboard.dispose();
                break;
        }


    }

    static class ListenFromServer extends Thread {
        String msg;
        public void run() {
            while(true) {
                try {
                    while ((msg = (String) ClientInit.outStream.readLine()) == null );
                }
                catch(IOException e) {
                }

                processInput(msg);
            }
        }

        private void processInput(String msg){
            if(STATE == LOGIN){
                login.processResponse(msg);
            }
            else if (STATE == LOBBY)
                lobby.processResponse(msg);
            else if (STATE == GAME)
                game.processResponse(msg);
            else if (STATE == ROOM);

        }
    }

    public static void main(String[] args) {
        initConn();
        new ListenFromServer().start();
        startGUI();
    }

}