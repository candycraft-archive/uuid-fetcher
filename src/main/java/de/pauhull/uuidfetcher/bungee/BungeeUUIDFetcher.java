package de.pauhull.uuidfetcher.bungee;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.pauhull.uuidfetcher.common.fetcher.CachedUUIDFetcher;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
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
