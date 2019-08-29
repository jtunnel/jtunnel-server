package cn.forwode.tunnel;

import java.net.Socket;

public class ClientDataSocket{
    
    private boolean isUsing;
    private Socket socket;

    public ClientDataSocket(Socket socket){
        this.isUsing = false;
        this.socket = socket;
    }
    
    public boolean isUsing(){
        return isUsing;
    }
    
    public void setUsing(boolean isUsing){
        this.isUsing = isUsing;
    }
    
    public Socket getSocket(){
        return socket;
    }
    
    public void setSocket(Socket socket){
        this.socket = socket;
    }

}