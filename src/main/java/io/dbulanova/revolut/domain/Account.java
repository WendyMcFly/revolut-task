package io.dbulanova.revolut.domain;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.concurrent.locks.ReentrantLock;

public class Account {

    private final ReentrantLock lock = new ReentrantLock();

    @Getter
    private String accountNumber;

    @Getter
    @Setter
    private volatile BigDecimal accountBalance;

    public Account(String accountNumber, BigDecimal accountBalance) {
        this.accountNumber = accountNumber;
        this.accountBalance = accountBalance;
    }

    public void lock() {
        lock.lock();
    }

    public void unlock() {
        lock.unlock();
    }
}
