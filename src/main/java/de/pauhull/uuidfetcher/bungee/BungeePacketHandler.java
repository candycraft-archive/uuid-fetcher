package de.pauhull.uuidfetcher.bungee;

import com.ikeirnez.pluginmessageframework.PacketHandler;
import com.ikeirnez.pluginmessageframework.PacketListener;
import de.pauhull.uuidfetcher.common.communication.packet.*;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

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
                plugin.getPacketManager().sendPacket(packet.getSender(), new ResponsePacket(uuid, name));
            });
        });
    }

    @PacketHandler
    public void onNameRequest(NameRequestPacket packet) {
        plugin.getCachedUUIDFetcher().fetchNameAsync(packet.getUuid(), name -> {
            plugin.getPacketManager().sendPacket(packet.getSender(), new ResponsePacket(packet.getUuid(), name));
        });
    }

    @PacketHandler
    public void onOnlineRequest(OnlineRequestPacket packet) {
        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(packet.getPlayer());
        plugin.getPacketManager().sendPacket(packet.getSender(), new OnlineResponsePacket(packet.getPlayer(), player != null));
    }

}
