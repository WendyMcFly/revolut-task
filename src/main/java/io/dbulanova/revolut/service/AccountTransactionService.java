package io.dbulanova.revolut.service;

import io.dbulanova.revolut.domain.Account;

import java.util.function.BiConsumer;

public interface AccountTransactionService {

    void transact(Account account1, Account account2, BiConsumer<Account, Account> action);
}
