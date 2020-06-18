package io.github.seanboyy.seans_necromancy.objects.tileentities;

import com.google.common.collect.Maps;
import io.github.seanboyy.seans_necromancy.Necromancy;
import io.github.seanboyy.seans_necromancy.container.inventory.ChargerContainer;
import io.github.seanboyy.seans_necromancy.crafting.ChargingRecipe;
import io.github.seanboyy.seans_necromancy.registry.ModRecipes;
import io.github.seanboyy.seans_necromancy.registry.ModTileEntities;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IRecipeHelperPopulator;
import net.minecraft.inventory.IRecipeHolder;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeItemHelper;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.LockableTileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;

public class ChargerTileEntity extends LockableTileEntity implements ISidedInventory, IRecipeHolder, IRecipeHelperPopulator, ITickableTileEntity {
    private final int[] SLOTS_FOR_UP = new int[] {1};
    private final int[] SLOTS_FOR_DOWN = new int[]{0};
    private final NonNullList<ItemStack> items;
    private final Map<ResourceLocation, Integer> usedRecipes = Maps.newHashMap();
    private int chargeTime;
    private int chargeTimeMax;
    private int recipesUsed;
    protected final IIntArray chargerData = new IIntArray() {
        @Override
        public int get(int index) {
            switch(index) {
                case 0:
                    return ChargerTileEntity.this.chargeTime;
                case 1:
                    return ChargerTileEntity.this.chargeTimeMax;
                default:
                    return 0;
            }
        }

        @Override
        public void set(int index, int value) {
            switch(index) {
                case 0:
                    ChargerTileEntity.this.chargeTime = value;
                    break;
                case 1:
                    ChargerTileEntity.this.chargeTimeMax = value;
                    break;
            }
        }

        @Override
        public int size() {
            return 2;
        }
    };

    protected final IRecipeType<ChargingRecipe> recipeType;

    public ChargerTileEntity() {
        super(ModTileEntities.CHARGER_TILE_ENTITY.get());
        this.items = NonNullList.withSize(2, ItemStack.EMPTY);
        this.recipeType = ModRecipes.CHARGING_RECIPE;
    }

    private boolean isCharging() {
        return this.chargeTime < this.chargeTimeMax;
    }


    @Override
    protected ITextComponent getDefaultName() {
        return new TranslationTextComponent(Necromancy.MOD_ID + ".container.charging");
    }

    @Override
    protected Container createMenu(int id, PlayerInventory player) {
        return new ChargerContainer(id, player, this, chargerData);
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return index == 0;
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        return side == Direction.UP ? SLOTS_FOR_DOWN : SLOTS_FOR_UP;
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, @Nullable Direction direction) {
        return this.isItemValidForSlot(index, itemStackIn);
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, Direction direction) {
        return index == 1;
    }

    @Override
    public int getSizeInventory() {
        return items.size();
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack item : this.items) {
            if(!item.isEmpty()) return false;
        }
        return true;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return index >= this.getSizeInventory() ? ItemStack.EMPTY : this.items.get(index);
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        return index >= 0 && index < this.items.size() ? ItemStackHelper.getAndSplit(this.items, index, count) : ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        return index >= 0 && index < this.items.size() ? ItemStackHelper.getAndRemove(this.items, index) : ItemStack.EMPTY;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        ItemStack itemStack = this.items.get(index);
        boolean flag = !stack.isEmpty() && stack.isItemEqual(itemStack) && ItemStack.areItemStackTagsEqual(stack, itemStack);
        this.items.set(index, stack);
        if(stack.getCount() > this.getInventoryStackLimit()) {
            stack.setCount(this.getInventoryStackLimit());
        }
        if(index == 0 && !flag) {
            this.chargeTime = 0;
            this.markDirty();
        }
    }

    @Override
    public boolean isUsableByPlayer(PlayerEntity player) {
        assert this.world != null;
        if(this.world.getTileEntity(this.pos) != this) return false;
        return player.getDistanceSq(this.pos.getX() + 0.5, this.pos.getY() + 0.5, this.pos.getZ() + 0.5) <= 64D;
    }

    @Override
    public void clear() {
        this.items.clear();
    }


    @Override
    public void fillStackedContents(RecipeItemHelper helper) {
        for (ItemStack item : this.items) {
            helper.accountStack(item);
            helper.accountStack(item);
        }
    }

    @Override
    public void setRecipeUsed(@Nullable IRecipe<?> recipe) {
        if(recipe != null) {
            usedRecipes.compute(recipe.getId(), (resourceLocation, integer) -> 1 + (integer == null ? 0 : integer));
        }
    }

    @Nullable
    @Override
    public IRecipe<?> getRecipeUsed() {
        return null;
    }

    @Override
    public void tick() {
        boolean flag = this.isCharging();
        boolean flag1 = false;
        if(this.items.get(0) == ItemStack.EMPTY){
            this.chargeTime = 0;
            return;
        }
        assert this.world != null;
        if(!this.world.isRemote) {
            ItemStack itemStack = this.items.get(0);
            if(this.isCharging() || !itemStack.isEmpty()) {
                ChargingRecipe iRecipe = this.world.getRecipeManager().getRecipe(this.recipeType, this, this.world).orElse(null);
                if(!(iRecipe instanceof ChargingRecipe)) return;
                if(!this.isCharging() && this.canCharge(iRecipe)) {
                    this.chargeTimeMax = getChargeTime();
                    this.chargeTime = 0;
                    this.recipesUsed = this.chargeTime;
                    if(this.isCharging()) {
                        flag1 = true;
                        //furnace consumes fuel here..... I do NOTHING!!!!!
                    }
                }
                if(this.isCharging() && this.canCharge(iRecipe)) {
                    ++this.chargeTime;
                    if(this.chargeTime == this.chargeTimeMax) {
                        this.chargeTime = 0;
                        this.charge(iRecipe);
                        flag1 = true;
                    }
                } else {
                    this.chargeTime = 0;
                }
            } else if (this.chargeTime > 0) {
                this.chargeTime = MathHelper.clamp(this.chargeTime - 2, 0, this.chargeTimeMax);
            }
        }
        if(flag1) this.markDirty();
    }

    private boolean canCharge(@Nullable IRecipe<?> recipeIn) {
        if(!this.items.get(0).isEmpty() && recipeIn != null) {
            ItemStack itemStack = recipeIn.getRecipeOutput();
            if(itemStack.isEmpty()) return false;
            else {
                ItemStack itemStack1 = this.items.get(1);
                if(itemStack1.isEmpty()) {return true;}
                else if(!itemStack1.isItemEqual(itemStack)) { return false; }
                else if(itemStack1.getCount() + itemStack.getCount() <= this.getInventoryStackLimit() && itemStack1.getCount() + itemStack.getCount() <= itemStack1.getMaxStackSize()) { return true; }
                else return itemStack1.getCount() + itemStack.getCount() <= itemStack.getMaxStackSize();
            }
        }
        return false;
    }

    private void charge(@Nullable IRecipe<?> recipe) {
        assert this.world != null;
        if(recipe != null && this.canCharge(recipe)) {
            ItemStack itemStack = this.items.get(0);
            ItemStack itemStack1 = recipe.getRecipeOutput();
            ItemStack itemStack2 = this.items.get(1);
            if(itemStack2.isEmpty()) {
                this.items.set(1, itemStack1.copy());
            } else if (itemStack2.getItem() == itemStack1.getItem()) {
                itemStack2.grow(itemStack1.getCount());
                this.items.set(1, itemStack2);
            }
            if(!this.world.isRemote) {
                LightningBoltEntity lightningBoltEntity = new LightningBoltEntity(this.world, this.pos.getX(), this.pos.getY() + 1D, this.pos.getZ(), false);
                ((ServerWorld)this.world).addLightningBolt(lightningBoltEntity);
                this.setRecipeUsed(recipe);
            }
            itemStack.shrink(1);
        }
    }

    LazyOptional<? extends IItemHandler>[] handlers = SidedInvWrapper.create(this, Direction.UP, Direction.DOWN);

    @Nullable
    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
        if(!this.removed && side != null && cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            if(side == Direction.UP) return handlers[0].cast();
            else return handlers[1].cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void remove() {
        super.remove();
        for (LazyOptional<? extends IItemHandler> handler : handlers) {
            handler.invalidate();
        }
    }

    private int getChargeTime() {
        assert this.world != null;
        return this.world.getRecipeManager().getRecipe(this.recipeType, this, this.world).map(ChargingRecipe::getChargeTime).orElse(200);
    }
}
