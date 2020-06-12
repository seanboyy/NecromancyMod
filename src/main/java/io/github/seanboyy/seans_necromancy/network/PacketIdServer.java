package io.github.seanboyy.seans_necromancy.network;

public enum PacketIdServer implements IPacketId{
    CORPSE_BUILDER_UPDATE_REQUEST;

    public static final PacketIdServer[] VALUES;

    static {
        VALUES = values();
    }
}
