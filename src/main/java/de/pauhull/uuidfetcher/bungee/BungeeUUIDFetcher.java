package de.pauhull.uuidfetcher.bungee;

import de.pauhull.uuidfetcher.common.fetcher.CachedUUIDFetcher;

import java.util.UUID;
import java.util.function.Consumer;

/**
 * Created by Paul
 * on 23.11.2018
 *
 * @author pauhull
 */
public class BungeeUUIDFetcher {

    public static UUID parseUUIDFromString(String uuidAsString) {
        return CachedUUIDFetcher.parseUUIDFromString(uuidAsString);
    }

    public void fetchUUIDAsync(String playerName, Consumer<UUID> consumer) {
        UUIDFetcherBungeePlugin.getInstance().getCachedUUIDFetcher().fetchUUIDAsync(playerName, consumer);
    }

    public void fetchNameAsync(UUID uuid, Consumer<String> consumer) {
        UUIDFetcherBungeePlugin.getInstance().getCachedUUIDFetcher().fetchNameAsync(uuid, consumer);
    }

    public void getNameCaseSensitive(String name, Consumer<String> consumer) {
        UUIDFetcherBungeePlugin.getInstance().getCachedUUIDFetcher().getNameCaseSensitive(name, consumer);
    }

}
