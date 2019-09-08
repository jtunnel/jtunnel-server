package cn.forwode.tunnel;

import java.net.Socket;

public interface IServerSocketHandler {

    public void onRequestControl(Socket clientSocket, Client client);
    public void onRequestRemoteForward(Socket clientSocket, Client client, String port);
    public void onRequestLocalForward(Socket clientSocket);
    public void onRequestDynamicForward(Socket clientSocket);

}