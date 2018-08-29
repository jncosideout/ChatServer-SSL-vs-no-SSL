// place this file the path such ends with: ChatServer_w_SSL/server/ClientThread.java

package server_SSL;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import javax.net.ssl.*;


import java.util.Scanner;

public class ClientThread_w_SSL implements Runnable {
    private SSLSocket socket;
    private PrintWriter clientOut;
    private ChatServer_w_SSL server;

    public ClientThread_w_SSL(ChatServer_w_SSL server, SSLSocket socket){
        this.server = server;
        this.socket = socket;
    }

    private PrintWriter getWriter(){
        return clientOut;
    }

    @Override
    public void run() {
        try{
            // setup
            this.clientOut = new PrintWriter(socket.getOutputStream(), false);
            Scanner in = new Scanner(socket.getInputStream());
            
            //setup handshake
			socket.setEnabledCipherSuites(socket.getSupportedCipherSuites());
			socket.startHandshake();
            // start communicating
            while(!socket.isClosed()){
                if(in.hasNextLine()){
                    String input = in.nextLine();
                    // NOTE: if you want to check server can read input, uncomment next line and check server file console.
                     System.out.println(input);
                    for(ClientThread_w_SSL thatClient : server.getClients()){
                        PrintWriter thatClientOut = thatClient.getWriter();
                        if(thatClientOut != null){
                            thatClientOut.write(input + "\r\n");
                            thatClientOut.flush();
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception a) {
        	a.printStackTrace();
        }
    }
}
