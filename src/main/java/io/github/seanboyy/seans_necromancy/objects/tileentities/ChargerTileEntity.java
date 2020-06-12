package io.github.seanboyy.seans_necromancy.objects.tileentities;

import com.google.common.collect.Maps;
import io.github.seanboyy.seans_necromancy.Necromancy;
import io.github.seanboyy.seans_necromancy.container.inventory.ChargerContainer;
import io.github.seanboyy.seans_necromancy.crafting.ChargingRecipe;
import io.github.seanboyy.seans_necromancy.registry.ModRecipes;
import io.github.seanboyy.seans_necromancy.registry.ModTileEntities;
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
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.*;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;
import java.util.Map;

public class ChargerTileEntity extends LockableTileEntity implements ISidedInventory, IRecipeHolder, IRecipeHelperPopulator, ITickableTileEntity {
    private final int[] SLOTS_FOR_UP = new int[] {1};
    private final int[] SLOTS_FOR_DOWN = new int[]{0};
    private final NonNullList<ItemStack> items;
    private final Map<ResourceLocation, Integer> usedRecipes = Maps.newHashMap();
    private int chargeTime;
    protected final IntReferenceHolder chargerData = new IntReferenceHolder() {
        @Override
        public int get() {
            return ChargerTileEntity.this.chargeTime;
        }

        @Override
        public void set(int p_221494_1_) {
            ChargerTileEntity.this.chargeTime = p_221494_1_;
        }
    };

    protected final IRecipeType<ChargingRecipe> recipeType;

    public ChargerTileEntity() {
        super(ModTileEntities.CHARGER_TILE_ENTITY.get());
        this.items = NonNullList.withSize(2, ItemStack.EMPTY);
        this.recipeType = ModRecipes.CHARGING_RECIPE;
    }


    @Override
    protected ITextComponent getDefaultName() {
        return new TranslationTextComponent(Necromancy.MOD_ID + ".container.charging");
    }

    @Override
    protected Container createMenu(int id, PlayerInventory player) {
        return new ChargerContainer(id, player);
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return index == 0;
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        return side == Direction.UP ? SLOTS_FOR_UP : SLOTS_FOR_DOWN;
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
        if(index >= 0 && index < this.items.size()) {
            this.items.set(index, stack);
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

    }

    @Override
    public void setRecipeUsed(@Nullable IRecipe<?> recipe) {

    }

    @Nullable
    @Override
    public IRecipe<?> getRecipeUsed() {
        return null;
    }

    @Override
    public void tick() {

    }
}
