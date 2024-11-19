package dev.piotrulla.newbieprotection;

import dev.piotrulla.newbieprotection.metrics.NewbieProtectionMetrics;
import org.bukkit.Server;
import org.bukkit.entity.Player;

import java.time.Instant;

public class NewbieProtectionTask implements Runnable {

    private final NewbieProtectionService newbieProtectionService;
<<<<<<< Updated upstream
=======
    private final NewbieProtectionNameTagService nameTagService;
    private final NewbieProtectionDataRepository dataRepository;
>>>>>>> Stashed changes
    private final NewbieProtectionMultification multification;
    private final NewbieProtectionMetrics metrics;
    private final NewbieProtectionNameTagServiceImpl newbieProtectionNameTagServiceImpl;
    private final Server server;

<<<<<<< Updated upstream
    public NewbieProtectionTask(NewbieProtectionService newbieProtectionService, NewbieProtectionMultification multification, NewbieProtectionMetrics metrics, NewbieProtectionNameTagServiceImpl newbieProtectionNameTagServiceImpl, Server server) {
=======
    public NewbieProtectionTask(
            NewbieProtectionService newbieProtectionService, NewbieProtectionDataRepository dataRepository,
            NewbieProtectionMultification multification, NewbieConfiguration configuration,
            NewbieProtectionMetrics metrics, NewbieProtectionNameTagService nameTagService, Server server
    ) {
>>>>>>> Stashed changes
        this.newbieProtectionService = newbieProtectionService;
        this.multification = multification;
        this.metrics = metrics;
        this.newbieProtectionNameTagServiceImpl = newbieProtectionNameTagServiceImpl;
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
                this.newbieProtectionNameTagServiceImpl.removeNameTag(player);

                this.multification.player(player.getUniqueId(), cfg -> cfg.protectionEnd);

                this.metrics.addTimeOnProtection(newbie.protectionTime());
            }

        }
    }
}
