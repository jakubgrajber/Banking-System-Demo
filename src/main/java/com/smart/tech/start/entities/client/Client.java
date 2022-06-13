package com.smart.tech.start.entities.client;

public abstract class Client {

    private final String firstname;
    private final String lastname;

    public Client(String firstname, String lastname) {
        this.firstname = firstname;
        this.lastname = lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }
}
