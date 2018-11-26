package de.pauhull.uuidfetcher.common.communication.message;

import cloud.timo.TimoCloud.api.TimoCloudAPI;
import cloud.timo.TimoCloud.api.messages.objects.PluginMessage;
import lombok.Getter;

/**
 * Created by Paul
 * on 26.11.2018
 *
 * @author pauhull
 */
public class ConnectMessage extends PluginMessage {

    public static final String TYPE = "CONNECT";

    @Getter
    private String playerName;

    @Getter
    private String server;

    public ConnectMessage(String playerName, String server) {
        super(TYPE);

        this.playerName = playerName;
        this.server = server;
        this.set("playerName", playerName);
        this.set("server", server);
    }

    public ConnectMessage(PluginMessage pluginMessage) {
        this(pluginMessage.getString("playerName"), pluginMessage.getString("server"));
    }

    public void send(String server) {
        TimoCloudAPI.getMessageAPI().sendMessageToServer(this, server);
    }

}
