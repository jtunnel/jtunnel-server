package cn.forwode.tunnel;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReadFromUserThread extends Thread {

    final Logger logger = LoggerFactory.getLogger(getClass());
    private Socket userSocket;
    private ClientDataSocket clientDataSocket;
    private Client client;

    public ReadFromUserThread(Socket userSocket, ClientDataSocket clientDataSocket, Client client) {
        super("ReadFromUserThread");
        this.userSocket = userSocket;
        this.clientDataSocket = clientDataSocket;
        this.client = client;
    }

    public void run() {
        Socket clientSocket = clientDataSocket.getSocket();
        int data = 0;
        try {
            InputStream inputStream = userSocket.getInputStream();
            OutputStream outputStream = clientSocket.getOutputStream();
            byte[] buffer = new byte[1024000];
            while (data != -1 && !userSocket.isClosed() && !clientSocket.isClosed()) {
                try {
                    data = inputStream.read(buffer);
                    if (data != -1) {
                        outputStream.write(buffer, 0, data);
                    } else {
                        logger.error("read from user return -1");
                        break;
                    }
                } catch (SocketTimeoutException e) {
                }

            }
        } catch (IOException e) {
            logger.error("ReadFromUser error", e);
        } finally {
            logger.error("socket closed, end ReadFromUserThread.....");
            client.deletePortDataSocket(Integer.toString(userSocket.getLocalPort()), clientDataSocket);
            try {
                clientSocket.close();
                userSocket.close();
            } catch (IOException e) {
                logger.error("close socket error", e);
            }

        }

    }

}