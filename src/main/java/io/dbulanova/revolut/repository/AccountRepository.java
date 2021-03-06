package io.dbulanova.revolut.repository;

import io.dbulanova.revolut.domain.Account;

import java.util.Collection;
import java.util.Optional;

public interface AccountRepository {

    Collection<Account> getAll();

    Optional<Account> findByAccountNumber(String accountNumber);

    Account save(Account account);
}
