package cn.forwode.tunnel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* 

客户端和服务端socket控制线程

 */
public class ControlThread extends Thread {
    
    final Logger logger = LoggerFactory.getLogger(getClass());
    private Client client;
    private Socket clientSocket;

    public ControlThread(Client client, Socket clientSocket) {
        super("ControlThread-" + client.getId());
        this.client = client;
        this.clientSocket = clientSocket;
	}

	public void run() {
        OutputStream outputStream = null;
        try {
            clientSocket.setSoTimeout(10000);
        } catch (SocketException e1) {
            e1.printStackTrace();
		}
        try {
            outputStream = clientSocket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        PrintStream controlPrintStream = new PrintStream(outputStream);
        try {
            BufferedReader clientInputBuf = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            boolean flag = true;
            while (flag) {
                Thread.sleep(3000);
                controlPrintStream.println("ping");
                String clientStr = "";
                try {
                    clientStr = clientInputBuf.readLine();
                } catch (SocketTimeoutException e) {
                    flag = false;
                }
            }
        } catch (IOException e) {
            logger.error("IOException...", e);
        } catch (InterruptedException e) {
            logger.error("Interrupted...", e);
        } finally {
            ClientManager.getInstance().remove(client);
            logger.warn("End Control Thread...");
        }
    }
}