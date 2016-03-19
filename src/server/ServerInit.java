package server;

import java.net.*;
import java.io.*;
import java.util.concurrent.ConcurrentHashMap;
import game.*;

public class ServerInit{

    ServerSocket server_socket = null;
    boolean listening;
    int PORT = 7100;

    public ServerInit(){
        ConcurrentHashMap<String, ServerMultiThread> allThreads = new ConcurrentHashMap<String, ServerMultiThread>();
        ConcurrentHashMap<String, Game> gameRooms = new ConcurrentHashMap<String, Game>();

        try {
            server_socket = new ServerSocket(PORT);
        }
        catch (IOException e) {
            System.out.println("Could not start listening on port " + PORT + ".");
            e.printStackTrace();
            System.exit(-1);
        }
        while(listening){
            try{
                new ServerMultiThread(server_socket.accept()).start();
            }
            catch(IOException e){
                System.out.println("Encountered exception when attempting to accept incoming connection");
                e.printStackTrace();
            }
        }
        try {
            server_socket.close();
        }
        catch (IOException e){
            System.out.println("Encountered exception when attempting to close server socket");
            e.printStackTrace();
            System.exit(-1);
        }

    }

    public static void main(String[] args){
        ServerInit server = new ServerInit();
        System.exit(0);
    }
}