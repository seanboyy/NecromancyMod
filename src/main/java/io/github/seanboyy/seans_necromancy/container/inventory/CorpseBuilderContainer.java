package io.github.seanboyy.seans_necromancy.container.inventory;

import io.github.seanboyy.seans_necromancy.objects.items.CorpseItem;
import io.github.seanboyy.seans_necromancy.registry.ModBlocks;
import io.github.seanboyy.seans_necromancy.registry.ModContainers;
import io.github.seanboyy.seans_necromancy.registry.ModItems;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.Tags;

public class CorpseBuilderContainer extends Container {
    private final IInventory tileCorpseBuilder;
    private final IIntArray data;

    public CorpseBuilderContainer(final int windowId, final PlayerInventory playerInventory) {
        this(windowId, playerInventory, new Inventory(4), new IntArray(4));
    }

    public CorpseBuilderContainer(final int windowId, final PlayerInventory playerInventory, final IInventory inventoryIn, IIntArray dataIn) {
        super(ModContainers.CORPSE_BUILDER_CONTAINER.get(), windowId);
        assertInventorySize(inventoryIn, 4);
        assertIntArraySize(dataIn, 4);
        this.tileCorpseBuilder = inventoryIn;
        this.data = dataIn;
        this.addSlot(new BoneSlot(inventoryIn, 0, 44, 24));
        this.addSlot(new FleshSlot(inventoryIn, 1, 116, 24));
        this.addSlot(new FuelSlot(inventoryIn, 2, 12, 16));
        this.addSlot(new CorpseSlot(inventoryIn, 3, 80, 55));
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

    public CorpseBuilderContainer(final int windowId, final PlayerInventory playerInventory, final PacketBuffer packetBuffer) {
        this(windowId, playerInventory);
    }

    @OnlyIn(Dist.CLIENT)
    public int getSkeletonTime() {
        return this.data.get(0);
    }

    @OnlyIn(Dist.CLIENT)
    public int getZombieTime() {
        return this.data.get(1);
    }

    @OnlyIn(Dist.CLIENT)
    public int getSkeletonStage() {
        return this.data.get(2);
    }

    @OnlyIn(Dist.CLIENT)
    public int getZombieStage() {
        return this.data.get(3);
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if(slot != null && slot.getHasStack()) {
            ItemStack itemStack1 = slot.getStack();
            itemStack = itemStack1.copy();
            if(index < 3) {
                if(!this.mergeItemStack(itemStack1, 3, this.inventorySlots.size(), true)) return ItemStack.EMPTY;
            } else {
                if(FuelSlot.isValidAssemblyFuel(itemStack)) {
                    if(!this.mergeItemStack(itemStack1, 2, 3, false)) return ItemStack.EMPTY;
                }
                else if(FleshSlot.isFlesh(itemStack)) {
                    if(!this.mergeItemStack(itemStack1, 1, 2, false)) return ItemStack.EMPTY;
                }
                else if(BoneSlot.isBone(itemStack)) {
                    if(!this.mergeItemStack(itemStack1, 0, 1, false)) return ItemStack.EMPTY;
                }
                else return ItemStack.EMPTY;
            }
        }
        return itemStack;
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return this.tileCorpseBuilder.isUsableByPlayer(playerIn);
    }

    private static final class BoneSlot extends Slot {
        public BoneSlot(IInventory inventoryIn, int index, int xPosition, int yPosition) {
            super(inventoryIn, index, xPosition, yPosition);
        }

        @Override
        public boolean isItemValid(ItemStack stack) {
            return isBone(stack);
        }

        public static boolean isBone(ItemStack stack) {
            return Tags.Items.BONES.contains(stack.getItem());
        }
    }

    private static final class FleshSlot extends Slot {
        public FleshSlot(IInventory inventoryIn, int index, int xPosition, int yPosition) {
            super(inventoryIn, index, xPosition, yPosition);
        }

        @Override
        public boolean isItemValid(ItemStack stack) {
            return isFlesh(stack);
        }

        public static boolean isFlesh(ItemStack stack) {
            return stack.getItem() == Items.ROTTEN_FLESH;
        }
    }

    private static final class FuelSlot extends Slot {
        public FuelSlot(IInventory inventoryIn, int index, int xPosition, int yPosition) {
            super(inventoryIn, index, xPosition, yPosition);
        }

        @Override
        public boolean isItemValid(ItemStack stack) {
            return isValidAssemblyFuel(stack);
        }

        public static boolean isValidAssemblyFuel(ItemStack stack) {
            return stack.getItem() == ModItems.CHARGED_DIAMOND.get();
        }
    }

    private static final class CorpseSlot extends Slot {
        public CorpseSlot(IInventory inventoryIn, int index, int xPosition, int yPosition) {
            super(inventoryIn, index, xPosition, yPosition);
        }

        @Override
        public boolean isItemValid(ItemStack stack) {
            return isCorpse(stack);
        }

        public static boolean isCorpse(ItemStack stack) {
            return stack.getItem() instanceof CorpseItem;
        }
    }
}
