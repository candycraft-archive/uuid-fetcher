package de.pauhull.uuidfetcher.spigot;

import com.ikeirnez.pluginmessageframework.PacketHandler;
import com.ikeirnez.pluginmessageframework.PacketListener;
import com.ikeirnez.pluginmessageframework.PacketManager;
import com.ikeirnez.pluginmessageframework.implementations.BukkitPacketManager;
import de.pauhull.uuidfetcher.common.communication.packet.NameRequestPacket;
import de.pauhull.uuidfetcher.common.communication.packet.ReturnPacket;
import de.pauhull.uuidfetcher.common.communication.packet.UUIDRequestPacket;
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
public class SpigotUUIDFetcher implements PacketListener {

    @Getter
    private JavaPlugin plugin;

    @Getter
    private PacketManager packetManager;

    @Getter
    private Map<String, List<Consumer<UUID>>> requestedUUIDs = new HashMap<>();

    @Getter
    private Map<UUID, List<Consumer<String>>> requestedNames = new HashMap<>();

    public SpigotUUIDFetcher(JavaPlugin plugin) {
        this.plugin = plugin;
        this.packetManager = new BukkitPacketManager(plugin, "UUIDFetcherChannel");
        this.packetManager.registerListener(this);
    }

    @PacketHandler
    public void onReturn(ReturnPacket packet) {
        String player = packet.getPlayer();
        UUID uuid = packet.getUuid();

        if (requestedUUIDs.containsKey(player.toUpperCase())) {
            for (Consumer<UUID> consumer : requestedUUIDs.get(player.toUpperCase())) {
                consumer.accept(uuid);
            }

            requestedUUIDs.remove(player.toUpperCase());
        }

        if (requestedNames.containsKey(uuid)) {
            for (Consumer<String> consumer : requestedNames.get(uuid)) {
                consumer.accept(player);
            }

            requestedNames.remove(uuid);
        }

    }

    public void fetchUUIDAsync(String playerName, Consumer<UUID> consumer) {

        Player player = Bukkit.getPlayer(playerName);
        if (player != null) {
            consumer.accept(player.getUniqueId());
            return;
        }

        packetManager.sendPacket(new UUIDRequestPacket(playerName));

        if (requestedUUIDs.containsKey(playerName.toUpperCase())) {
            requestedUUIDs.get(playerName.toUpperCase()).add(consumer);
        } else {
            List<Consumer<UUID>> consumers = new ArrayList<>();
            consumers.add(consumer);
            requestedUUIDs.put(playerName.toUpperCase(), consumers);
        }
    }

    public void fetchNameAsync(UUID uuid, Consumer<String> consumer) {

        Player player = Bukkit.getPlayer(uuid);
        if (player != null) {
            consumer.accept(player.getName());
            return;
        }

        packetManager.sendPacket(new NameRequestPacket(uuid));

        if (requestedNames.containsKey(uuid)) {
            requestedNames.get(uuid).add(consumer);
        } else {
            List<Consumer<String>> consumers = new ArrayList<>();
            consumers.add(consumer);
            requestedNames.put(uuid, consumers);
        }
    }

    public void getNameCaseSensitive(String name, Consumer<String> consumer) {
        fetchUUIDAsync(name, uuid -> fetchNameAsync(uuid, consumer));
    }

}
