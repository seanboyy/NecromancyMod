package io.github.seanboyy.seans_necromancy.objects.container.inventory;

import io.github.seanboyy.seans_necromancy.registry.ModBlocks;
import io.github.seanboyy.seans_necromancy.registry.ModContainers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IWorldPosCallable;

import javax.annotation.Nullable;

public class RitualTableContainer extends Container {
    private final IWorldPosCallable worldPosCallable;

    public RitualTableContainer(final int windowId, final PlayerInventory playerInventory, final IWorldPosCallable worldPosCallable){
        super(ModContainers.RITUAL_TABLE_CONTAINER.get(), windowId);
        this.worldPosCallable = worldPosCallable;
    }

    public RitualTableContainer(final int id, final PlayerInventory playerInventory, final PacketBuffer packetBuffer) {
        this(id, playerInventory, IWorldPosCallable.of(playerInventory.player.world, playerInventory.player.getPosition()));
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return isWithinUsableDistance(worldPosCallable, playerIn, ModBlocks.RITUAL_TABLE.get());
    }
}
