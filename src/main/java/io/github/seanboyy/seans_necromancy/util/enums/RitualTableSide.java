package io.github.seanboyy.seans_necromancy.util.enums;

import net.minecraft.util.IStringSerializable;

public enum RitualTableSide implements IStringSerializable {
    LEFT("left"),
    RIGHT("right");

    private final String name;

    RitualTableSide(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }

    public String getName() {
        return this.name;
    }
}
