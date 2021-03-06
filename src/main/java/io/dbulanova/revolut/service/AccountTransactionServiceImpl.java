package io.dbulanova.revolut.service;

import io.dbulanova.revolut.domain.Account;

import javax.inject.Singleton;
import java.util.function.BiConsumer;

@Singleton
public class AccountTransactionServiceImpl implements AccountTransactionService {

    @Override
    public void transact(Account account1, Account account2, BiConsumer<Account, Account> action) {
        Account first;
        Account second;

        if (account1.getNumber().compareTo(account2.getNumber()) > 0) {
            first = account1;
            second = account2;
        } else {
            first = account2;
            second = account1;
        }
        first.lock();
        try {
            second.lock();
            try {
                action.accept(account1, account2);
            } finally {
                second.unlock();
            }
        } finally {
            first.unlock();
        }
    }
}
