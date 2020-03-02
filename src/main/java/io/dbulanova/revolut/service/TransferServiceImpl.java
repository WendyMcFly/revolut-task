package io.dbulanova.revolut.service;

import io.dbulanova.revolut.domain.Account;
import io.dbulanova.revolut.repository.AccountRepository;
import io.dbulanova.revolut.service.exception.AccountNotFoundException;
import io.dbulanova.revolut.service.exception.InvalidTransferException;
import io.dbulanova.revolut.service.exception.NotEnoughMoneyException;
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
    private final CurrencyRatesProvider currencyRatesProvider;

    @Override
    public void transfer(String accountFromStr, String accountToStr, BigDecimal amount) {
        if ((accountFromStr == null || accountToStr == null) ||
                ((Objects.equals(accountFromStr, accountToStr))) ||
                amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidTransferException();
        }
        Account accountFrom = getAccountByNumber(accountFromStr);
        Account accountTo = getAccountByNumber(accountToStr);

        BigDecimal currencyRate = currencyRatesProvider.getCurrencyRate(accountFrom.getCurrency(), accountTo.getCurrency());
        BigDecimal amountToTransfer = amount.multiply(currencyRate);

        transactionService.transact(accountFrom, accountTo, (from, to) -> {

            if (from.getBalance().compareTo(amount) < 0) {
                throw new NotEnoughMoneyException("Account " + accountFromStr + " has insufficient funds (req: " + amount + "; actual: " + from.getBalance() + ")");
            }

            from.setBalance(from.getBalance().subtract(amount));
            to.setBalance(to.getBalance().add(amountToTransfer));
        });
    }

    private Account getAccountByNumber(String accountNum) {
        return accountRepository.findByAccountNumber(accountNum)
                .orElseThrow(() -> new IllegalArgumentException("Account " + accountNum + " is not found!"));
    }
}
