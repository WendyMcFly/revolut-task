package io.dbulanova.revolut.service;

import java.math.BigDecimal;

public interface TransferService {

    void transfer(String accountFrom, String accountTo, BigDecimal amount);
}
