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
public class NameRequestMessage extends CommunicationMessage {

    public static final String TYPE = "NAME_REQUEST";

    @Getter
    private UUID uuid;

    public NameRequestMessage(UUID uuid) {
        super(TYPE);

        this.uuid = uuid;
        this.set("uuid", uuid.toString());
    }

    public NameRequestMessage(PluginMessage pluginMessage) {
        this(fetchUUID(pluginMessage.getString("uuid")));
    }

    private static UUID fetchUUID(String s) {
        if (s == null) return null;
        if (s.equals("null")) return null;
        return UUID.fromString(s);
    }

}
