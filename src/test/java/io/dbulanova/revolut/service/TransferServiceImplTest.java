package io.dbulanova.revolut.service;

import io.dbulanova.revolut.domain.Account;
import io.dbulanova.revolut.repository.AccountRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class TransferServiceImplTest {

    @Mock
    private AccountRepository accountRepository;
    @Mock
    private CurrencyRatesProvider currencyRatesProvider;
    private AccountTransactionService transactionService = (account1, account2, action) -> action.accept(account1, account2); // mock
    private TransferService transferService;
    private Account account1;
    private Account account2;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
        transferService = new TransferServiceImpl(accountRepository,  transactionService, currencyRatesProvider);
        Mockito.when(currencyRatesProvider.getCurrencyRate(Currency.getInstance("RUB"), Currency.getInstance("RUB")))
                .thenReturn(BigDecimal.ONE);
        Mockito.when(currencyRatesProvider.getCurrencyRate(Currency.getInstance("USD"), Currency.getInstance("EUR")))
                .thenReturn(new BigDecimal("0.92"));
    }

    @Test
    public void checkSimpleTransfer() {
        givenAccountsWithTheSameCurrencies();

        transferService.transfer("acc1", "acc2", new BigDecimal("125.0"));

        thenTransferIsCorrect();
    }

    @Test
    public void checkConversionTransfer() {
        givenAccountsWithDifferentCurrencies();

        transferService.transfer("acc1", "acc2", new BigDecimal("20"));

        thenConversionIsCorrect();
    }

    private void givenAccountsWithTheSameCurrencies() {
        account1 = new Account("acc1", BigDecimal.valueOf(20000), Currency.getInstance("RUB"));
        account2 = new Account("acc2", BigDecimal.valueOf(40000), Currency.getInstance("RUB"));
        Mockito.when(accountRepository.findByAccountNumber("acc1"))
                .thenReturn(Optional.of(account1));
        Mockito.when(accountRepository.findByAccountNumber("acc2"))
                .thenReturn(Optional.of(account2));
    }

    private void givenAccountsWithDifferentCurrencies() {
        account1 = new Account("acc1", BigDecimal.valueOf(14000), Currency.getInstance("USD"));
        account2 = new Account("acc2", BigDecimal.valueOf(40000), Currency.getInstance("EUR"));

        Mockito.when(accountRepository.findByAccountNumber("acc1"))
                .thenReturn(Optional.of(account1));
        Mockito.when(accountRepository.findByAccountNumber("acc2"))
                .thenReturn(Optional.of(account2));
    }

    private void thenConversionIsCorrect() {
        assertThat(account1.getBalance(), is(new BigDecimal("13980")));
        assertThat(account2.getBalance(), is(new BigDecimal("40018.40")));
    }

    private void thenTransferIsCorrect() {
        assertThat(account1.getBalance(), is(new BigDecimal("19875.0")));
        assertThat(account2.getBalance(), is(new BigDecimal("40125.0")));
    }
}