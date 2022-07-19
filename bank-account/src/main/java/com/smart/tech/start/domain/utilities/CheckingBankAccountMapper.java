package com.smart.tech.start.domain.utilities;

import com.smart.tech.start.domain.account.CheckingBankAccount;
import com.smart.tech.start.management.entity.AccountEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.springframework.stereotype.Component;

import java.util.Currency;

@Component
@Mapper(componentModel = "spring")
public interface CheckingBankAccountMapper {

    @Mapping(target = "balance", source = "account")
    @Mapping(target = "currency", source = "account")
    CheckingBankAccount map(AccountEntity account);

    default Currency mapCurrency(AccountEntity account){
        return Currency.getInstance(account.getCurrencyCode());
    }

    default Money mapBalance(AccountEntity account){
        return new Money(account.getBalance(), Currency.getInstance(account.getCurrencyCode()));
    }

}
