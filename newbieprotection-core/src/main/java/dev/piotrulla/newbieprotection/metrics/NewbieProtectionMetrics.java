package dev.piotrulla.newbieprotection.metrics;

import java.time.Duration;

public interface MetricsService {

    void addProtectedPlayer();

    void addTimeOnProtection(Duration duration);

}
