package io.dbulanova.revolut.service;

import java.math.BigDecimal;
import java.util.Currency;

public interface CurrencyRatesProvider {

    BigDecimal getCurrencyRate(Currency from, Currency to);
}
