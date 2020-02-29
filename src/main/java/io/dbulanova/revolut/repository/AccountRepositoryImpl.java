package io.dbulanova.revolut.repository;

import io.dbulanova.revolut.domain.Account;

import javax.inject.Singleton;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Singleton
public class AccountRepositoryImpl implements AccountRepository {

    private final Map<String, Account> accounts = new ConcurrentHashMap<>();

    @Override
    public Collection<Account> getAll() {
        return accounts.values();
    }

    @Override
    public Optional<Account> findByAccountNumber(String accountNumber) {
        return Optional.ofNullable(accounts.get(accountNumber));
    }

    @Override
    public Account save(Account account) {
        return accounts.put(account.getAccountNumber(), account);
    }

    @Override
    public void delete(Account account) {
        accounts.remove(account.getAccountNumber());
    }

    public void clear() {
        accounts.clear();
    }
}
