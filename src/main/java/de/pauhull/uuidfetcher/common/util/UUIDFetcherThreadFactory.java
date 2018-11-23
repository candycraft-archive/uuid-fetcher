package de.pauhull.uuidfetcher.common.util;

import lombok.Getter;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Paul
 * on 23.11.2018
 *
 * @author pauhull
 */
public class UUIDFetcherThreadFactory implements ThreadFactory {

    @Getter
    private static final String THREAD_NAME_PREFIX = "UUID Fetcher Thread";

    @Getter
    private static final AtomicInteger CURRENT_ID = new AtomicInteger(0);

    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(r);
        thread.setName(THREAD_NAME_PREFIX + " #" + CURRENT_ID.incrementAndGet());
        return thread;
    }

}
