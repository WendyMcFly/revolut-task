package io.dbulanova.revolut.service.exception;

import org.jooby.Err;

public class InvalidTransferException extends Err {
    public InvalidTransferException(String s) {
        super(400, s);
    }
}
