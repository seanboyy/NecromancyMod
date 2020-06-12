package io.github.seanboyy.seans_necromancy.util;

import io.github.seanboyy.seans_necromancy.Necromancy;
import io.github.seanboyy.seans_necromancy.registry.ModItems;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

@Mod.EventBusSubscriber(modid = Necromancy.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeEventBusSubscriber {

    @SubscribeEvent
    public static void onLivingDeath(final LivingDeathEvent event) {
        if(event.getEntityLiving() instanceof PlayerEntity) return;
        World worldIn = event.getEntityLiving().getEntityWorld();
        if(worldIn instanceof ServerWorld) {
            BlockPos deathPos = event.getEntityLiving().getPosition();
            String entityName = Objects.requireNonNull(ForgeRegistries.ENTITIES.getKey(event.getEntityLiving().getType())).toString();
            float entityHealth = event.getEntityLiving().getMaxHealth();
            String entityType = "hostile";
            if(!(event.getEntityLiving() instanceof MonsterEntity)) {
                entityType = "passive";
            }
            String displayName = event.getEntityLiving().getDisplayName().getFormattedText();
            ItemStack spirit = new ItemStack(ModItems.SPIRIT_ITEM.get(), 1);
            SpiritItemUtils.addAttributesToItemStack(spirit, entityName, entityHealth, entityType, displayName);
            ItemEntity itemEntity = new ItemEntity(worldIn, deathPos.getX(), deathPos.getY(), deathPos.getZ(), spirit);
            if(((ServerWorld)worldIn).summonEntity(itemEntity)) {
                Necromancy.LOGGER.debug("Spawned new spirit item with " + new Spirit(entityName, entityHealth, entityType, displayName));
            }
        }
    }
}
