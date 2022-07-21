package com.smart.tech.start.domain.account;

import com.smart.tech.start.domain.utilities.Money;

/**
 * This interface imposes the general behaviour of a bank account on its specific implementation.
 */

public interface BankAccount {
    void send(Money money, BankAccount recipient);

    void receive(Money money, BankAccount sender);
}
