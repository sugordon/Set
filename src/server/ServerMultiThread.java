package server;

import java.net.*;
import java.io.*;
import game.*;

public class ServerMultiThread extends Thread {
    private Socket socket;
    public PrintWriter outStream;
    public BufferedReader inStream;
    public Player current_player;
    public Game current_game;

    private SocketIOHandler io;

    public ServerMultiThread(Socket s){
        super("ServerMultiThread");
        socket = s;
        io = new SocketIOHandler(this);
    }

    public void run(){
        try {
            outStream = new PrintWriter(socket.getOutputStream(), true);
            inStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String out, in;
            out = "INIT_CONN";
            outStream.println(out);

            while ((in = inStream.readLine()) != null) {
                out = io.processInput(in);
                if (out.equals("END_CONN"))
                    break;
                outStream.println(out);
            }
            outStream.close();
            inStream.close();
            socket.close();
        }
        catch(IOException e){
            System.out.println("IO exception!");
            e.printStackTrace();
        }
    }
}