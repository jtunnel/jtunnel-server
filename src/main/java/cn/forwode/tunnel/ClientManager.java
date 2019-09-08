package cn.forwode.tunnel;

import java.util.ArrayList;
import java.util.List;

public class ClientManager implements IClientManager{

    private static ClientManager instance = new ClientManager();
    private List<Client> clients = new ArrayList<Client>();

    public static ClientManager getInstance(){
        return instance;
    }

    public Client getClientById(String id) {
        return clients.parallelStream().filter(t -> t.getId().equals(id)).findAny().orElse(null);
    }

    public Client getClientByPort(String port) {
		return clients.parallelStream().filter(t -> t.getPortSocketMap().containsKey(port)).findAny().orElse(null);
    }

    public void add(Client client) {
        clients.add(client);
    }

    public void remove(Client client) {
        client.destory();
        clients.remove(client);
    }

    public List<Client> getClients() {
        return clients;
    }

    public void setClients(List<Client> clients) {
        this.clients = clients;
    }
}