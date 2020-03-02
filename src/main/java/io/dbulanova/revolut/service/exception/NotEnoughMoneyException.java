package io.dbulanova.revolut.service.exception;

public class NotEnoughMoneyException extends RuntimeException {
    public NotEnoughMoneyException(String s) {
        super(s);
    }
}
