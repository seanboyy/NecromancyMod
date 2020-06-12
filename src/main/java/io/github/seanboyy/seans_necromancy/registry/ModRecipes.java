package io.github.seanboyy.seans_necromancy.registry;

import io.github.seanboyy.seans_necromancy.Necromancy;
import io.github.seanboyy.seans_necromancy.crafting.ChargingRecipe;
import io.github.seanboyy.seans_necromancy.crafting.RecipeTypeCharging;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModRecipes {
    public static final IRecipeType<ChargingRecipe> CHARGING_RECIPE = new RecipeTypeCharging();

    public static final DeferredRegister<IRecipeSerializer<?>> RECIPE_SERIALIZERS = new DeferredRegister<>(ForgeRegistries.RECIPE_SERIALIZERS, Necromancy.MOD_ID);

    public static final RegistryObject<IRecipeSerializer<ChargingRecipe>> CHARGING = RECIPE_SERIALIZERS.register("charging", ChargingRecipe.Serializer::new);

}
