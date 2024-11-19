package dev.piotrulla.newbieprotection;

import dev.piotrulla.newbieprotection.configuration.implementation.NewbieConfiguration;
import dev.piotrulla.newbieprotection.metrics.NewbieProtectionMetrics;
import org.bukkit.Server;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.time.Instant;

public class NewbieProtectionTask implements Runnable {

    private final NewbieProtectionService newbieProtectionService;
    private final NewbieProtectionNameTagServiceImpl nameTagService;
    private final NewbieProtectionDataRepository dataRepository;
    private final NewbieProtectionMultification multification;
    private final NewbieConfiguration configuration;
    private final NewbieProtectionMetrics metrics;
    private final Server server;

    public NewbieProtectionTask(
            NewbieProtectionService newbieProtectionService, NewbieProtectionDataRepository dataRepository,
            NewbieProtectionMultification multification, NewbieConfiguration configuration,
            NewbieProtectionMetrics metrics, NewbieProtectionNameTagServiceImpl nameTagService,
            Server server
    ) {
        this.newbieProtectionService = newbieProtectionService;
        this.dataRepository = dataRepository;
        this.multification = multification;
        this.configuration = configuration;
        this.nameTagService = nameTagService;
        this.metrics = metrics;
        this.server = server;
    }

    @Override
    public void run() {
        if (this.configuration.protectionTimeType == NewbieProtectionTime.REAL_TIME) {
            handleProtectionTime(this::calculateRealTimeEnd);
            return;
        }

        handleProtectionTime(this::calculateGameTimeEnd);
    }

    private void handleProtectionTime(CheckProtectionEndTime checkEndTime) {
        for (Newbie newbie : this.newbieProtectionService.newbies()) {
            Player player = this.server.getPlayer(newbie.uniqueId());
            if (player == null) {
                continue;
            }

            if (checkEndTime.shouldEndProtection(newbie)) {
                processProtectionEnd(player, newbie);
            }
        }
    }

    private void processProtectionEnd(Player player, Newbie newbie) {
        this.newbieProtectionService.endProtection(player);
        this.nameTagService.removeNameTag(player);
        this.multification.player(player.getUniqueId(), cfg -> cfg.protectionEnd);
        this.metrics.addTimeOnProtection(Duration.between(newbie.issuedAt(), Instant.now()));
        this.removeData(newbie);
    }

    private boolean calculateGameTimeEnd(Newbie newbie) {
        Duration timeOnline = newbie.protectionTime().minus(Duration.ofSeconds(1));
        newbie.setProtectionTime(timeOnline);

        this.updateData(newbie);

        return newbie.protectionTime().toSeconds() <= 0;
    }

    private boolean calculateRealTimeEnd(Newbie newbie) {
        Instant protectionEnd = newbie.issuedAt().plus(newbie.protectionTime());

        this.updateData(newbie);

        return protectionEnd.isBefore(Instant.now());
    }

    private void updateData(Newbie newbie) {
        if (this.configuration.saveToMemory) {
            this.dataRepository.update(newbie);
        }
    }

    private void removeData(Newbie newbie) {
        if (this.configuration.saveToMemory) {
            this.dataRepository.remove(newbie);
        }
    }

    @FunctionalInterface
    private interface CheckProtectionEndTime {
        boolean shouldEndProtection(Newbie newbie);
    }
}
