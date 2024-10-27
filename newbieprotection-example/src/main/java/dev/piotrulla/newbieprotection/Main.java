package dev.piotrulla.newbieprotection;

import dev.piotrulla.newbieprotection.event.NewbieProtectionStartEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {

    private NewbieProtectionAPI api;

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);

        this.api = NewbieProtectionAPIProvider.provide();
    }

    @Override
    public void onDisable() {

    }

    @EventHandler
    void onProtect(NewbieProtectionStartEvent event) {
        this.api.getNewbieProtectionService().getNewbie(event.getPlayer()).ifPresent(newbie -> {
            System.out.println("Player " + event.getPlayer().getName() + " is now protected for "+newbie.protectionTime().toMinutes()+" mins!");
        });
    }
}