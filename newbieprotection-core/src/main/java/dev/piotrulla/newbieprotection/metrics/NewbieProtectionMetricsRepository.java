package dev.piotrulla.newbieprotection.metrics;

import eu.okaeri.configs.OkaeriConfig;

import java.time.Duration;

public class NewbieProtectionMetricsRepository extends OkaeriConfig implements NewbieProtectionMetrics {

    public int protectedPlayers = 0;
    public Duration timeOnProtection = Duration.ZERO;

    @Override
    public void addProtectedPlayer() {
        this.protectedPlayers++;
        this.save();
    }

    @Override
    public void addTimeOnProtection(Duration duration) {
        this.timeOnProtection = this.timeOnProtection.plus(duration);
        this.save();
    }
}
