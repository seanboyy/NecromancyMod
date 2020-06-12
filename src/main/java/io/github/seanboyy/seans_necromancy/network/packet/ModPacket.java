package io.github.seanboyy.seans_necromancy.network.packet;

import io.github.seanboyy.seans_necromancy.network.IPacketId;
import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketBuffer;
import org.apache.commons.lang3.tuple.Pair;

public abstract class ModPacket {
    public final Pair<PacketBuffer, Integer> getPacketData() {
        IPacketId packetId = getPacketId();
        int packetIdOrdinal = packetId.ordinal();
        PacketBuffer packetBuffer = new PacketBuffer(Unpooled.buffer());
        packetBuffer.writeByte(packetIdOrdinal);
        writePacketData(packetBuffer);
        return Pair.of(packetBuffer, packetIdOrdinal);
    }

    protected abstract IPacketId getPacketId();

    protected abstract void writePacketData(PacketBuffer packetBuffer);
}
