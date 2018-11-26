package de.pauhull.uuidfetcher.bungee;

import cloud.timo.TimoCloud.api.TimoCloudAPI;
import cloud.timo.TimoCloud.api.messages.listeners.MessageListener;
import cloud.timo.TimoCloud.api.messages.objects.AddressedPluginMessage;
import cloud.timo.TimoCloud.api.messages.objects.PluginMessage;
import de.pauhull.uuidfetcher.common.communication.message.ConnectMessage;
import de.pauhull.uuidfetcher.common.communication.message.NameRequestMessage;
import de.pauhull.uuidfetcher.common.communication.message.ResponseMessage;
import de.pauhull.uuidfetcher.common.communication.message.UUIDRequestMessage;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * Created by Paul
 * on 26.11.2018
 *
 * @author pauhull
 */
public class BungeeMessageListener implements MessageListener {

    private UUIDFetcherBungeePlugin plugin;

    public BungeeMessageListener(UUIDFetcherBungeePlugin plugin) {
        this.plugin = plugin;
        TimoCloudAPI.getMessageAPI().registerMessageListener(this);
    }

    @Override
    public void onPluginMessage(AddressedPluginMessage addressedPluginMessage) {
        PluginMessage message = addressedPluginMessage.getMessage();

        if (message.getType().equals(UUIDRequestMessage.TYPE)) {
            UUIDRequestMessage requestMessage = new UUIDRequestMessage(message);

            plugin.getCachedUUIDFetcher().fetchUUIDAsync(requestMessage.getPlayerName(), uuid -> {
                plugin.getCachedUUIDFetcher().fetchNameAsync(uuid, name -> {
                    ResponseMessage response = new ResponseMessage(name, uuid);
                    response.send(addressedPluginMessage.getSender().getName());
                });
            });
        } else if (message.getType().equals(NameRequestMessage.TYPE)) {
            NameRequestMessage requestMessage = new NameRequestMessage(message);

            plugin.getCachedUUIDFetcher().fetchNameAsync(requestMessage.getUuid(), name -> {
                ResponseMessage response = new ResponseMessage(name, requestMessage.getUuid());
                response.send(addressedPluginMessage.getSender().getName());
            });
        } else if (message.getType().equals(ConnectMessage.TYPE)) {
            ConnectMessage connectMessage = new ConnectMessage(message);

            ProxiedPlayer player = ProxyServer.getInstance().getPlayer(connectMessage.getPlayerName());
            ServerInfo server = ProxyServer.getInstance().getServerInfo(connectMessage.getServer());

            if (player != null && server != null) {
                player.connect(server);
            }
        }
    }

}
