package de.pauhull.uuidfetcher.bungee;

import cloud.timo.TimoCloud.api.TimoCloudAPI;
import cloud.timo.TimoCloud.api.messages.listeners.MessageListener;
import cloud.timo.TimoCloud.api.messages.objects.AddressedPluginMessage;
import cloud.timo.TimoCloud.api.messages.objects.PluginMessage;
import de.pauhull.uuidfetcher.common.communication.message.*;
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

            plugin.getCachedUUIDFetcher().fetchProfileAsync(requestMessage.getPlayerName(), profile -> {

                if (profile == null || profile.getUuid() == null) {
                    ResponseMessage response = new ResponseMessage(requestMessage.getPlayerName(), null);
                    response.sendToServer(addressedPluginMessage.getSender().getName());
                    return;
                }

                ResponseMessage response = new ResponseMessage(profile.getPlayerName(), profile.getUuid());
                response.sendToServer(addressedPluginMessage.getSender().getName());
            });
        } else if (message.getType().equals(NameRequestMessage.TYPE)) {
            NameRequestMessage requestMessage = new NameRequestMessage(message);

            plugin.getCachedUUIDFetcher().fetchProfileAsync(requestMessage.getUuid(), profile -> {
                ResponseMessage response = new ResponseMessage(profile.getPlayerName(), requestMessage.getUuid());
                response.sendToServer(addressedPluginMessage.getSender().getName());
            });
        } else if (message.getType().equals(ConnectMessage.TYPE)) {
            ConnectMessage connectMessage = new ConnectMessage(message);

            ProxiedPlayer player = ProxyServer.getInstance().getPlayer(connectMessage.getPlayerName());
            ServerInfo server = ProxyServer.getInstance().getServerInfo(connectMessage.getServer());

            if (player != null && server != null) {
                player.connect(server);
            }
        } else if(message.getType().equals(RunCommandMessage.TYPE)) {
            RunCommandMessage runCommandMessage = new RunCommandMessage(message);

            ProxiedPlayer player = ProxyServer.getInstance().getPlayer(runCommandMessage.getPlayerName());
            String command = runCommandMessage.getCommand();

            if (player != null) {
                ProxyServer.getInstance().getPluginManager().dispatchCommand(player, command);
            }
        }
    }

}
