package io.github.seanboyy.seans_necromancy.crafting;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import io.github.seanboyy.seans_necromancy.registry.ModRecipes;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;

public class ChargingRecipe implements IRecipe<IInventory> {
    private final int chargeTime;
    private final Ingredient input;
    private final ItemStack output;
    private final ResourceLocation id;
    private final String group;

    public ChargingRecipe(ResourceLocation idIn, String groupIn, int recipeTime, Ingredient inputIn, ItemStack outputIn) {
        this.id = idIn;
        this.group = groupIn;
        this.chargeTime = recipeTime;
        this.input = inputIn;
        this.output = outputIn;
    }

    @Override
    public boolean matches(IInventory inv, World worldIn) {
        return this.input.test(inv.getStackInSlot(0));
    }

    @Override
    public ItemStack getCraftingResult(IInventory inv) {
        return this.getRecipeOutput().copy();
    }

    @Override
    public boolean canFit(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return this.output;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    public int getChargeTime() {
        return chargeTime;
    }

    @Override
    public IRecipeSerializer<ChargingRecipe> getSerializer() {
        return ModRecipes.CHARGING.get();
    }

    @Override
    public IRecipeType<ChargingRecipe> getType() {
        return ModRecipes.CHARGING_RECIPE;
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<ChargingRecipe> {

        public Serializer() { }

        @Override
        public ChargingRecipe read(ResourceLocation recipeId, JsonObject json) {
            String s = JSONUtils.getString(json, "group", "");
            JsonElement jsonElement = (JSONUtils.isJsonArray(json, "input") ? JSONUtils.getJsonArray(json, "input") : JSONUtils.getJsonObject(json, "input"));
            Ingredient input = Ingredient.deserialize(jsonElement);
            ItemStack output;
            if(json.get("output").isJsonObject()) output = ShapedRecipe.deserializeItem(JSONUtils.getJsonObject(json, "output"));
            else {
                String s1 = JSONUtils.getString(json, "output");
                ResourceLocation resourceLocation = new ResourceLocation(s1);
                if(ForgeRegistries.ITEMS.getValue(resourceLocation) == null) throw new IllegalStateException("Item: " + s1 + " does not exist");
                output = new ItemStack(ForgeRegistries.ITEMS.getValue(resourceLocation));
            }
            int recipeTime = JSONUtils.getInt(json, "time");
            return new ChargingRecipe(recipeId, s, recipeTime, input, output);
        }

        @Nullable
        @Override
        public ChargingRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
            int time = buffer.readVarInt();
            String s = buffer.readString(Short.MAX_VALUE);
            Ingredient input = Ingredient.read(buffer);
            ItemStack output = buffer.readItemStack();
            return new ChargingRecipe(recipeId, s, time, input, output);
        }

        @Override
        public void write(PacketBuffer buffer, ChargingRecipe recipe) {
            buffer.writeVarInt(recipe.chargeTime);
            buffer.writeString(recipe.group);
            recipe.input.write(buffer);
            buffer.writeItemStack(recipe.output);
        }
    }
}
