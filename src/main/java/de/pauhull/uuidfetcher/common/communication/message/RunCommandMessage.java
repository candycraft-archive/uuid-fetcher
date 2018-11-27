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
public class RunCommandMessage extends CommunicationMessage {

    public static final String TYPE = "RUN_COMMAND";

    @Getter
    private String playerName;

    @Getter
    private String command;

    public RunCommandMessage(String playerName, String command) {
        super(TYPE);

        this.playerName = playerName;
        this.command = command;
        this.set("playerName", playerName);
        this.set("command", command);
    }

    public RunCommandMessage(PluginMessage pluginMessage) {
        this(pluginMessage.getString("playerName"), pluginMessage.getString("command"));
    }

}
