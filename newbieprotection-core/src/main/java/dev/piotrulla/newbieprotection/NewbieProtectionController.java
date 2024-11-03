package dev.piotrulla.newbieprotection;

import com.eternalcode.multification.shared.Formatter;
import dev.piotrulla.newbieprotection.configuration.implementation.NewbieConfiguration;
import dev.piotrulla.newbieprotection.metrics.NewbieProtectionMetrics;
import dev.piotrulla.newbieprotection.util.DurationUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.projectiles.ProjectileSource;

public class NewbieProtectionController implements Listener {

    private final NewbieProtectionMultification multification;
    private final NewbieProtectionService protectionService;
    private final NewbieConfiguration configuration;
    private final NewbieProtectionMetrics metrics;
    private final NewbieProtectionNameTagServiceImpl newbieProtectionNameTagServiceImpl;

    public NewbieProtectionController(NewbieProtectionMultification multification, NewbieProtectionService protectionService, NewbieConfiguration configuration, NewbieProtectionMetrics metrics, NewbieProtectionNameTagServiceImpl newbieProtectionNameTagServiceImpl) {
        this.multification = multification;
        this.protectionService = protectionService;
        this.configuration = configuration;
        this.metrics = metrics;
        this.newbieProtectionNameTagServiceImpl = newbieProtectionNameTagServiceImpl;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (player.hasPlayedBefore()) {
            return;
        }

        this.protectionService.startProtection(player, this.configuration.protectionTime);
        this.multification.player(player.getUniqueId(), cfg -> cfg.newbieOnJoinMessage);
        this.metrics.addProtectedPlayer();

        if (this.configuration.nameTag.enabled) {
            Bukkit.getScheduler().runTaskLater(NewbieProtectionPlugin.getProvidingPlugin(NewbieProtectionPlugin.class), () -> this.newbieProtectionNameTagServiceImpl.applyNameTag(player), 40L);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerAttack(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player victim)) {
            return;
        }

        Player attacker = null;

        if (event.getDamager() instanceof Player) {
            attacker = (Player) event.getDamager();
        }

        if (event.getDamager() instanceof Projectile projectile) {
            ProjectileSource shooter = projectile.getShooter();
            if (shooter instanceof Player) {
                attacker = (Player) shooter;
            }
        }

        if (attacker == null) {
            return;
        }

        if (this.protectionService.isProtected(victim)) {
            event.setCancelled(true);

            Formatter formatter = new Formatter()
                    .register("{PLAYER}", victim.getName())
                    .register("{TIME}", DurationUtil.format(this.protectionService.getRemainingProtectionTime(victim)));

            this.multification.player(attacker.getUniqueId(), cfg -> cfg.cantAttackProtected, formatter);
            return;
        }

        if (this.protectionService.isProtected(attacker)) {
            event.setCancelled(true);

            Formatter formatter = new Formatter()
                    .register("{PLAYER}", attacker.getName())
                    .register("{TIME}", DurationUtil.format(this.protectionService.getRemainingProtectionTime(victim)));

            this.multification.player(attacker.getUniqueId(), cfg -> cfg.cantAttackWhenProtected, formatter);
        }
    }
}

