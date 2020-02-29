package io.dbulanova.revolut.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.dbulanova.revolut.domain.Account;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@JsonPropertyOrder({"accountNumber", "accountBalance"})
public class AccountDto {

    private String accountNumber;

    private BigDecimal accountBalance;

    public static AccountDto fromDomain(Account account) {
        return new AccountDto(account.getAccountNumber(), account.getAccountBalance());
    }
}