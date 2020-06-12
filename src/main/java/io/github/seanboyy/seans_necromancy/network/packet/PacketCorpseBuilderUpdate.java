package io.github.seanboyy.seans_necromancy.network.packet;

import io.github.seanboyy.seans_necromancy.network.IPacketId;
import io.github.seanboyy.seans_necromancy.network.PacketIdClient;
import io.github.seanboyy.seans_necromancy.objects.blocks.CorpseBuilderBlock;
import io.github.seanboyy.seans_necromancy.objects.tileentities.CorpseBuilderTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PacketCorpseBuilderUpdate extends ModPacket {
    public final int zombieStage;
    public final int skeletonStage;
    public final BlockPos position;

    public PacketCorpseBuilderUpdate(int skeletonStage, int zombieStage, BlockPos position) {
        this.skeletonStage = skeletonStage;
        this.zombieStage = zombieStage;
        this.position = position;
    }

    @Override
    protected IPacketId getPacketId() {
        return PacketIdClient.CORPSE_BUILDER_UPDATE;
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
        World world = player.getEntityWorld();
        if(world instanceof ClientWorld) {
            if(world.getTileEntity(position) instanceof CorpseBuilderTileEntity) {
                BlockState state = world.getBlockState(position);
                world.setBlockState(position, state.with(CorpseBuilderBlock.SKELETON_LEVEL,  skeletonStage).with(CorpseBuilderBlock.FLESH_LEVEL, zombieStage));
            }
        }
    }
}
