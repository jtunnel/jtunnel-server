package cn.forwode.tunnel;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerClientSocket{

    private ServerSocket serverSocket;
    private Socket controlSocket;
    private List<ClientDataSocket> clientDataSockets = new ArrayList<ClientDataSocket>();

    public ServerClientSocket() {
	}

    public ServerClientSocket(ServerSocket serverSocket){
        this.serverSocket = serverSocket;
    }


	public Socket getControlSocket(){
        return controlSocket;
    }
    
    public void setControlSocket(Socket controlSocket){
        this.controlSocket = controlSocket;
    }

    public List<ClientDataSocket> getClientDataSockets(){
        return clientDataSockets;
    }

    public ServerSocket getServerSocket(){
        return serverSocket;
    }

    public void setServerSocket(ServerSocket serverSocket){
        this.serverSocket = serverSocket;
    }
}