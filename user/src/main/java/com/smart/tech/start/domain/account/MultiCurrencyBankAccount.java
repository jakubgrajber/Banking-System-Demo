package com.smart.tech.start.domain.account;

import com.smart.tech.start.domain.utilites.Money;

public class MultiCurrencyBankAccount implements BankAccount {

    @Override
    public void sendTransfer(Money money, BankAccount recipient) {

    }

    @Override
    public void receiveTransfer(Money money, BankAccount sender) {

    }

    @Override
    public boolean isBalanceEmpty() {
        return false;
    }
}
