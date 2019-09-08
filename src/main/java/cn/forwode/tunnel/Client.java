package cn.forwode.tunnel;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Client {

    private String id;
    private String pid;
    private String machineName;
    private String ip;
    private Map<String, ServerAndClientDataSockets> portSocketMap;
    private Socket controlSocket;

    public Client(String pid, String machineName, String ip) {
        this.pid = pid;
        this.machineName = machineName;
        this.ip = ip;
        this.id = String.format("%s@%s@%s", pid, machineName, ip);
        portSocketMap = new HashMap<>();
    }

    public void destory() {

        try {
            controlSocket.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

		portSocketMap.forEach(
            (key, value) -> {
                value.close();
            }
        );
        portSocketMap.clear();
    }
    
    public void setPortServerSocket(String port, ServerSocket serverSocket){
        if (portSocketMap.get(port) == null){
            ServerAndClientDataSockets serverAndClientDataSockets = new ServerAndClientDataSockets();
            portSocketMap.put(port, serverAndClientDataSockets);
        }
        portSocketMap.get(port).setServerSocket(serverSocket);
    }

    public ServerSocket getPortServerSocket(String port){
         return portSocketMap.get(port).getServerSocket();
    }

    public void addPortClientDataSocket(String port, ClientDataSocket dataSocket){
        System.out.println("port serverAndClientDataSockets="+portSocketMap.get(port));
        if (portSocketMap.get(port) == null){
            ServerAndClientDataSockets serverAndClientDataSockets = new ServerAndClientDataSockets();
            portSocketMap.put(port, serverAndClientDataSockets);
        }
        System.out.println("port serverAndClientDataSockets="+port+portSocketMap.get(port));
        portSocketMap.get(port).add(dataSocket);
    }

    public ClientDataSocket getUnusedClientDataSocket(String port){
        return portSocketMap.get(port).getUnusedDataSocket();
    }

    
	public void deletePortDataSocket(String port, ClientDataSocket clientDataSocket) {
        portSocketMap.get(port).remove(clientDataSocket);
	}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getMachineName() {
        return machineName;
    }

    public void setMachineName(String machineName) {
        this.machineName = machineName;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Socket getControlSocket() {
        return controlSocket;
    }

    public void setControlSocket(Socket controlSocket) {
        this.controlSocket = controlSocket;
    }

    public Map<String, ServerAndClientDataSockets> getPortSocketMap() {
        return portSocketMap;
    }

    public void setPortSocketMap(Map<String, ServerAndClientDataSockets> portSocketMap) {
        this.portSocketMap = portSocketMap;
    }



}