package cn.forwode.tunnel;

import java.io.IOException;
import java.net.Socket;

public class SocketConnector {

    public static void connect(Socket userSocket, ClientDataSocket clientDataSocket, Client client) throws IOException {
        
        Thread readFromUserThread = new ReadFromUserThread(userSocket, clientDataSocket, client);
        Thread writeToUserThread = new WriteToUserThread(userSocket, clientDataSocket, client);
        readFromUserThread.start();
        writeToUserThread.start();
    }
}