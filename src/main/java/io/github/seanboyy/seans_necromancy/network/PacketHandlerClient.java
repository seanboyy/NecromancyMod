package io.github.seanboyy.seans_necromancy.network;

import io.github.seanboyy.seans_necromancy.Necromancy;
import io.github.seanboyy.seans_necromancy.network.packet.IPacketHandler;
import io.github.seanboyy.seans_necromancy.network.packet.PacketCorpseBuilderUpdate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.EnumMap;

public class PacketHandlerClient {
    public final EnumMap<PacketIdClient, IPacketHandler> clientHandlers = new EnumMap<>(PacketIdClient.class);

    public PacketHandlerClient() {
        clientHandlers.put(PacketIdClient.CORPSE_BUILDER_UPDATE, PacketCorpseBuilderUpdate::readPacketData);
    }

    public void onPacket(NetworkEvent.ServerCustomPayloadEvent event) {
        try {
            PacketBuffer packetBuffer = new PacketBuffer(event.getPayload());
            int packetIdOrdinal = packetBuffer.readByte();
            PacketIdClient packetId = PacketIdClient.VALUES[packetIdOrdinal];
            IPacketHandler packetHandler = this.clientHandlers.get(packetId);
            Minecraft minecraft = Minecraft.getInstance();
            ClientPlayerEntity clientPlayerEntity = minecraft.player;
            if(clientPlayerEntity != null) {
                packetHandler.readPacketData(packetBuffer, clientPlayerEntity);
            }
        } catch (Exception e) {
            Necromancy.LOGGER.error("Packet error", e);
        }
        event.getSource().get().setPacketHandled(true);
    }
}
