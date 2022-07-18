package com.smart.tech.start.domain.account;

import java.math.BigDecimal;
import java.math.BigInteger;

abstract public class BankAccount {

    private BigDecimal balance = new BigDecimal(BigInteger.ZERO);

    public boolean isBalanceEmpty() {
        return balance.equals(BigDecimal.ZERO);
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
