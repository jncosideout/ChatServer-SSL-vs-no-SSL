// place this file the path such ends with: ChatServer/client/Client.java

package client_SSL;

import java.io.IOException;
import java.net.Socket;
import javax.net.ssl.*;


import java.util.Scanner;

public class Client_with_SSL {

    private static final String host = "localhost";
    private static final int portNumber = 4444;

    private String userName;
    private String serverHost;
    private int serverPort;
    private Scanner userInputScanner;

    public static void main(String[] args){
        String readName = null;
        Scanner scan = new Scanner(System.in);
        System.out.println("Please input username:");
        while(readName == null || readName.trim().equals("")){
            // null, empty, whitespace(s) not allowed.
            readName = scan.nextLine();
            if(readName.trim().equals("")){
                System.out.println("Invalid. Please enter again:");
            }
        }

        Client_with_SSL client = new Client_with_SSL(readName, host, portNumber);
        client.startClient(scan);
    }

    private Client_with_SSL(String userName, String host, int portNumber){
        this.userName = userName;
        this.serverHost = host;
        this.serverPort = portNumber;
    }

    private void startClient(Scanner scan){
        try{
        	SSLSocketFactory sslFact = (SSLSocketFactory) SSLSocketFactory.getDefault();
            SSLSocket socket = (SSLSocket) sslFact.createSocket(serverHost, serverPort);
            //socket.startHandshake();
            Thread.sleep(1000); // waiting for network communicating.

            ServerThread_w_SSL serverThread = new ServerThread_w_SSL(socket, userName);
            Thread serverAccessThread = new Thread(serverThread);
            serverAccessThread.start();
            while(serverAccessThread.isAlive()){
                if(scan.hasNextLine()){
                    serverThread.addNextMessage(scan.nextLine());
                }
                // NOTE: scan.hasNextLine waits input (in the other words block this thread's process).
                // NOTE: If you use buffered reader or something else not waiting way,
                // NOTE: I recommends write waiting short time like following.
                // else {
                //    Thread.sleep(200);
                // }
            }
        }catch(IOException ex){
            System.err.println("Fatal Connection error!");
            ex.printStackTrace();
        }catch(InterruptedException ex){
            System.out.println("Interrupted");
        } catch (Exception all) {
        	all.printStackTrace();
        }
    }
}
