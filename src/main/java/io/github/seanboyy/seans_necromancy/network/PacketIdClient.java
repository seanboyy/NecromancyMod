package io.github.seanboyy.seans_necromancy.network;

public enum PacketIdClient implements IPacketId {
    CORPSE_BUILDER_UPDATE;

    public static final PacketIdClient[] VALUES;

    static {
        VALUES = values();
    }
}
