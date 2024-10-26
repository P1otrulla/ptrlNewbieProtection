package dev.piotrulla.newbieprotection.bridge.packetevents;

import com.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import org.bukkit.plugin.java.JavaPlugin;

public class PacketEventsImpl implements PacketEventsProvider {

    @Override
    public void load(JavaPlugin plugin) {
        PacketEvents.setAPI(SpigotPacketEventsBuilder.build(plugin));
        PacketEvents.getAPI().load();
    }

    @Override
    public void enable() {
        PacketEvents.getAPI().init();
    }

    @Override
    public void disable() {
        PacketEvents.getAPI().terminate();
    }
}
