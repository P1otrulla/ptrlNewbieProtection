package dev.piotrulla.newbieprotection.configuration.implementation;

import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;

import java.time.Duration;

public class NewbieConfiguration extends OkaeriConfig {

    @Comment
    @Comment("# Protection time for newbies")
    public Duration protectionTime = Duration.ofMinutes(3);

    @Comment
    @Comment("# If true, player will have protection message")
    public boolean reminderProtection = true;
    public Duration reminderInterval = Duration.ofSeconds(8);



}
