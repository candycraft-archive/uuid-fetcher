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
public class UUIDRequestMessage extends CommunicationMessage {

    public static final String TYPE = "UUID_REQUEST";

    @Getter
    private String playerName;

    public UUIDRequestMessage(String playerName) {
        super(TYPE);

        this.playerName = playerName;
        this.set("playerName", playerName);
    }

    public UUIDRequestMessage(PluginMessage pluginMessage) {
        this(pluginMessage.getString("playerName"));
    }

}
