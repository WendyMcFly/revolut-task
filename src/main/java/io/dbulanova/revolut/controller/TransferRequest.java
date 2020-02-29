package io.dbulanova.revolut.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferRequest {

    private String accountFrom;
    private String accountTo;
    private BigDecimal amount;
}
