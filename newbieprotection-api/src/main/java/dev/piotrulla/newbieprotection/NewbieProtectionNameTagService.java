package dev.piotrulla.newbieprotection;

import org.bukkit.entity.Player;

public interface NewbieProtectionNameTagService {

    void initialize();

    void applyNameTag(Player player);

    void removeNameTag(Player player);

    void shutdown();

}
