package com.smart.tech.start.domain.user;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * User is a domain model that represents a client.
 * It is used to manage the profile of the client and his accounts.
 * There is no validation rules in this layer. All the validation is done in application layer.
 * We can easily assume that we are not going to meet invalid data in User domain model.
 */

public class User {

    private String firstName;
    private String lastName;
    private String email;
    private String password;

    private List<UUID> accounts = new ArrayList<>(3);
    private Boolean enabled = false;

    public User() {
    }

    public User(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    public void addAccount(UUID account) {
        if (!enabled)
            throw new IllegalStateException("Cannot add an account for disabled User.");

        accounts.add(account);
    }

    public void enable() {
        if (enabled)
            throw new IllegalStateException("The User has already been enabled.");
        enabled = true;
    }

    public List<UUID> getAccounts() {
        return accounts;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void removeAccount(UUID account) {
        if (!enabled)
            throw new IllegalStateException("Cannot remove account - User is disabled.");
        if (!accounts.contains(account))
            throw new IllegalStateException("There is no such account assigned to this User.");
        accounts.remove(account);
    }

    public void disable() {
        if (!enabled)
            throw new IllegalStateException("The User has already been disabled.");
        enabled = false;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void changePassword(String newPassword) {
        this.password = newPassword;
    }

    public void changeEmail(String newEmail) {
        this.email = newEmail;
    }
}
