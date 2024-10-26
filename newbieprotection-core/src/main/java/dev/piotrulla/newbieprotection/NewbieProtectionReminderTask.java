package dev.piotrulla.newbieprotection;

import com.eternalcode.multification.shared.Formatter;
import dev.piotrulla.newbieprotection.util.DurationUtil;
import org.bukkit.Server;
import org.bukkit.entity.Player;

public class NewbieProtectionReminderTask implements Runnable {

    private final NewbieProtectionService newbieProtectionService;
    private final NewbieProtectionMultification multification;
    private final Server server;

    public NewbieProtectionReminderTask(NewbieProtectionService newbieProtectionService, NewbieProtectionMultification multification, Server server) {
        this.newbieProtectionService = newbieProtectionService;
        this.multification = multification;
        this.server = server;
    }

    @Override
    public void run() {
        for (Newbie newbie : this.newbieProtectionService.newbies()) {
            Player player = this.server.getPlayer(newbie.uniqueId());

            if (player == null) {
                continue;
            }

            Formatter formatter = new Formatter()
                    .register("{TIME}", DurationUtil.format(this.newbieProtectionService.getRemainingProtectionTime(player)))
                    .register("{PLAYER}", player.getName());

            this.multification.player(player.getUniqueId(), cfg -> cfg.reminderTask, formatter);
        }
    }
}
