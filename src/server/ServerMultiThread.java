package server;

import java.net.*;
import java.io.*;
import game.*;

public class ServerMultiThread extends Thread {
    private Socket socket;
    public PrintWriter outStream;
    public BufferedReader inStream;
    private String current_player;
    private Game current_game;

    private SocketIOHandler io;

    public ServerMultiThread(Socket s){
        super("ServerMultiThread");
        socket = s;
        io = new SocketIOHandler(this);
    }

    public void setPlayer(String p) {
        this.current_player = p;
    }

    public void setGame(Game g) {
        this.current_game = g;
    }

    public Game getGame() {
        return current_game;
    }

    public String getPlayer() {
        return this.current_player;
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
                if (out.equals("END_CONN")) {
                    this.close();
                    break;
                }
                outStream.println(out);
            }
        }
        catch(IOException e){
            System.out.println("IO exception!");
            this.close();
            e.printStackTrace();
        }
    }

    private void close() {
        ServerInit.allThreads.remove(this.current_player);
        if (this.current_game.removePlayer(this.current_player) == false)
            System.out.println("ERROR REMOVING PLAYER");
        outStream.close();
        try {
            inStream.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}