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
public class NameRequestPacket extends StandardPacket {

    @Getter
    private UUID uuid;

    public NameRequestPacket() {
        this(UUID.randomUUID());
    }

    public NameRequestPacket(UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public void handle(DataInputStream dataInputStream) throws IOException {
        this.uuid = UUID.fromString(dataInputStream.readUTF());
    }

    @Override
    public PacketWriter write() throws IOException {
        PacketWriter writer = new PacketWriter(this);
        writer.writeUTF(uuid.toString());
        return writer;
    }

}
