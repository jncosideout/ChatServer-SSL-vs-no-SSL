// place this file the path such ends with: ChatServer/server/ChatServer.java

package server_SSL;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import javax.net.ssl.*;


import java.util.ArrayList;
import java.util.List;

public class ChatServer_w_SSL {

    private static final int portNumber = 4444;

    private int serverPort;
    private List<ClientThread_w_SSL> clients; // or "protected static List<ClientThread_w_SSL> clients;"

    public static void main(String[] args){
        ChatServer_w_SSL server = new ChatServer_w_SSL(portNumber);
        server.startServer();
    }

    public ChatServer_w_SSL(int portNumber){
        this.serverPort = portNumber;
    }

    public List<ClientThread_w_SSL> getClients(){
        return clients;
    }

    private void startServer(){
        clients = new ArrayList<ClientThread_w_SSL>();
        SSLServerSocket serverSocket = null;
        try {
            SSLServerSocketFactory sslSrvFact = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
            serverSocket = (SSLServerSocket) sslSrvFact.createServerSocket(serverPort);
            acceptClients(serverSocket);
        } catch (IOException e){
            System.err.println("Could not listen on port: " + serverPort);
            System.exit(1);
        }
    }

    private void acceptClients(SSLServerSocket serverSocket){

        System.out.println("server starts port = " + serverSocket.getLocalSocketAddress());
        while(true){
            try{
                SSLSocket socket = (SSLSocket) serverSocket.accept();
                System.out.println("accepts : " + socket.getRemoteSocketAddress());
                ClientThread_w_SSL client = new ClientThread_w_SSL(this, socket);
                Thread thread = new Thread(client);
                thread.start();
                clients.add(client);
            } catch (IOException ex){
                System.out.println("Accept failed on : "+serverPort);
            }
        }
    }
}
