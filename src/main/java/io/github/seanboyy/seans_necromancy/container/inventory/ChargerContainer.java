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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ChargerContainer extends Container {
    private final IInventory tileCharger;
    private final IIntArray data;

    public ChargerContainer(final int windowId, final PlayerInventory playerInventory) {
        this(windowId, playerInventory, new Inventory(2), new IntArray(2));
    }

    public ChargerContainer(final int windowId, final PlayerInventory playerInventory, final IInventory inventoryIn, final IIntArray dataIn) {
        super(ModContainers.CHARGER_CONTAINER.get(), windowId);
        assertInventorySize(inventoryIn, 2);
        assertIntArraySize(dataIn, 2);
        this.tileCharger = inventoryIn;
        this.data = dataIn;
        this.addSlot(new Slot(inventoryIn, 0, 53, 34) {
            @Override
            public boolean isItemValid(ItemStack stack) {
                return true;
            }
        });
        this.addSlot(new Slot(inventoryIn, 1, 107, 34) {
            @Override
            public boolean isItemValid(ItemStack stack) {
                return false;
            }
        });
        this.trackIntArray(dataIn);
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
        return this.data.get(0);
    }

    @OnlyIn(Dist.CLIENT)
    public int getChargeTimeMax() {
        return this.data.get(1);
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return this.tileCharger.isUsableByPlayer(playerIn);
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if(slot != null && slot.getHasStack()) {
            ItemStack itemStack1 = slot.getStack();
            itemStack = itemStack1.copy();
            if(index < 2) {
                if(!this.mergeItemStack(itemStack1, 2, this.inventorySlots.size(), true)) return ItemStack.EMPTY;
            } else {
                if(!this.mergeItemStack(itemStack1, 0, 1, false)) return ItemStack.EMPTY;
            }
        }
        return itemStack;
    }
}
