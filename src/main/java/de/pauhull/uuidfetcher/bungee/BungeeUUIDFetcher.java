package de.pauhull.uuidfetcher.bungee;

import de.pauhull.uuidfetcher.common.fetcher.CachedUUIDFetcher;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Created by Paul
 * on 23.11.2018
 *
 * @author pauhull
 */
public class BungeeUUIDFetcher {

    private static UUIDFetcherBungeePlugin plugin = UUIDFetcherBungeePlugin.getInstance();

    public void fetchProfileAsync(UUID uuid, BiConsumer<String, UUID> consumer) {
        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(uuid);
        if (player != null) {
            CachedUUIDFetcher.getCache().save(player.getUniqueId(), player.getName());
            consumer.accept(player.getName(), player.getUniqueId());
            return;
        }

        plugin.getCachedUUIDFetcher().fetchProfileAsync(uuid, profile -> {
            consumer.accept(profile.getPlayerName(), profile.getUuid());
        });
    }

    public void fetchProfileAsync(String playerName, BiConsumer<String, UUID> consumer) {
        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(playerName);
        if (player != null) {
            CachedUUIDFetcher.getCache().save(player.getUniqueId(), player.getName());
            consumer.accept(player.getName(), player.getUniqueId());
            return;
        }

        plugin.getCachedUUIDFetcher().fetchProfileAsync(playerName, profile -> {
            consumer.accept(profile.getPlayerName(), profile.getUuid());
        });
    }

    @Deprecated
    public void fetchUUIDAsync(String playerName, Consumer<UUID> consumer) {
        fetchProfileAsync(playerName, (ignored, uuid) -> consumer.accept(uuid));
    }

    @Deprecated
    public void fetchNameAsync(UUID uuid, Consumer<String> consumer) {
        fetchProfileAsync(uuid, (name, ignored) -> consumer.accept(name));
    }

    @Deprecated
    public void getNameCaseSensitive(String name, Consumer<String> consumer) {
        fetchUUIDAsync(name, uuid -> fetchNameAsync(uuid, consumer));
    }

}
