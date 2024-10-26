package dev.piotrulla.newbieprotection.metrics;

import java.time.Duration;

public interface NewbieProtectionMetrics {

    void addProtectedPlayer();

    void addTimeOnProtection(Duration duration);

}
