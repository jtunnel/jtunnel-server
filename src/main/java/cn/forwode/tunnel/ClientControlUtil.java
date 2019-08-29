package cn.forwode.tunnel;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

public class ClientControlUtil {

    public static void sendMsg(String port, String msg) throws IOException{
        Socket controlSocket = ServerClientSocketPool.getServerClientSocket(port).getControlSocket();
        PrintStream controlPrintStream = new PrintStream(controlSocket.getOutputStream());
        controlPrintStream.println(msg);
    }
}