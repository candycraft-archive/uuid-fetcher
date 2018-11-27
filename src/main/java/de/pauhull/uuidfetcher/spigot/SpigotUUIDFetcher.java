package de.pauhull.uuidfetcher.spigot;

import cloud.timo.TimoCloud.api.TimoCloudAPI;
import cloud.timo.TimoCloud.api.messages.listeners.MessageListener;
import cloud.timo.TimoCloud.api.messages.objects.AddressedPluginMessage;
import cloud.timo.TimoCloud.api.messages.objects.PluginMessage;
import de.pauhull.uuidfetcher.common.communication.message.NameRequestMessage;
import de.pauhull.uuidfetcher.common.communication.message.ResponseMessage;
import de.pauhull.uuidfetcher.common.communication.message.UUIDRequestMessage;
import de.pauhull.uuidfetcher.common.fetcher.UUIDFetcher;
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
    private Map<String, List<Consumer<UUID>>> requestedUUIDs = new HashMap<>();

    @Getter
    private Map<UUID, List<Consumer<String>>> requestedNames = new HashMap<>();

    public SpigotUUIDFetcher(JavaPlugin plugin) {
        this.plugin = plugin;
        TimoCloudAPI.getMessageAPI().registerMessageListener(this);
    }

    @Override
    public void fetchUUIDAsync(String playerName, Consumer<UUID> consumer) {

        Player player = Bukkit.getPlayer(playerName);
        if (player != null) {
            consumer.accept(player.getUniqueId());
            return;
        }

        UUIDRequestMessage requestMessage = new UUIDRequestMessage(playerName);
        requestMessage.sendToProxy("Proxy");

        if (requestedUUIDs.containsKey(playerName.toUpperCase())) {
            requestedUUIDs.get(playerName.toUpperCase()).add(consumer);
        } else {
            List<Consumer<UUID>> consumers = new ArrayList<>();
            consumers.add(consumer);
            requestedUUIDs.put(playerName.toUpperCase(), consumers);
        }
    }

    @Override
    public void fetchNameAsync(UUID uuid, Consumer<String> consumer) {

        Player player = Bukkit.getPlayer(uuid);
        if (player != null) {
            consumer.accept(player.getName());
            return;
        }

        NameRequestMessage requestMessage = new NameRequestMessage(uuid);
        requestMessage.sendToProxy("Proxy");

        if (requestedNames.containsKey(uuid)) {
            requestedNames.get(uuid).add(consumer);
        } else {
            List<Consumer<String>> consumers = new ArrayList<>();
            consumers.add(consumer);
            requestedNames.put(uuid, consumers);
        }
    }

    @Override
    public void getNameCaseSensitive(String name, Consumer<String> consumer) {
        fetchUUIDAsync(name, uuid -> fetchNameAsync(uuid, consumer));
    }

    @Override
    public void onPluginMessage(AddressedPluginMessage addressedPluginMessage) {
        PluginMessage message = addressedPluginMessage.getMessage();

        if (message.getType().equals(ResponseMessage.TYPE)) {

            ResponseMessage responseMessage = new ResponseMessage(message);
            String player = responseMessage.getPlayerName();
            UUID uuid = responseMessage.getUuid();

            if (player != null && requestedUUIDs.containsKey(player.toUpperCase())) {
                for (Consumer<UUID> consumer : requestedUUIDs.get(player.toUpperCase())) {
                    consumer.accept(uuid);
                }

                requestedUUIDs.remove(player.toUpperCase());
            }

            if (uuid != null && requestedNames.containsKey(uuid)) {
                for (Consumer<String> consumer : requestedNames.get(uuid)) {
                    consumer.accept(player);
                }

                requestedNames.remove(uuid);
            }

        }
    }

}
