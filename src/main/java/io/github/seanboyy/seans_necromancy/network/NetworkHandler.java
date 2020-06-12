package io.github.seanboyy.seans_necromancy.network;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.event.EventNetworkChannel;

public class NetworkHandler {
    private final EventNetworkChannel CHANNEL;

    public NetworkHandler() {
        this.CHANNEL = NetworkRegistry.newEventChannel(PacketHandler.CHANNEL_ID, () -> "1.0.0", s -> true, s -> true);
    }

    public void createServerPacketHandler() {
        PacketHandler packetHandler = new PacketHandler();
        this.CHANNEL.addListener(packetHandler::onPacket);
    }

    @OnlyIn(Dist.CLIENT)
    public void createClientPacketHandler() {
        PacketHandlerClient packetHandler = new PacketHandlerClient();
        this.CHANNEL.addListener(packetHandler::onPacket);
    }
}
