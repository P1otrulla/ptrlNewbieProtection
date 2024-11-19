package dev.piotrulla.newbieprotection;

import eu.okaeri.configs.OkaeriConfig;

import java.time.Duration;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class NewbieProtectionDataInMemory extends OkaeriConfig implements NewbieProtectionDataRepository {

    private Set<Entry> entries = new HashSet<>();

    @Override
    public boolean isExists(UUID uniqueId) {
        return this.entries.stream().anyMatch(entry -> entry.uniqueId().equals(uniqueId));
    }

    @Override
    public void save(Newbie newbie) {
        this.entries.add(new Entry(newbie.uniqueId(), newbie.issuedAt(), newbie.protectionTime()));
        this.save();
    }

    @Override
    public void remove(UUID uniqueId) {
        this.entries.removeIf(entry -> entry.uniqueId().equals(uniqueId));
        this.save();
    }

    @Override
    public void remove(Newbie newbie) {
        this.remove(newbie.uniqueId());
    }

    @Override
    public void update(Newbie newbie) {
        this.entries.removeIf(entry -> entry.uniqueId().equals(newbie.uniqueId()));
        this.save(newbie);
    }

    @Override
    public Newbie load(UUID uniqueId) {
        return this.entries.stream()
                .filter(entry -> entry.uniqueId().equals(uniqueId))
                .findFirst()
                .map(entry -> new NewbieProtectionUser(uniqueId, entry.issuedAt(), entry.protectionTime()))
                .orElse(null);
    }

    public static class Entry extends OkaeriConfig {

        private UUID uniqueId;
        private Instant issuedAt;
        private Duration protectionTime;

        public Entry(UUID uniqueId, Instant issuedAt, Duration protectionTime) {
            this.uniqueId = uniqueId;
            this.issuedAt = issuedAt;
            this.protectionTime = protectionTime;
        }

        UUID uniqueId() {
            return this.uniqueId;
        }

        Instant issuedAt() {
            return this.issuedAt;
        }

        Duration protectionTime() {
            return this.protectionTime;
        }
    }
}
