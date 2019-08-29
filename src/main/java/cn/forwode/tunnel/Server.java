package cn.forwode.tunnel;

import java.net.ServerSocket;  
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Server {  
    final static Logger logger = LoggerFactory.getLogger(Server.class);
    public static void main(String[] args) throws Exception{  
        logger.info("start server...............");
        ServerSocket server = new ServerSocket(20006);  
        Socket clientSocket = null;  
        boolean f = true;  
        while(f){  
            clientSocket = server.accept();  
            clientSocket.setSoTimeout(3000);
            logger.info("连接成功: " + clientSocket.getRemoteSocketAddress().toString());  
            (new ServerThread(clientSocket)).start();  
        }  
        server.close();  
    }  
}  