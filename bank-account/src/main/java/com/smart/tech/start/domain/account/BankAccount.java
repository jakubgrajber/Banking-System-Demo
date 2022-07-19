package com.smart.tech.start.domain.account;


import com.smart.tech.start.domain.utilities.Money;

public interface BankAccount {
    void sendTransfer(Money money, BankAccount recipient);

    void receiveTransfer(Money money, BankAccount sender);

    boolean isBalanceEmpty();
}
