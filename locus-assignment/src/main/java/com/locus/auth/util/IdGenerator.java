package com.locus.auth.util;

import java.util.concurrent.atomic.AtomicLong;

public final class IdGenerator {

    private final static AtomicLong idGenerator;

    private IdGenerator() {

    }

    static {
        idGenerator = new AtomicLong(10000);
    }

    public static Long generateId() {
        return idGenerator.getAndIncrement();
    }
}
