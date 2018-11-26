package de.pauhull.uuidfetcher.bungee;

import de.pauhull.uuidfetcher.common.fetcher.CachedUUIDFetcher;
import de.pauhull.uuidfetcher.common.util.UUIDFetcherThreadFactory;
import lombok.Getter;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Paul
 * on 23.11.2018
 *
 * @author pauhull
 */
public class UUIDFetcherBungeePlugin extends Plugin {

    @Getter
    private static UUIDFetcherBungeePlugin instance = null;

    @Getter
    private ExecutorService executorService;

    @Getter
    private CachedUUIDFetcher cachedUUIDFetcher;

    @Override
    public void onEnable() {
        instance = this;

        this.executorService = Executors.newSingleThreadExecutor(new UUIDFetcherThreadFactory());
        this.cachedUUIDFetcher = new CachedUUIDFetcher(executorService);
        new BungeeMessageListener(this);
    }

    @Override
    public void onDisable() {
        this.executorService.shutdown();
    }

}
