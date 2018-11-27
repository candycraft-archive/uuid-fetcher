package de.pauhull.uuidfetcher.common.communication.message;

import cloud.timo.TimoCloud.api.TimoCloudAPI;
import cloud.timo.TimoCloud.api.messages.objects.PluginMessage;
import lombok.Getter;

import java.util.UUID;

/**
 * Created by Paul
 * on 26.11.2018
 *
 * @author pauhull
 */
public class ResponseMessage extends CommunicationMessage {

    public static final String TYPE = "RESPONSE";

    @Getter
    private String playerName;

    @Getter
    private UUID uuid;

    public ResponseMessage(String playerName, UUID uuid) {
        super(TYPE);

        this.playerName = playerName;
        this.uuid = uuid;
        this.set("playerName", playerName != null ? playerName : "null");
        this.set("uuid", uuid != null ? uuid.toString() : "null");
    }

    public ResponseMessage(PluginMessage pluginMessage) {
        this(pluginMessage.getString("playerName"),
                pluginMessage.getString("uuid") != null ? UUID.fromString(pluginMessage.getString("uuid")) : null);
    }

}
