package cn.forwode.tunnel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerThread extends Thread {
    
    final Logger logger = LoggerFactory.getLogger(getClass());
    private Socket clientSocket = null;

    public ServerThread(Socket clientSocket) {
        super("ServerThread");
        this.clientSocket = clientSocket;
    }

    public void run() {

        try {
            PrintStream controlPrintStream = new PrintStream(clientSocket.getOutputStream());
            BufferedReader clientInputBuf = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            boolean flag = true;
            while (flag) {
                String clientStr = "";
                try {
                    clientStr = clientInputBuf.readLine();
                } catch (SocketTimeoutException e) {
                }
                
                if (clientStr.startsWith("R=")) {
                    ServerSocket serverSocket = null;
                    String port = clientStr.split("=")[1];
                    try {
                        serverSocket = new ServerSocket(Integer.parseInt(port));
                        controlPrintStream.println("200");
                        ServerClientSocketPool.getServerClientSocket(port).setServerSocket(serverSocket);
                        ServerClientSocketPool.getServerClientSocket(port).setControlSocket(clientSocket);
                        Thread controlThread = new ControlThread(port, clientSocket);
                        // ControlThreadManager.addControlThread(port, controlThread);
                        controlThread.start();
                        new PortServerThread(port).start();
                        // server.setSoTimeout(3000);
                    } catch (NumberFormatException e2) {
                        logger.error("NumberFormatException", e2);
                        controlPrintStream.println("400");
                    } catch (IOException e2) {
                        logger.error("new data socket, port has bind......." + port);
                        ClientDataSocket clientDataSocket = new ClientDataSocket(clientSocket);
                        ServerClientSocketPool.getServerClientSocket(port).getClientDataSockets().add(clientDataSocket);
                    }
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