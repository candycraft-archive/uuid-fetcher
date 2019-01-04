package de.pauhull.uuidfetcher.common.communication.message;

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
        this(pluginMessage.getString("playerName"), fetchUUID(pluginMessage.getString("uuid")));
    }

    private static UUID fetchUUID(String s) {
        if (s == null) return null;
        if (s.equals("null")) return null;
        return UUID.fromString(s);
    }

}
