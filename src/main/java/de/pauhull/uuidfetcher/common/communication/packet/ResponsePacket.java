package de.pauhull.uuidfetcher.common.communication.packet;

import com.ikeirnez.pluginmessageframework.PacketWriter;
import com.ikeirnez.pluginmessageframework.StandardPacket;
import lombok.Getter;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by Paul
 * on 23.11.2018
 *
 * @author pauhull
 */
public class ResponsePacket extends StandardPacket {

    @Getter
    private UUID uuid;

    @Getter
    private String player;

    public ResponsePacket() {
        this(UUID.randomUUID(), "");
    }

    public ResponsePacket(UUID uuid, String player) {
        this.uuid = uuid;
        this.player = player;
    }

    @Override
    public void handle(DataInputStream dataInputStream) throws IOException {
        String[] input = dataInputStream.readUTF().split(", ");
        this.uuid = UUID.fromString(input[0]);
        this.player = input[1];
    }

    @Override
    public PacketWriter write() throws IOException {
        PacketWriter writer = new PacketWriter(this);
        writer.writeUTF(uuid.toString() + ", " + player);
        return writer;
    }

}
