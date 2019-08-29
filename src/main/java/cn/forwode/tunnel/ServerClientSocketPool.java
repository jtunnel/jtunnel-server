package cn.forwode.tunnel;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServerClientSocketPool {
    
    private static Map<String, ServerClientSocket> clientSocketsMap = new HashMap<String, ServerClientSocket>();
    
    public static ServerClientSocket getServerClientSocket(String port){
        ServerClientSocket serverClientSocket = clientSocketsMap.get(port);
        if (serverClientSocket == null) {
            serverClientSocket = new ServerClientSocket();
            clientSocketsMap.put(port, serverClientSocket);
        }
        return serverClientSocket;
    }

	public static void clearSockets(String port) {
        try {
            clientSocketsMap.get(port).getServerSocket().close();
            clientSocketsMap.get(port).getControlSocket().close();
            List<ClientDataSocket> clientDataSockets = clientSocketsMap.get(port).getClientDataSockets();
            for (ClientDataSocket clientDataSocket : clientDataSockets) {
                clientDataSocket.getSocket().close();
            }
            clientSocketsMap.put(port, null);
        } catch (IOException e) {
            e.printStackTrace();
		}
	}

	public static ClientDataSocket getUnusingDataSocket(String port) {
        ClientDataSocket unusingClientDataSocket = null;
        List<ClientDataSocket> clientDataSockets = clientSocketsMap.get(port).getClientDataSockets();
        for (ClientDataSocket clientDataSocket : clientDataSockets) {
            if (clientDataSocket.isUsing() == false && !clientDataSocket.getSocket().isClosed()) {
                unusingClientDataSocket =  clientDataSocket;
                break;
            } 
        }
		return unusingClientDataSocket;
    }
    
    public static void deleteDataSocket(String port, ClientDataSocket dataSocket){
        List<ClientDataSocket> clientDataSockets = clientSocketsMap.get(port).getClientDataSockets();
        clientDataSockets.remove(dataSocket);
    }

}