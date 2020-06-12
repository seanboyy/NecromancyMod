package io.github.seanboyy.seans_necromancy.network;

import io.github.seanboyy.seans_necromancy.Necromancy;
import io.github.seanboyy.seans_necromancy.network.packet.IPacketHandler;
import io.github.seanboyy.seans_necromancy.network.packet.PacketCorpseBuilderUpdateRequest;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.EnumMap;

public class PacketHandler {
    public static final ResourceLocation CHANNEL_ID = new ResourceLocation(Necromancy.MOD_ID, "channel");

    public final EnumMap<PacketIdServer, IPacketHandler> serverHandlers = new EnumMap<>(PacketIdServer.class);

    public PacketHandler() {
        serverHandlers.put(PacketIdServer.CORPSE_BUILDER_UPDATE_REQUEST, PacketCorpseBuilderUpdateRequest::readPacketData);
    }

    public void onPacket(NetworkEvent.ClientCustomPayloadEvent event) {
        PacketBuffer packetBuffer = new PacketBuffer(event.getPayload());
        NetworkEvent.Context context = event.getSource().get();
        ServerPlayerEntity player = context.getSender();
        if(player == null) {
            Necromancy.LOGGER.error("Packet error: The sending player is missing for event {}", event);
            return;
        }
        try {
            int packetIdOrdinal = packetBuffer.readByte();
            PacketIdServer packetId = PacketIdServer.VALUES[packetIdOrdinal];
            IPacketHandler packetHandler = this.serverHandlers.get(packetId);
            packetHandler.readPacketData(packetBuffer, player);
        } catch (RuntimeException e) {
            Necromancy.LOGGER.error("Packet error for event {} ", event, e);
        }
        event.getSource().get().setPacketHandled(true);
    }
}
