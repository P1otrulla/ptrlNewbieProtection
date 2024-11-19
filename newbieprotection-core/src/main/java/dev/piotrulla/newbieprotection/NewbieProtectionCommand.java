package dev.piotrulla.newbieprotection;

import com.eternalcode.multification.shared.Formatter;
import dev.piotrulla.newbieprotection.metrics.NewbieProtectionMetrics;
import dev.piotrulla.newbieprotection.util.DurationUtil;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Command(name = "newbieprotection")
public class NewbieProtectionCommand {

    private final NewbieProtectionNameTagService newbieProtectionNameTagService;
    private final NewbieProtectionDataRepository newbieProtectionDataRepository;
    private final NewbieProtectionService newbieProtectionService;
    private final NewbieProtectionMultification multification;
    private final NewbieProtectionMetrics metrics;

    public NewbieProtectionCommand(
            NewbieProtectionNameTagService newbieProtectionNameTagService, NewbieProtectionDataRepository newbieProtectionDataRepository,
            NewbieProtectionService newbieProtectionService, NewbieProtectionMultification multification, NewbieProtectionMetrics metrics
    ) {
        this.newbieProtectionNameTagService = newbieProtectionNameTagService;
        this.newbieProtectionDataRepository = newbieProtectionDataRepository;
        this.newbieProtectionService = newbieProtectionService;
        this.multification = multification;
        this.metrics = metrics;
    }

    @Execute
    void info(@Context Player player) {
        if (this.newbieProtectionService.isProtected(player)) {
            Formatter formatter = new Formatter()
                    .register("{TIME}", DurationUtil.format(this.newbieProtectionService.getRemainingProtectionTime(player)));

            this.multification.player(player.getUniqueId(), cfg -> cfg.command.infoCommand, formatter);
            return;
        }

        this.multification.player(player.getUniqueId(), cfg -> cfg.command.notProtected);
    }

    @Execute(name = "remove")
    void remove(@Context Player player) {
        UUID uniqueId = player.getUniqueId();

        if (this.newbieProtectionService.isProtected(player)) {
            this.newbieProtectionService.getNewbie(player).ifPresent(newbie -> {
                Instant now = Instant.now();
                Instant issuedAt = newbie.issuedAt();

                this.metrics.addTimeOnProtection(Duration.between(issuedAt, now));
            });

            this.newbieProtectionService.endProtection(player);
            this.newbieProtectionNameTagService.removeNameTag(player);
            this.newbieProtectionDataRepository.remove(uniqueId);

            this.multification.player(uniqueId, cfg -> cfg.command.protectionRemoved);
            return;
        }

        this.multification.player(uniqueId, cfg -> cfg.command.notProtected);
    }
}
