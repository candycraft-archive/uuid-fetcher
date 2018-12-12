package de.pauhull.uuidfetcher.spigot;

import cloud.timo.TimoCloud.api.TimoCloudAPI;
import cloud.timo.TimoCloud.api.messages.listeners.MessageListener;
import cloud.timo.TimoCloud.api.messages.objects.AddressedPluginMessage;
import cloud.timo.TimoCloud.api.messages.objects.PluginMessage;
import de.pauhull.uuidfetcher.common.communication.message.NameRequestMessage;
import de.pauhull.uuidfetcher.common.communication.message.ResponseMessage;
import de.pauhull.uuidfetcher.common.communication.message.UUIDRequestMessage;
import de.pauhull.uuidfetcher.common.fetcher.UUIDFetcher;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.function.Consumer;

/**
 * Created by Paul
 * on 23.11.2018
 *
 * @author pauhull
 */
public class SpigotUUIDFetcher implements MessageListener, UUIDFetcher {

    @Getter
    private JavaPlugin plugin;

    @Getter
    private Map<String, List<Consumer<Profile>>> requestedProfilesByName = new HashMap<>();

    @Getter
    private Map<UUID, List<Consumer<Profile>>> requestedProfilesByUUID = new HashMap<>();

    public SpigotUUIDFetcher(JavaPlugin plugin) {
        this.plugin = plugin;
        TimoCloudAPI.getMessageAPI().registerMessageListener(this);
    }

    public void fetchProfileAsync(String playerName, Consumer<Profile> consumer) {

        Player player = Bukkit.getPlayer(playerName);
        if (player != null) {
            consumer.accept(new Profile(player.getName(), player.getUniqueId()));
            return;
        }

        UUIDRequestMessage requestMessage = new UUIDRequestMessage(playerName);
        requestMessage.sendToProxy("Proxy");

        if (requestedProfilesByName.containsKey(playerName.toUpperCase())) {
            requestedProfilesByName.get(playerName.toUpperCase()).add(consumer);
        } else {
            List<Consumer<Profile>> consumers = new ArrayList<>();
            consumers.add(consumer);
            requestedProfilesByName.put(playerName.toUpperCase(), consumers);
        }
    }

    public void fetchProfileAsync(UUID uuid, Consumer<Profile> consumer) {

        Player player = Bukkit.getPlayer(uuid);
        if (player != null) {
            consumer.accept(new Profile(player.getName(), player.getUniqueId()));
            return;
        }

        NameRequestMessage requestMessage = new NameRequestMessage(uuid);
        requestMessage.sendToProxy("Proxy");

        if (requestedProfilesByUUID.containsKey(uuid)) {
            requestedProfilesByUUID.get(uuid).add(consumer);
        } else {
            List<Consumer<Profile>> consumers = new ArrayList<>();
            consumers.add(consumer);
            requestedProfilesByUUID.put(uuid, consumers);
        }
    }

    @Deprecated
    @Override
    public void fetchUUIDAsync(String playerName, Consumer<UUID> consumer) {
        fetchProfileAsync(playerName, profile -> consumer.accept(profile.getUuid()));
    }

    @Deprecated
    @Override
    public void fetchNameAsync(UUID uuid, Consumer<String> consumer) {
        fetchProfileAsync(uuid, profile -> consumer.accept(profile.getName()));
    }

    @Deprecated
    @Override
    public void getNameCaseSensitive(String name, Consumer<String> consumer) {
        fetchProfileAsync(name, profile -> {
            consumer.accept(profile.getName());
        });
    }

    @Override
    public void onPluginMessage(AddressedPluginMessage addressedPluginMessage) {
        PluginMessage message = addressedPluginMessage.getMessage();

        if (message.getType().equals(ResponseMessage.TYPE)) {

            ResponseMessage responseMessage = new ResponseMessage(message);
            String player = responseMessage.getPlayerName();
            UUID uuid = responseMessage.getUuid();
            Profile profile = new Profile(player, uuid);

            if (player != null && requestedProfilesByName.containsKey(player.toUpperCase())) {
                for (Consumer<Profile> consumer : requestedProfilesByName.get(player.toUpperCase())) {
                    consumer.accept(profile);
                }

                requestedProfilesByName.remove(player.toUpperCase());
            }

            if (uuid != null && requestedProfilesByUUID.containsKey(uuid)) {
                for (Consumer<Profile> consumer : requestedProfilesByUUID.get(uuid)) {
                    consumer.accept(profile);
                }

                requestedProfilesByUUID.remove(uuid);
            }

        }
    }

    @AllArgsConstructor
    public class Profile {

        @Getter
        private String name;

        @Getter
        private UUID uuid;

    }

}
