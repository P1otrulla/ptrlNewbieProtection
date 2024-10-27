package dev.piotrulla.newbieprotection;

import dev.piotrulla.newbieprotection.metrics.NewbieProtectionMetrics;
import dev.piotrulla.newbieprotection.nametag.NameTagService;
import org.bukkit.Server;
import org.bukkit.entity.Player;

import java.time.Instant;

public class NewbieProtectionTask implements Runnable {

    private final NewbieProtectionService newbieProtectionService;
    private final NewbieProtectionMultification multification;
    private final NewbieProtectionMetrics metrics;
    private final NameTagService nameTagService;
    private final Server server;

    public NewbieProtectionTask(NewbieProtectionService newbieProtectionService, NewbieProtectionMultification multification, NewbieProtectionMetrics metrics, NameTagService nameTagService, Server server) {
        this.newbieProtectionService = newbieProtectionService;
        this.multification = multification;
        this.metrics = metrics;
        this.nameTagService = nameTagService;
        this.server = server;
    }

    @Override
    public void run() {
        for (Newbie newbie : this.newbieProtectionService.newbies()) {
            Player player = this.server.getPlayer(newbie.uniqueId());

            if (player == null) {
                this.newbieProtectionService.removeProtection(newbie.uniqueId());
                continue;
            }

            Instant now = Instant.now();
            Instant protectionEnd = newbie.issuedAt().plus(newbie.protectionTime());

            if (protectionEnd.isBefore(now)) {
                this.newbieProtectionService.endProtection(player);
                this.nameTagService.removeNameTag(player);

                this.multification.player(player.getUniqueId(), cfg -> cfg.protectionEnd);

                this.metrics.addTimeOnProtection(newbie.protectionTime());
            }

        }
    }
}
