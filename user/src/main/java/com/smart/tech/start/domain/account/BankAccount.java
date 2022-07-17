package com.smart.tech.start.domain.account;

import com.smart.tech.start.domain.utilites.Money;

public interface BankAccount {
    void sendTransfer(Money money, BankAccount recipient);
    void receiveTransfer(Money money, BankAccount sender);
    boolean isBalanceEmpty();
}
