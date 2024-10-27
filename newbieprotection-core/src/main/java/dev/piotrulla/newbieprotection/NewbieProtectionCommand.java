package dev.piotrulla.newbieprotection;

import com.eternalcode.multification.shared.Formatter;
import dev.piotrulla.newbieprotection.metrics.NewbieProtectionMetrics;
import dev.piotrulla.newbieprotection.nametag.NameTagService;
import dev.piotrulla.newbieprotection.util.DurationUtil;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.time.Instant;

@Command(name = "newbieprotection")
public class NewbieProtectionCommand {

    private final NewbieProtectionService newbieProtectionService;
    private final NewbieProtectionMultification multification;
    private final NewbieProtectionMetrics metrics;
    private final NameTagService nameTagService;

    public NewbieProtectionCommand(NewbieProtectionService newbieProtectionService, NewbieProtectionMultification multification, NewbieProtectionMetrics metrics, NameTagService nameTagService) {
        this.newbieProtectionService = newbieProtectionService;
        this.multification = multification;
        this.metrics = metrics;
        this.nameTagService = nameTagService;
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
        if (this.newbieProtectionService.isProtected(player)) {
            this.newbieProtectionService.getNewbie(player).ifPresent(newbie -> {
                Instant now = Instant.now();
                Instant issuedAt = newbie.issuedAt();

                this.metrics.addTimeOnProtection(Duration.between(issuedAt, now));
            });

            this.newbieProtectionService.endProtection(player);
            this.nameTagService.removeNameTag(player);


            this.multification.player(player.getUniqueId(), cfg -> cfg.command.protectionRemoved);
            return;
        }

        this.multification.player(player.getUniqueId(), cfg -> cfg.command.notProtected);
    }
}
