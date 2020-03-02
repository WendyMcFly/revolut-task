package io.dbulanova.revolut.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.dbulanova.revolut.domain.Account;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@JsonPropertyOrder({"number", "balance", "currency"})
public class AccountDto {

    private String number;

    private BigDecimal balance;

    private String currency;

    public static AccountDto fromDomain(Account account) {
        return new AccountDto(account.getNumber(), account.getBalance(), account.getCurrency().getDisplayName());
    }
}
