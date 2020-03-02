package io.dbulanova.revolut.service.exception;

import org.jooby.Err;

public class AccountNotFoundException extends Err {
    public AccountNotFoundException(String s) {
        super(404, s);
    }
}
