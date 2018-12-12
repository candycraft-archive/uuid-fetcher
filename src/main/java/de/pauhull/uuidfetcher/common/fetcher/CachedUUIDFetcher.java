package de.pauhull.uuidfetcher.common.fetcher;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.Getter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

public class CachedUUIDFetcher implements UUIDFetcher {

    @Getter
    protected static UUIDCache cache = new UUIDCache();

    @Getter
    protected ExecutorService executor;

    public CachedUUIDFetcher(ExecutorService executor) {
        this.executor = executor;
    }

    @Override
    public void fetchProfileAsync(String playerName, Consumer<Profile> consumer) {

        executor.execute(() -> {

            UUID uuid = cache.getUUID(playerName);
            if (uuid != null) {
                consumer.accept(new Profile(playerName, uuid));
                return;
            }

            try {
                // Get response from Mojang API
                URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + playerName);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                if (connection.getResponseCode() != 200) {
                    consumer.accept(null);
                    return;
                }

                InputStream inputStream = connection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                // Parse JSON response and get UUID
                JsonElement element = new JsonParser().parse(bufferedReader);
                JsonObject object = element.getAsJsonObject();
                String uuidAsString = object.get("id").getAsString();
                String retrievedName = object.get("name").getAsString();

                inputStream.close();
                bufferedReader.close();

                // Return UUID
                UUID result = UUIDFetcher.parseUUIDFromString(uuidAsString);
                cache.save(result, retrievedName);
                consumer.accept(new Profile(retrievedName, result));
            } catch (IOException e) {
                e.printStackTrace();
                consumer.accept(null);
            }
        });
    }

    @Override
    public void fetchProfileAsync(UUID uuid, Consumer<Profile> consumer) {

        executor.execute(() -> {

            String playerName = cache.getName(uuid);
            if (playerName != null) {
                consumer.accept(new Profile(playerName, uuid));
                return;
            }

            try {
                // Get response from Mojang API
                URL url = new URL(String.format("https://api.mojang.com/user/profiles/%s/names", uuid.toString().replace("-", "")));
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                if (connection.getResponseCode() != 200) {
                    consumer.accept(null);
                    return;
                }

                InputStream inputStream = connection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                // Parse JSON response and return name
                JsonElement element = new JsonParser().parse(bufferedReader);
                JsonArray array = element.getAsJsonArray();
                JsonObject object = array.get(array.size() - 1).getAsJsonObject();

                bufferedReader.close();
                inputStream.close();

                String result = object.get("name").getAsString();
                cache.save(uuid, result);
                consumer.accept(new Profile(result, uuid));
            } catch (IOException e) {
                e.printStackTrace();
                consumer.accept(null);
            }
        });
    }

    @Deprecated
    @Override
    public void fetchNameAsync(UUID uuid, Consumer<String> consumer) {
        fetchProfileAsync(uuid, profile -> consumer.accept(profile.getPlayerName()));
    }

    @Deprecated
    @Override
    public void fetchUUIDAsync(String playerName, Consumer<UUID> consumer) {
        fetchProfileAsync(playerName, profile -> consumer.accept(profile.getUuid()));
    }

    @Deprecated
    @Override
    public void getNameCaseSensitive(String name, Consumer<String> consumer) {

        fetchUUIDAsync(name, uuid -> {
            if (uuid == null) {
                consumer.accept(null);
                return;
            }

            fetchNameAsync(uuid, consumer);
        });
    }

}