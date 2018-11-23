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
public class UUIDRequestPacket extends StandardPacket {

    @Getter
    private String player;

    public UUIDRequestPacket() {
        this("");
    }

    public UUIDRequestPacket(String player) {
        this.player = player;
    }

    @Override
    public void handle(DataInputStream dataInputStream) throws IOException {
        this.player = dataInputStream.readUTF();
    }

    @Override
    public PacketWriter write() throws IOException {
        PacketWriter writer = new PacketWriter(this);
        writer.writeUTF(player);
        return writer;
    }

}
