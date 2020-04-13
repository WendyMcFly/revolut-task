package io.dbulanova.revolut.service;

import javax.inject.Singleton;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Singleton
public class IdempotencyKeyServiceImpl implements IdempotencyKeyService {

    private Set<UUID> set = new HashSet<>();


    @Override
    public boolean isDuplicate(UUID key) {
        synchronized (this) {
            return set.contains(key);
        }
    }

    @Override
    public void add(UUID key) {
        synchronized (this) {
            set.add(key);
        }
    }
}
