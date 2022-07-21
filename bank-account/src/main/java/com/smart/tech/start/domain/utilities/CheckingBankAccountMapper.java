package com.smart.tech.start.domain.utilities;

import com.smart.tech.start.domain.account.CheckingBankAccount;
import com.smart.tech.start.management.entity.CheckingBankAccountEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Currency;

@Component
@Mapper(componentModel = "spring")
public interface CheckingBankAccountMapper {

    @Mapping(target = "balance", source = "account")
    @Mapping(target = "currency", source = "account")
    CheckingBankAccount entityToDomainModel(CheckingBankAccountEntity account);

    default Currency mapCurrency(CheckingBankAccountEntity account){
        return Currency.getInstance(account.getCurrencyCode());
    }

    default Money mapBalance(CheckingBankAccountEntity account){
        return new Money(account.getBalance(), Currency.getInstance(account.getCurrencyCode()));
    }

    @Mapping(target = "balance", source = "account")
    @Mapping(target = "currencyCode", source = "account")
    CheckingBankAccountEntity domainModelToEntity(CheckingBankAccount account);

    default BigDecimal mapBalanceAmount(CheckingBankAccount account){
        return account.getBalance().getAmount();
    }

    default String mapCurrencyCode(CheckingBankAccount account){
        return account.getCurrency().getCurrencyCode();
    }

}
