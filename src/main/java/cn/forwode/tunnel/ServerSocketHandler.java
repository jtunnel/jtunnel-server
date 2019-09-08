package cn.forwode.tunnel;

import java.io.IOException;
import java.io.PrintStream;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerSocketHandler implements IServerSocketHandler {
    
    final Logger logger = LoggerFactory.getLogger(getClass());
    
    public void onRequestRemoteForward(Socket clientSocket, Client client, String port) {
        ServerSocket serverSocket = null;
        PrintStream controlPrintStream;
        try {
            controlPrintStream = new PrintStream(clientSocket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        ClientDataSocket clientDataSocket = new ClientDataSocket(clientSocket);
        client.addPortClientDataSocket(port, clientDataSocket);
        try {
            serverSocket = new ServerSocket(Integer.parseInt(port));
            client.setPortServerSocket(port, serverSocket);
            new PortServerThread(port, client).start();
            controlPrintStream.println("200");
            // server.setSoTimeout(3000);
        } catch (NumberFormatException e2) {
            logger.error("NumberFormatException", e2);
            controlPrintStream.println("400");
        } catch (BindException e2) {
            logger.error("new data socket, port has bind......." + port);
            controlPrintStream.println("201");
        } catch (Exception e2){
            logger.error("error=", e2);
            controlPrintStream.println("500");
        }
    }

    public void onRequestLocalForward(Socket clientSocket) {

    }

    public void onRequestDynamicForward(Socket clientSocket) {
		
	}

    public void onRequestControl(Socket clientSocket, Client client) {
        PrintStream controlPrintStream = null;
        try {
            controlPrintStream = new PrintStream(clientSocket.getOutputStream());
            client.setControlSocket(clientSocket);
            ClientManager.getInstance().add(client);
            controlPrintStream.println("200");
            new ControlThread(client, clientSocket).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}