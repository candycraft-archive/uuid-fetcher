package de.pauhull.uuidfetcher.bungee;

import com.ikeirnez.pluginmessageframework.PacketHandler;
import com.ikeirnez.pluginmessageframework.PacketListener;
import de.pauhull.uuidfetcher.common.communication.packet.NameRequestPacket;
import de.pauhull.uuidfetcher.common.communication.packet.UUIDRequestPacket;
import de.pauhull.uuidfetcher.common.communication.packet.ReturnPacket;

/**
 * Created by Paul
 * on 23.11.2018
 *
 * @author pauhull
 */
public class BungeePacketHandler implements PacketListener {

    private UUIDFetcherBungeePlugin plugin;

    public BungeePacketHandler(UUIDFetcherBungeePlugin plugin) {
        this.plugin = plugin;
    }

    @PacketHandler
    public void onUUIDRequest(UUIDRequestPacket packet) {
        plugin.getCachedUUIDFetcher().fetchUUIDAsync(packet.getPlayer(), uuid -> {
            plugin.getCachedUUIDFetcher().getNameCaseSensitive(packet.getPlayer(), name -> {
                plugin.getPacketManager().sendPacket(packet.getSender(), new ReturnPacket(uuid, name));
            });
        });
    }

    @PacketHandler
    public void onNameRequest(NameRequestPacket packet) {
        plugin.getCachedUUIDFetcher().fetchNameAsync(packet.getUuid(), name -> {
            plugin.getPacketManager().sendPacket(packet.getSender(), new ReturnPacket(packet.getUuid(), name));
        });
    }

}
