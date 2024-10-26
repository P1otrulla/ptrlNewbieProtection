package dev.piotrulla.newbieprotection;

import dev.piotrulla.newbieprotection.event.EventCaller;
import dev.piotrulla.newbieprotection.event.NewbieProtectionEndEvent;
import dev.piotrulla.newbieprotection.event.NewbieProtectionStartEvent;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class NewbieProtectionServiceImpl implements NewbieProtectionService {

    private final Map<UUID, NewbieProtectionUser> newbies = new HashMap<>();
    private final EventCaller eventCaller;

    public NewbieProtectionServiceImpl(EventCaller eventCaller) {
        this.eventCaller = eventCaller;
    }

    @Override
    public void startProtection(Player player, Duration duration) {
        NewbieProtectionUser newbie = new NewbieProtectionUser(player.getUniqueId());
        newbie.setProtectionTime(duration);
        newbie.setIssuedAt(Instant.now());
        this.newbies.put(player.getUniqueId(), newbie);

        this.eventCaller.callEvent(new NewbieProtectionStartEvent(player, newbie));
    }

    @Override
    public void endProtection(Player player) {
        this.newbies.remove(player.getUniqueId());

        this.eventCaller.callEvent(new NewbieProtectionEndEvent(player));
    }

    @Override
    public void removeProtection(UUID uniqueId) {
        this.newbies.remove(uniqueId);
    }

    @Override
    public Optional<Newbie> getNewbie(Player player) {
        return Optional.ofNullable(this.newbies.get(player.getUniqueId()));
    }

    @Override
    public boolean isProtected(Player player) {
        if (!this.newbies.containsKey(player.getUniqueId())) {
            return false;
        }

        NewbieProtectionUser newbie = this.newbies.get(player.getUniqueId());
        Instant protectionEnd = newbie.issuedAt().plus(newbie.protectionTime());
        Instant now = Instant.now();

        if (protectionEnd.isBefore(now)) {
            this.newbies.remove(player.getUniqueId());
            return false;
        }

        return true;
    }

    @Override
    public Duration getRemainingProtectionTime(Player player) {
        if (!this.newbies.containsKey(player.getUniqueId())) {
            return Duration.ZERO;
        }

        NewbieProtectionUser newbie = this.newbies.get(player.getUniqueId());

        Instant protectionEnd = newbie.issuedAt().plus(newbie.protectionTime());
        Instant now = Instant.now();

        Duration between = Duration.between(now, protectionEnd);

        if (between.isNegative() || between.isZero()) {
            return Duration.ZERO;
        }

        return between;
    }

    @Override
    public Collection<Newbie> newbies() {
        return Collections.unmodifiableCollection(this.newbies.values());
    }
}
