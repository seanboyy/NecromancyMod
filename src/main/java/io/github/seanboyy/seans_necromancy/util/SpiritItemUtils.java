package io.github.seanboyy.seans_necromancy.util;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class SpiritItemUtils {
    @Nullable
    public static Spirit getAttributesFromItemStack(ItemStack stack) {
        if(stack.getTag() == null) return null;
        return getAttributesFromTag(stack.getTag());
    }

    @Nullable
    public static Spirit getAttributesFromTag(CompoundNBT tag) {
        if(tag.contains("Spirit")){
            String name = "minecraft:zombie";
            float health = 20F;
            String type = "hostile";
            String display = "Zombie";
            CompoundNBT spiritAttr = tag.getCompound("Spirit");
            if(spiritAttr.contains("name")) {
                name = spiritAttr.getString("name");
            }
            if(spiritAttr.contains("health")) {
                health = spiritAttr.getFloat("health");
            }
            if(spiritAttr.contains("type")) {
                type = spiritAttr.getString("type");
            }
            if(spiritAttr.contains("display")) {
                display = spiritAttr.getString("display");
            }
            return new Spirit(name, health, type, display);
        }
        return null;
    }

    public static void addAttributesToItemStack(ItemStack stack, String name, Float health, String type, String display) {
        addAttributesToTag(stack.getOrCreateTag(), name, health, type, display);
    }

    public static void addSpiritToItemStack(ItemStack stack, Spirit spirit) {
        addAttributesToItemStack(stack, spirit.getName(), spirit.getHealth(), spirit.getType(), spirit.getDisplayName());
    }

    public static void addAttributesToTag(CompoundNBT tag, String name, Float health, String type, String display) {
        CompoundNBT spiritAttr = new CompoundNBT();
        spiritAttr.putString("name", name);
        spiritAttr.putFloat("health", health);
        spiritAttr.putString("type", type);
        spiritAttr.putString("display", display);
        tag.put("Spirit", spiritAttr);
    }

    @OnlyIn(Dist.CLIENT)
    public static void addSpiritTooltip(ItemStack itemStackIn, List<ITextComponent> lores) {
        Spirit spiritAttributes = getAttributesFromItemStack(itemStackIn);
        if(spiritAttributes != null) {
            lores.add(new StringTextComponent(spiritAttributes.getDisplayName()));
            lores.add(new TranslationTextComponent("seans_necromancy:health").appendText(": " + spiritAttributes.getHealth()));
            switch(spiritAttributes.getType()) {
                case "hostile":
                    lores.add(new TranslationTextComponent("seans_necromancy:hostile").applyTextStyle(TextFormatting.RED));
                    break;
                case "passive":
                    lores.add(new TranslationTextComponent("seans_necromancy:passive").applyTextStyle(TextFormatting.BLUE));
                    break;
                default:
                    break;
            }
        }
    }


}
