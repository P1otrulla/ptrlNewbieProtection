package dev.piotrulla.newbieprotection;

import com.eternalcode.multification.shared.Formatter;
import dev.piotrulla.newbieprotection.metrics.NewbieProtectionMetrics;
import dev.piotrulla.newbieprotection.util.DurationUtil;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.time.Instant;

@Command(name = "newbieprotectionadmin", aliases = "npa")
@Permission("ptrlNewbieProtection.admin")
public class NewbieProtectionAdminCommand {

    private final NewbieProtectionNameTagService newbieProtectionNameTagService;
    private final NewbieProtectionDataRepository newbieProtectionDataRepository;
    private final NewbieProtectionService newbieProtectionService;
    private final NewbieProtectionMultification multification;
    private final NewbieProtectionMetrics metrics;

    public NewbieProtectionAdminCommand(
            NewbieProtectionNameTagService newbieProtectionNameTagService, NewbieProtectionDataRepository newbieProtectionDataRepository, NewbieProtectionService newbieProtectionService,
            NewbieProtectionMultification multification, NewbieProtectionMetrics metrics
    ) {
        this.newbieProtectionNameTagService = newbieProtectionNameTagService;
        this.newbieProtectionDataRepository = newbieProtectionDataRepository;
        this.newbieProtectionService = newbieProtectionService;
        this.multification = multification;
        this.metrics = metrics;
    }

    @Execute(name = "add")
    void add(@Context CommandSender commandSender, @Arg("target") Player target, @Arg("time") Duration time) {
        this.newbieProtectionService.startProtection(target, time);
        this.newbieProtectionNameTagService.applyNameTag(target);

        Formatter formatter = new Formatter()
                .register("{PLAYER}", target.getName())
                .register("{TIME}", DurationUtil.format(time));

        this.metrics.addProtectedPlayer();

        this.multification.viewer(commandSender, cfg -> cfg.command.admin.protectionAdded, formatter);
    }

    @Execute(name = "remove")
    void remove(@Context CommandSender commandSender, @Arg("target") Player target) {
        this.newbieProtectionService.getNewbie(target).ifPresent(newbie -> {
            Instant now = Instant.now();
            Instant issuedAt = newbie.issuedAt();

            this.metrics.addTimeOnProtection(Duration.between(issuedAt, now));
        });

        this.newbieProtectionService.endProtection(target);
        this.newbieProtectionNameTagService.removeNameTag(target);
        this.newbieProtectionDataRepository.remove(target.getUniqueId());

        Formatter formatter = new Formatter()
                .register("{PLAYER}", target.getName());

        this.multification.viewer(commandSender, cfg -> cfg.command.admin.protectionRemoved, formatter);
    }

    @Execute(name = "info")
    void info(@Context CommandSender commandSender, @Arg("target") Player target) {
        Formatter formatter = new Formatter()
                .register("{PLAYER}", target.getName())
                .register("{TIME}", DurationUtil.format(this.newbieProtectionService.getRemainingProtectionTime(target)));

        if (this.newbieProtectionService.isProtected(target)) {
            this.multification.viewer(commandSender, cfg -> cfg.command.admin.protectionInfo, formatter);
            return;
        }

        this.multification.viewer(commandSender, cfg -> cfg.command.admin.notProtected, formatter);
    }
}
