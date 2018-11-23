package de.pauhull.uuidfetcher.bungee;

import com.ikeirnez.pluginmessageframework.PacketManager;
import com.ikeirnez.pluginmessageframework.implementations.BungeeCordPacketManager;
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
    private PacketManager packetManager;

    @Getter
    private ExecutorService executorService;

    @Getter
    private CachedUUIDFetcher cachedUUIDFetcher;

    @Override
    public void onEnable() {
        instance = this;

        this.executorService = Executors.newSingleThreadExecutor(new UUIDFetcherThreadFactory());
        this.cachedUUIDFetcher = new CachedUUIDFetcher(executorService);
        this.packetManager = new BungeeCordPacketManager(this, "UUIDFetcherChannel");
        this.packetManager.registerListener(new BungeePacketHandler(this));
    }

    @Override
    public void onDisable() {
        this.executorService.shutdown();
    }

}
