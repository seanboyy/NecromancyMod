package io.github.seanboyy.seans_necromancy.util;

public class Spirit {
    private final String name;
    private final float health;
    private final String type;
    private final String displayName;

    public Spirit(String name, float health, String type, String displayName) {
        this.name = name;
        this.health = health;
        this.type = type;
        this.displayName = displayName;
    }

    public String getName() {
        return name;
    }

    public float getHealth() {
        return health;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Spirit{" +
                "name='" + name + '\'' +
                ", health=" + health +
                ", type='" + type + '\'' +
                '}';
    }

    public String getDisplayName() {
        return displayName;
    }
}
