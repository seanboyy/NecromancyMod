package io.github.seanboyy.seans_necromancy.network.packet;

import io.github.seanboyy.seans_necromancy.network.IPacketId;
import io.github.seanboyy.seans_necromancy.network.Network;
import io.github.seanboyy.seans_necromancy.network.PacketIdServer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;

public class PacketCorpseBuilderUpdateRequest extends ModPacket {
    public final int zombieStage;
    public final int skeletonStage;
    public final BlockPos position;

    public PacketCorpseBuilderUpdateRequest(final int skeletonStage, final int zombieStage, final BlockPos position) {
        this.skeletonStage = skeletonStage;
        this.zombieStage = zombieStage;
        this.position = position;
    }

    @Override
    protected IPacketId getPacketId() {
        return PacketIdServer.CORPSE_BUILDER_UPDATE_REQUEST;
    }

    @Override
    protected void writePacketData(PacketBuffer packetBuffer) {
        packetBuffer.writeInt(this.skeletonStage);
        packetBuffer.writeInt(this.zombieStage);
        packetBuffer.writeBlockPos(this.position);
    }

    public static void readPacketData(PacketBuffer buf, PlayerEntity player) {
        int skeletonStage = buf.readInt();
        int zombieStage = buf.readInt();
        BlockPos position = buf.readBlockPos();
        if(player instanceof ServerPlayerEntity) {
            PacketCorpseBuilderUpdate packetCorpseBuilderUpdate = new PacketCorpseBuilderUpdate(skeletonStage, zombieStage, position);
            Network.sendPacketToClient(packetCorpseBuilderUpdate, (ServerPlayerEntity) player);
        }
    }
}
