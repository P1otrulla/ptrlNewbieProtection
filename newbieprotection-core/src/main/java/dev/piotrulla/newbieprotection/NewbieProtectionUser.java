package dev.piotrulla.newbieprotection;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

public class NewbieProtectionUser implements Newbie {

    private final UUID uniqueId;

    private Instant issuedAt;
    private Duration protectionTime;

    public NewbieProtectionUser(UUID uniqueId) {
        this.uniqueId = uniqueId;
    }

    public NewbieProtectionUser(UUID uniqueId, Instant issuedAt, Duration protectionTime) {
        this.uniqueId = uniqueId;
        this.issuedAt = issuedAt;
        this.protectionTime = protectionTime;
    }

    @Override
    public UUID uniqueId() {
        return this.uniqueId;
    }

    @Override
    public Instant issuedAt() {
        return this.issuedAt;
    }

    @Override
    public void setIssuedAt(Instant issuedAt) {
        this.issuedAt = issuedAt;
    }

    @Override
    public Duration protectionTime() {
        return this.protectionTime;
    }

    @Override
    public void setProtectionTime(Duration protectionTime) {
        this.protectionTime = protectionTime;
    }
}
