package io.dbulanova.revolut.domain;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.concurrent.locks.ReentrantLock;

public class Account {

    private final ReentrantLock lock = new ReentrantLock();

    @Getter
    private String number;

    @Getter
    private Currency currency;

    @Getter
    @Setter
    private volatile BigDecimal balance;

    public Account(String number, BigDecimal balance, Currency currency) {
        this.number = number;
        this.balance = balance;
        this.currency = currency;
    }

    public void lock() {
        lock.lock();
    }

    public void unlock() {
        lock.unlock();
    }
}
