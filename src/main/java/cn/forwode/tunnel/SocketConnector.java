package cn.forwode.tunnel;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketConnector {

    public static void connect(Socket userSocket, ClientDataSocket clientDataSocket, ServerSocket server) throws IOException {
        
        Thread readFromUserThread = new ReadFromUserThread(userSocket, clientDataSocket);
        Thread writeToUserThread = new WriteToUserThread(userSocket, clientDataSocket);
        readFromUserThread.start();
        writeToUserThread.start();
    }
}