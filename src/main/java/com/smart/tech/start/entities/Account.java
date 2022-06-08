package com.smart.tech.start.entities;

import java.util.ArrayList;
import java.util.List;

public class Account {

    private int balance =0;
    private int transactionLimit=1500;

    private List<Client> clients = new ArrayList<>();

    public Account(Client client) {
        clients.add(client);
    }

    public int getBalance() {
        return balance;
    }

    public void deposit(int amount) {
        if (!(amount>0)){
            throw new IllegalArgumentException();
        }
        balance += amount;
    }

    public void withdraw(int amount) {
        if (amount > balance || transactionLimit < amount){
            throw new IllegalStateException();
        }
        balance -= amount;
    }

    public void setTransactionLimit(int transactionLimit) {
        this.transactionLimit = transactionLimit;
    }

    public List<Client> getClients() {
        return clients;
    }

    public void addClient(Client client) {
        clients.add(client);
    }

    public void remove(Client client1) {
        if (clients.size() <= 1){
            throw new IllegalStateException();
        }
        clients.remove(client1);
    }
}
