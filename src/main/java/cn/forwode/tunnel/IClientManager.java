package cn.forwode.tunnel;

public interface IClientManager{

    public Client getClientById(String id);
    public Client getClientByPort(String port);
    public void add(Client client);
    public void remove(Client client);
    
}