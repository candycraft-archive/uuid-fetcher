package de.pauhull.uuidfetcher.common.communication.packet;

import com.ikeirnez.pluginmessageframework.PacketWriter;
import com.ikeirnez.pluginmessageframework.StandardPacket;
import lombok.Getter;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * Created by Paul
 * on 23.11.2018
 *
 * @author pauhull
 */
public class OnlineResponsePacket extends StandardPacket {

    @Getter
    private boolean online;

    @Getter
    private String player;

    public OnlineResponsePacket() {
        this(null, false);
    }

    public OnlineResponsePacket(String player, boolean online) {
        this.player = player;
        this.online = online;
    }

    @Override
    public void handle(DataInputStream dataInputStream) throws IOException {
        String[] input = dataInputStream.readUTF().split(", ");
        this.player = input[0];
        this.online = Boolean.valueOf(input[1]);
    }

    @Override
    public PacketWriter write() throws IOException {
        PacketWriter writer = new PacketWriter(this);
        writer.writeUTF(player + ", " + online);
        return writer;
    }

}
