package io.github.seanboyy.seans_necromancy.util.enums;

import net.minecraft.util.IStringSerializable;

public enum LeftRightBlockSide implements IStringSerializable {
    LEFT("left"),
    RIGHT("right");

    private final String name;

    LeftRightBlockSide(String name) {
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
