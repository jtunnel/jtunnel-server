package cn.forwode.tunnel;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PortServerThread extends Thread {  
    final Logger logger = LoggerFactory.getLogger(getClass());
    private String port;
    private Client client;
    
    public PortServerThread(String port, Client client){  
        super("PortServerThread-"+port);
        this.port = port;
        this.client = client;
    }  

    public void run() {  

        ServerSocket server = client.getPortServerSocket(port);
        Socket userSocket = null;
        
        try {
            while(!server.isClosed()) {
                try {
                    userSocket = server.accept();
                    ClientControlUtil.sendMsg(client, "NEW="+port);
                    logger.info("用户连接成功: "+userSocket.getRemoteSocketAddress().toString());
                    userSocket.setSoTimeout(3000);
                    ClientDataSocket unUsedclientDataSocket = client.getUnusedClientDataSocket(port);
                    if (unUsedclientDataSocket != null){
                        unUsedclientDataSocket.setUsing(true);
                        SocketConnector.connect(userSocket, unUsedclientDataSocket, client);
                    } else {
                        logger.warn("no more client socket connect to......");
                        userSocket.close();
                    }
                } catch (SocketTimeoutException e) {
                }
            }
        } catch (IOException e1) {
            logger.error("IOException", e1);
		}  finally {
            try {
                server.close();
            } catch (IOException e) {
                logger.error("server socket close error", e);
            }
            
        }

    }  

}  