package io.dbulanova.revolut.service;

import java.util.UUID;

public interface IdempotencyKeyService {
    boolean isDuplicate(UUID key);

    void add(UUID key);
}
