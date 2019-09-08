package cn.forwode.tunnel;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

public class ClientControlUtil {

    public static void sendMsg(Client client, String msg) throws IOException{
        Socket controlSocket = client.getControlSocket();
        PrintStream controlPrintStream = new PrintStream(controlSocket.getOutputStream());
        controlPrintStream.println(msg);
    }
}