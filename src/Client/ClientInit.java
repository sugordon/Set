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

    private static String HOST = "sable06.ee.cooper.edu";
    private static int PORT = 7100;
    private static Socket sck;

    public static int STATE;
    public static BufferedReader outStream;
    public static PrintWriter inStream;

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
            try {
                inStream = new PrintWriter(sck.getOutputStream(),true);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            try {
                outStream = new BufferedReader(new InputStreamReader(sck.getInputStream()));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

    }
    public static void startGUI(){
        GUILogin login = new GUILogin();
        login.createAndShowLogin();
    }

    public static void main(String[] args) {
        startGUI();
        initConn();
    }

}