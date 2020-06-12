package io.github.seanboyy.seans_necromancy.container.inventory;

import io.github.seanboyy.seans_necromancy.registry.ModContainers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;
import net.minecraft.util.IntReferenceHolder;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

public class ChargerContainer extends Container {
    private final IInventory tileCharger;
    private final IntReferenceHolder data;

    public ChargerContainer(final int windowId, final PlayerInventory playerInventory) {
        this(windowId, playerInventory, new Inventory(2), IntReferenceHolder.single());
    }

    public ChargerContainer(final int windowId, final PlayerInventory playerInventory, final IInventory inventoryIn, final IntReferenceHolder dataIn) {
        super(ModContainers.CHARGER_CONTAINER.get(), windowId);
        assertInventorySize(inventoryIn, 2);
        this.tileCharger = inventoryIn;
        this.data = dataIn;
        this.addSlot(new Slot(inventoryIn, 0, 0, 0) {
            @Override
            public boolean isItemValid(ItemStack stack) {
                return true;
            }
        });
        this.addSlot(new Slot(inventoryIn, 0, 18, 0) {
            @Override
            public boolean isItemValid(ItemStack stack) {
                return false;
            }
        });
        this.trackInt(dataIn);
        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        for(int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(playerInventory, k, 8 + k * 18, 142));
        }
    }

    public ChargerContainer(final int windowId, final PlayerInventory playerInventory, final PacketBuffer packetBuffer) {
        this(windowId, playerInventory);
    }

    @OnlyIn(Dist.CLIENT)
    public int getChargeTime() {
        return this.data.get();
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return false;
    }
}
