package dev.piotrulla.newbieprotection.bridge;

import dev.piotrulla.newbieprotection.bridge.packetevents.NonePacketEventsImpl;
import dev.piotrulla.newbieprotection.bridge.packetevents.PacketEventsImpl;
import dev.piotrulla.newbieprotection.bridge.packetevents.PacketEventsProvider;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;

import java.util.logging.Logger;

public class BridgeService {

    private static final Logger LOGGER = Logger.getLogger("ptrlNewbieProtection");

    private final PluginDescriptionFile pluginDescriptionFile;
    private final PluginManager pluginManager;

    private PacketEventsProvider packetEventsProvider;

    public BridgeService(PluginDescriptionFile pluginDescriptionFile, PluginManager pluginManager) {
        this.pluginDescriptionFile = pluginDescriptionFile;
        this.pluginManager = pluginManager;
    }

    public void initialize() {
        this.init("PacketEvents", () -> this.packetEventsProvider = new PacketEventsImpl(), () -> this.packetEventsProvider = new NonePacketEventsImpl());
    }

    private void init(String plugin, Bridge bridge, Runnable fail) {
        if (this.pluginManager.isPluginEnabled(plugin)) {
            bridge.initialize();

            LOGGER.info("Successfully registered " + plugin + " bridge.");
        }
        else {
            fail.run();

            LOGGER.warning("Failed to register " + plugin + " bridge.");
        }
    }

    public PacketEventsProvider getPacketEventsProvider() {
        return this.packetEventsProvider;
    }
}
