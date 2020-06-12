package io.github.seanboyy.seans_necromancy.objects.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ChargedItem extends Item {
    public ChargedItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean hasEffect(ItemStack stack) {
        return true;
    }
}
