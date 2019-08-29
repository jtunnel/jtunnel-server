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
    
    public PortServerThread(String port){  
        super("PortServerThread-"+port);
        this.port = port;
    }  

    public void run() {  

        ServerSocket server = ServerClientSocketPool.getServerClientSocket(port).getServerSocket();
        Socket userSocket = null;
        
        try {
            while(!server.isClosed()) {
                try {
                    userSocket = server.accept();
                    ClientControlUtil.sendMsg(port, "NEW");
                    logger.info("用户连接成功: "+userSocket.getRemoteSocketAddress().toString());
                    userSocket.setSoTimeout(3000);
                    ClientDataSocket unUsedclientDataSocket = ServerClientSocketPool.getUnusingDataSocket(port);
                    if (unUsedclientDataSocket != null){
                        unUsedclientDataSocket.setUsing(true);
                        SocketConnector.connect(userSocket, unUsedclientDataSocket, server);
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