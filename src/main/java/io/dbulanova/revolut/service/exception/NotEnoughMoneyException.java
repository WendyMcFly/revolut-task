package io.dbulanova.revolut.service.exception;

import org.jooby.Err;

public class NotEnoughMoneyException extends Err {
    public NotEnoughMoneyException(String s) {
        super(400, s);
    }
}
