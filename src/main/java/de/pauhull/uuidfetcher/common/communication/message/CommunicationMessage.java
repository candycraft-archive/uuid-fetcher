package de.pauhull.uuidfetcher.common.communication.message;

import cloud.timo.TimoCloud.api.TimoCloudAPI;
import cloud.timo.TimoCloud.api.messages.objects.PluginMessage;

/**
 * Created by Paul
 * on 26.11.2018
 *
 * @author pauhull
 */
public abstract class CommunicationMessage extends PluginMessage {

    public CommunicationMessage(String type) {
        super(type);
    }

    public void sendToServer(String server) {
        TimoCloudAPI.getMessageAPI().sendMessageToServer(this, server);
    }

    public void sendToProxy(String proxy) {
        TimoCloudAPI.getMessageAPI().sendMessageToProxy(this, proxy);
    }

}
