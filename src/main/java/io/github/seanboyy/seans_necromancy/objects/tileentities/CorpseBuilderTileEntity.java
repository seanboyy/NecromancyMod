package io.github.seanboyy.seans_necromancy.objects.tileentities;

import io.github.seanboyy.seans_necromancy.Necromancy;
import io.github.seanboyy.seans_necromancy.container.inventory.CorpseBuilderContainer;
import io.github.seanboyy.seans_necromancy.network.Network;
import io.github.seanboyy.seans_necromancy.network.packet.PacketCorpseBuilderUpdate;
import io.github.seanboyy.seans_necromancy.objects.blocks.CorpseBuilderBlock;
import io.github.seanboyy.seans_necromancy.registry.ModItems;
import io.github.seanboyy.seans_necromancy.registry.ModTileEntities;
import net.minecraft.block.BlockState;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.LockableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

import javax.annotation.Nullable;

import java.util.Objects;

import static io.github.seanboyy.seans_necromancy.util.Constants.*;

public class CorpseBuilderTileEntity extends LockableTileEntity implements ISidedInventory, ITickableTileEntity {

    private static final int[] SLOTS_FOR_UP = new int[]{3};
    private static final int[] SLOTS_FOR_ELSE = new int[] {0, 1, 2, 3};
    private NonNullList<ItemStack> corpseMaterialStacks = NonNullList.withSize(4, ItemStack.EMPTY);
    private int assemblyTimeSkeleton;
    private int assemblyTimeZombie;
    private int skeletonStage;
    private int zombieStage;
    public final IIntArray data = new IIntArray() {
        @Override
        public int get(int index) {
            switch(index) {
                case 0:
                    return CorpseBuilderTileEntity.this.assemblyTimeSkeleton;
                case 1:
                    return CorpseBuilderTileEntity.this.assemblyTimeZombie;
                case 2:
                    return CorpseBuilderTileEntity.this.skeletonStage;
                case 3:
                    return CorpseBuilderTileEntity.this.zombieStage;
                default:
                    return 0;
            }
        }

        @Override
        public void set(int index, int value) {
            switch(index) {
                case 0:
                    CorpseBuilderTileEntity.this.assemblyTimeSkeleton = value;
                    break;
                case 1:
                    CorpseBuilderTileEntity.this.assemblyTimeZombie = value;
                    break;
                case 2:
                    CorpseBuilderTileEntity.this.skeletonStage = value;
                    break;
                case 3:
                    CorpseBuilderTileEntity.this.zombieStage = value;
            }
        }

        @Override
        public int size() {
            return 4;
        }
    };

    public CorpseBuilderTileEntity() {
        super(ModTileEntities.CORPSE_BUILDER_TILE_ENTITY.get());
    }

    @Override
    public int getSizeInventory() {
        return this.corpseMaterialStacks.size();
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack itemStack : this.corpseMaterialStacks) {
            if(!itemStack.isEmpty()) return false;
        }
        return true;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return index >= 0 && index < this.corpseMaterialStacks.size() ? this.corpseMaterialStacks.get(index) : ItemStack.EMPTY;
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        return ItemStackHelper.getAndSplit(this.corpseMaterialStacks, index, count);
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        return ItemStackHelper.getAndRemove(this.corpseMaterialStacks, index);
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        if(index >= 0 && index < this.corpseMaterialStacks.size()) {
            this.corpseMaterialStacks.set(index, stack);
        }
    }

    @Override
    public boolean isUsableByPlayer(PlayerEntity player) {
        assert this.world != null;
        if(this.world.getTileEntity(this.pos) != this) {
            return false;
        } else {
            return !(player.getDistanceSq(this.pos.getX() + 0.5D, this.pos.getY() + 0.5D, this.pos.getZ() + 0.5D) > 64D);
        }
    }

    @Override
    public void tick() {
        ItemStack corpseStack = this.corpseMaterialStacks.get(3);

        if(corpseStack.getItem() == ModItems.SKELETON.get()) this.skeletonStage = 6;
        else if(corpseStack.getItem() == ModItems.ZOMBIE.get()) {
            this.skeletonStage = 6;
            this.zombieStage = 6;
        }
        ItemStack fuelStack = this.corpseMaterialStacks.get(2);
        if(fuelStack.getCount() <= 0 || fuelStack.isEmpty()) return;
        boolean flag = this.canStartAssembleSkeleton();
        boolean flag1 = this.canStartAssembleZombie();
        boolean flag2 = this.assemblyTimeSkeleton > 0 && this.canContinueAssembleSkeleton();
        boolean flag3 = this.assemblyTimeZombie > 0 && this.canContinueAssembleZombie();
        ItemStack bones = this.corpseMaterialStacks.get(0);
        ItemStack flesh = this.corpseMaterialStacks.get(1);
        if(flag2) {
            --this.assemblyTimeSkeleton;
            if(assemblyTimeSkeleton % 100 == 0) ++this.skeletonStage;
            if(this.skeletonStage == 6) {
                fuelStack.shrink(1);
                this.corpseMaterialStacks.set(2, fuelStack);
                bones.shrink(10);
                this.corpseMaterialStacks.set(0, bones);
            }
            if (!this.canStartAssembleZombie() && this.skeletonStage == 6) {
                //DO SOMETHING NO ZOMBIE
                World worldIn = this.getWorld();
                if(worldIn instanceof ServerWorld) {
                    LightningBoltEntity lightningBolt = new LightningBoltEntity(worldIn, this.pos.getX(), this.pos.getY() + 1D, this.pos.getZ(), false);
                    ((ServerWorld) worldIn).addLightningBolt(lightningBolt);
                }
            }
        } else if(flag) {
            this.assemblyTimeSkeleton = 600;
            this.markDirty();
        }
        if(flag3) {
            --this.assemblyTimeZombie;
            if(assemblyTimeZombie % 100 == 0) ++this.zombieStage;
            if(zombieStage == 6) {
                fuelStack.shrink(1);
                this.corpseMaterialStacks.set(2, fuelStack);
                flesh.shrink(10);
                this.corpseMaterialStacks.set(1, flesh);
                World worldIn = this.getWorld();
                if(worldIn instanceof ServerWorld) {
                    LightningBoltEntity lightningBolt = new LightningBoltEntity(worldIn, this.pos.getX(), this.pos.getY() + 1D, this.pos.getZ(), false);
                    ((ServerWorld)worldIn).addLightningBolt(lightningBolt);
                }
            }
            //DO SOMETHING WHEN ZOMBIE IS DONE
        } else if(flag1) {
            this.assemblyTimeZombie = 600;
            this.markDirty();
        }
        assert this.world != null;
        if(!this.world.isRemote) {
            BlockState blockState = this.world.getBlockState(this.pos);
            if (!(blockState.getBlock() instanceof CorpseBuilderBlock)) {
                return;
            }
            if(blockState.get(CorpseBuilderBlock.SKELETON_LEVEL) != skeletonStage || blockState.get(CorpseBuilderBlock.FLESH_LEVEL) != zombieStage) {
                blockState = blockState.with(CorpseBuilderBlock.SKELETON_LEVEL, skeletonStage).with(CorpseBuilderBlock.FLESH_LEVEL, zombieStage);
                this.world.setBlockState(this.pos, blockState, SEND_TO_CLIENT | BLOCK_UPDATE);
                PacketCorpseBuilderUpdate packetCorpseBuilderUpdate = new PacketCorpseBuilderUpdate(this.skeletonStage, this.zombieStage, this.pos);
                Network.sendPacketToClient(packetCorpseBuilderUpdate, (ServerPlayerEntity) Objects.requireNonNull(world.getClosestPlayer(this.pos.getX(), this.pos.getY(), this.pos.getZ())));
            }
        }
    }

    private boolean canStartAssembleSkeleton() {
        ItemStack itemStack = this.corpseMaterialStacks.get(0);
        ItemStack itemStack1 = this.corpseMaterialStacks.get(3);
        if(!itemStack1.isEmpty() || itemStack1.getCount() < 1) return false;
        if(itemStack.isEmpty() || itemStack.getCount() < 10) return false;
        return this.skeletonStage == 0;
    }

    private boolean canContinueAssembleSkeleton() {
        ItemStack itemStack = this.corpseMaterialStacks.get(0);
        ItemStack itemStack1 = this.corpseMaterialStacks.get(3);
        if(!itemStack1.isEmpty() || itemStack1.getCount() < 1) return false;
        return !itemStack.isEmpty() && itemStack.getCount() >= 10;
    }

    private boolean canStartAssembleZombie() {
        ItemStack itemStack = this.corpseMaterialStacks.get(1);
        ItemStack itemStack1 = this.corpseMaterialStacks.get(3);
        if(!itemStack1.isEmpty() || itemStack1.getCount() < 1 || itemStack1.getItem() != ModItems.SKELETON.get()) return false;
        if(itemStack.isEmpty() || itemStack.getCount() < 10) return false;
        return this.zombieStage == 0 && this.skeletonStage == 6;
    }

    private boolean canContinueAssembleZombie() {
        ItemStack itemStack = this.corpseMaterialStacks.get(1);
        ItemStack itemStack1 = this.corpseMaterialStacks.get(3);
        if(!itemStack1.isEmpty() || itemStack1.getCount() < 1 || itemStack1.getItem() != ModItems.SKELETON.get()) return false;
        return !itemStack.isEmpty() && itemStack.getCount() >= 10;
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        if(side == Direction.UP) {
            return SLOTS_FOR_UP;
        } else {
            return SLOTS_FOR_ELSE;
        }
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        Item item  = stack.getItem();
        if(index == 0) return Tags.Items.BONES.contains(item);
        else if(index == 1) return item == Items.ROTTEN_FLESH;
        else if(index == 2) return item == ModItems.CHARGED_DIAMOND.get();
        else return false;
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, @Nullable Direction direction) {
        return this.isItemValidForSlot(index, itemStackIn);
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, Direction direction) {
        return index == 3;
    }

    @Override
    public void clear() {
        this.corpseMaterialStacks.clear();
    }

    @Override
    protected ITextComponent getDefaultName() {
        return new TranslationTextComponent(Necromancy.MOD_ID + ".container.corpse_builder");
    }

    @Override
    protected Container createMenu(int id, PlayerInventory player) {
        return new CorpseBuilderContainer(id, player, this, this.data);
    }

    @Override
    public void read(CompoundNBT compound) {
        super.read(compound);
        this.corpseMaterialStacks = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(compound, this.corpseMaterialStacks);
        this.assemblyTimeSkeleton = compound.getShort("SkeletonTime");
        this.assemblyTimeZombie = compound.getShort("ZombieTime");
        this.skeletonStage = compound.getByte("SkeletonStage");
        this.zombieStage = compound.getByte("ZombieStage");
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        compound.putShort("SkeletonTime", (short)this.assemblyTimeSkeleton);
        compound.putShort("ZombieTime", (short)this.assemblyTimeZombie);
        compound.putByte("SkeletonStage", (byte)this.skeletonStage);
        compound.putByte("ZombieStage", (byte)this.zombieStage);
        return compound;
    }

    LazyOptional<? extends IItemHandler>[] handlers = SidedInvWrapper.create(this, Direction.UP, Direction.NORTH);

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
}
