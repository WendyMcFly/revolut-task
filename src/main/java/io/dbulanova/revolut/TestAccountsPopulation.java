package io.dbulanova.revolut;

import io.dbulanova.revolut.domain.Account;
import io.dbulanova.revolut.repository.AccountRepository;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.stream.IntStream;

class TestAccountsPopulation {

    static void populateAccounts(AccountRepository rep) {

        IntStream.range(1, 10)
                .mapToObj(i -> new Account(String.valueOf(i), BigDecimal.valueOf(i), Currency.getInstance("USD")))
                .forEach(rep::save);
    }
}
