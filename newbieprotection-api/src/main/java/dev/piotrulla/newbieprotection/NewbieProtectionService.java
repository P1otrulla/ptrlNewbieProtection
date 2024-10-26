package dev.piotrulla.newbieprotection;

import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface NewbieProtectionService {

    /**
     * Start protection for player
     *
     * @param player   player to protect
     * @param duration protection duration
     */
    void startProtection(Player player, Duration duration);

    /**
     * End protection for player
     *
     * @param player player to end protection
     */
    void endProtection(Player player);

    /**
     * Remove protection for player without sending event
     *
     * @param uniqueId player to remove protection
     */
    void removeProtection(UUID uniqueId);


    Optional<Newbie> getNewbie(Player player);

    /**
     * Check if player is protected
     *
     * @param player player to check
     * @return true if player is protected
     */
    boolean isProtected(Player player);

    /**
     * Get remaining protection time for player
     *
     * @param player player to check
     * @return remaining protection time
     */
    Duration getRemainingProtectionTime(Player player);

    /**
     * Get all newbies
     *
     * @return collection of newbies
     */
    Collection<Newbie> newbies();

}
