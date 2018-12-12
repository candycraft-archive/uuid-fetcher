package de.pauhull.uuidfetcher.bungee;

import de.pauhull.uuidfetcher.common.fetcher.CachedUUIDFetcher;
import de.pauhull.uuidfetcher.common.fetcher.Profile;
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
    public void fetchProfileAsync(UUID uuid, Consumer<Profile> consumer) {
        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(uuid);
        if (player != null) {
            CachedUUIDFetcher.getCache().save(player.getUniqueId(), player.getName());
            consumer.accept(new Profile(player.getName(), player.getUniqueId()));
            return;
        }

        plugin.getCachedUUIDFetcher().fetchProfileAsync(uuid, consumer);
    }

    @Override
    public void fetchProfileAsync(String playerName, Consumer<Profile> consumer) {
        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(playerName);
        if (player != null) {
            CachedUUIDFetcher.getCache().save(player.getUniqueId(), player.getName());
            consumer.accept(new Profile(player.getName(), player.getUniqueId()));
            return;
        }

        plugin.getCachedUUIDFetcher().fetchProfileAsync(playerName, consumer);
    }

    @Deprecated
    @Override
    public void fetchUUIDAsync(String playerName, Consumer<UUID> consumer) {
        fetchProfileAsync(playerName, profile -> consumer.accept(profile.getUuid()));
    }

    @Deprecated
    @Override
    public void fetchNameAsync(UUID uuid, Consumer<String> consumer) {
        fetchProfileAsync(uuid, profile -> consumer.accept(profile.getPlayerName()));
    }

    @Deprecated
    @Override
    public void getNameCaseSensitive(String name, Consumer<String> consumer) {
        fetchUUIDAsync(name, uuid -> fetchNameAsync(uuid, consumer));
    }

}
