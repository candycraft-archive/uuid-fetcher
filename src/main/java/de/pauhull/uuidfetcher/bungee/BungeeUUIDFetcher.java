package de.pauhull.uuidfetcher.bungee;

import de.pauhull.uuidfetcher.common.fetcher.CachedUUIDFetcher;
import de.pauhull.uuidfetcher.common.fetcher.UUIDFetcher;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.UUID;
import java.util.function.Consumer;

/**
 * Created by Paul
 * on 23.11.2018
 *
 * @author pauhull
 */
public class BungeeUUIDFetcher implements UUIDFetcher {

    private static UUIDFetcherBungeePlugin plugin = UUIDFetcherBungeePlugin.getInstance();

    @Override
    public void fetchUUIDAsync(String playerName, Consumer<UUID> consumer) {
        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(playerName);
        if (player != null) {
            CachedUUIDFetcher.getCache().save(player.getUniqueId(), player.getName());
            consumer.accept(player.getUniqueId());
            return;
        }

        plugin.getCachedUUIDFetcher().fetchUUIDAsync(playerName, consumer);
    }

    @Override
    public void fetchNameAsync(UUID uuid, Consumer<String> consumer) {
        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(uuid);
        if (player != null) {
            CachedUUIDFetcher.getCache().save(player.getUniqueId(), player.getName());
            consumer.accept(player.getName());
            return;
        }

        plugin.getCachedUUIDFetcher().fetchNameAsync(uuid, consumer);
    }

    @Override
    public void getNameCaseSensitive(String name, Consumer<String> consumer) {
        fetchUUIDAsync(name, uuid -> fetchNameAsync(uuid, consumer));
    }

}
