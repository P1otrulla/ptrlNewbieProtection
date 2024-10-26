package dev.piotrulla.newbieprotection;

import com.eternalcode.multification.shared.Formatter;
import dev.piotrulla.newbieprotection.configuration.implementation.NewbieConfiguration;
import dev.piotrulla.newbieprotection.metrics.NewbieProtectionMetrics;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.projectiles.ProjectileSource;

public class NewbieProtectionController implements Listener {

    private final NewbieProtectionMultification multification;
    private final NewbieProtectionService protectionService;
    private final NewbieConfiguration configuration;
    private final NewbieProtectionMetrics metrics;

    public NewbieProtectionController(NewbieProtectionMultification multification, NewbieProtectionService protectionService, NewbieConfiguration configuration, NewbieProtectionMetrics metrics) {
        this.multification = multification;
        this.protectionService = protectionService;
        this.configuration = configuration;
        this.metrics = metrics;
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
    }

    @EventHandler
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
                    .register("{TIME}", protectionService.getRemainingProtectionTime(victim));

            this.multification.player(attacker.getUniqueId(), cfg -> cfg.cantAttackProtected, formatter);
            return;
        }

        if (this.protectionService.isProtected(attacker)) {
            event.setCancelled(true);

            Formatter formatter = new Formatter()
                    .register("{PLAYER}", attacker.getName())
                    .register("{TIME}", protectionService.getRemainingProtectionTime(attacker));

            this.multification.player(attacker.getUniqueId(), cfg -> cfg.cantAttackWhenProtected, formatter);
        }
    }
}

