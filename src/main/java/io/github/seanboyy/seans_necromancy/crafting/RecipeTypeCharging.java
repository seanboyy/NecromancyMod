package io.github.seanboyy.seans_necromancy.crafting;

import io.github.seanboyy.seans_necromancy.Necromancy;
import net.minecraft.item.crafting.IRecipeType;

public class RecipeTypeCharging implements IRecipeType<ChargingRecipe> {
    @Override
    public String toString() {
        return Necromancy.MOD_ID + ":charging";
    }
}
