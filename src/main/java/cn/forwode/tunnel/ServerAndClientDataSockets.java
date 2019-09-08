package cn.forwode.tunnel;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class ServerAndClientDataSockets{

    private ServerSocket serverSocket;
    private List<ClientDataSocket> clientDataSockets;

    public ServerAndClientDataSockets() {
        clientDataSockets = new ArrayList<ClientDataSocket>();
	}

    public List<ClientDataSocket> getClientDataSockets(){
        return clientDataSockets;
    }

    public void add(ClientDataSocket dataSocket){
        clientDataSockets.add(dataSocket);
    }

    public void remove(ClientDataSocket dataSocket) {
        try {
            dataSocket.getSocket().close();
        } catch (IOException e) {
            e.printStackTrace();
		}
        clientDataSockets.remove(dataSocket);
    }

    public ClientDataSocket getUnusedDataSocket() {
        Predicate<ClientDataSocket> predicate = new Predicate<ClientDataSocket>(){
            public boolean test(ClientDataSocket t) {
                return !t.isUsing();
            }
        };
		return clientDataSockets.parallelStream().filter(predicate).findAny().orElse(null);
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public void setServerSocket(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void close(){
        try {
            serverSocket.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        clientDataSockets.forEach(
            t -> {
                    try {
                        t.getSocket().close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        );
    }
}