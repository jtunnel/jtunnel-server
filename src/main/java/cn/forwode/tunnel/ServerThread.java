package cn.forwode.tunnel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketTimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerThread extends Thread {
    
    final Logger logger = LoggerFactory.getLogger(getClass());
    private Socket clientSocket;
    private IServerSocketHandler handler;

    public ServerThread(Socket clientSocket, IServerSocketHandler handler) {
        super("ServerThread");
        this.clientSocket = clientSocket;
        this.handler = handler;
    }

    public void run() {

        try {
            BufferedReader clientInputBuf = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            boolean flag = true;
            while (flag) {
                String clientStr = "";
                try {
                    clientStr = clientInputBuf.readLine();
                } catch (SocketTimeoutException e) {
                }

                if (clientStr.startsWith("CTRL=")) {
                    
                    String pid = clientStr.split("=")[1].split("@")[0];
                    String machineName = clientStr.split("=")[1].split("@")[1];
                    String ip = clientSocket.getInetAddress().toString();
                    Client client = new Client(pid, machineName, ip);
                    handler.onRequestControl(clientSocket, client);
                    flag = false;
                }
                
                if (clientStr.startsWith("R=")) {
                    String port = clientStr.split("=")[2];
                    String pid = clientStr.split("=")[1].split("@")[0];
                    String machineName = clientStr.split("=")[1].split("@")[1];
                    String ip = clientSocket.getInetAddress().toString();
                    String id = String.format("%s@%s@%s", pid, machineName, ip);
                    Client client = ClientManager.getInstance().getClientById(id);
                    handler.onRequestRemoteForward(clientSocket, client, port);
                    flag = false;
                }

                if (clientStr.startsWith("L")) {
                    String addr = clientStr.split("=")[1];
                    logger.info("forward to " + addr);
                    flag = false;
                }

            }
        } catch (IOException e) {
            logger.error("IOException", e);
        } finally {
            logger.info("end thread...");
        }

    }

}