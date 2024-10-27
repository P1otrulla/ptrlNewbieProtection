package dev.piotrulla.newbieprotection.bridge;

import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;

import java.util.logging.Logger;

public class BridgeService {

    private static final Logger LOGGER = Logger.getLogger("ptrlNewbieProtection");

    private final PluginDescriptionFile pluginDescriptionFile;
    private final PluginManager pluginManager;


    public BridgeService(PluginDescriptionFile pluginDescriptionFile, PluginManager pluginManager) {
        this.pluginDescriptionFile = pluginDescriptionFile;
        this.pluginManager = pluginManager;
    }

    public void initialize() {

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

}
