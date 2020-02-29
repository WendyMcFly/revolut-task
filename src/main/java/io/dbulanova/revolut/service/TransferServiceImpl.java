package io.dbulanova.revolut.service;

import io.dbulanova.revolut.domain.Account;
import io.dbulanova.revolut.repository.AccountRepository;
import lombok.RequiredArgsConstructor;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.math.BigDecimal;
import java.util.Objects;

@Singleton
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class TransferServiceImpl implements TransferService {

    private final AccountRepository accountRepository;
    private final AccountTransactionService transactionService;

    @Override
    public void transfer(String accountFromStr, String accountToStr, BigDecimal amount) {
        if ((accountFromStr == null || accountToStr == null) ||
                ((Objects.equals(accountFromStr, accountToStr))) ||
                amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException();
        }
        Account accountFrom = getAccountByNumber(accountFromStr);
        Account accountTo = getAccountByNumber(accountToStr);

        transactionService.transact(accountFrom, accountTo, (from, to) -> {

            if (from.getAccountBalance().compareTo(amount) < 0) {
                throw new IllegalArgumentException("Account " + accountFromStr + " has insufficient funds (req: " + amount + "; actual: " + from.getAccountBalance());
            }

            from.setAccountBalance(from.getAccountBalance().subtract(amount));
            to.setAccountBalance(to.getAccountBalance().add(amount));
        });
    }

    private Account getAccountByNumber(String accountNum) {
        return accountRepository.findByAccountNumber(accountNum)
                .orElseThrow(() -> new IllegalArgumentException("Account " + accountNum + " not found!"));
    }
}
