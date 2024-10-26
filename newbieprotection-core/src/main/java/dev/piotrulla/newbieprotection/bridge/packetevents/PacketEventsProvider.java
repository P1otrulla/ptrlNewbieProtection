package dev.piotrulla.newbieprotection.bridge.packetevents;

import org.bukkit.plugin.java.JavaPlugin;

public interface PacketEventsProvider {

    void load(JavaPlugin plugin);

    void enable();

    void disable();
}
