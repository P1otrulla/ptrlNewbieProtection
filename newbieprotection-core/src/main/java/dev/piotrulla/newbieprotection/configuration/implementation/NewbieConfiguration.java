package dev.piotrulla.newbieprotection.configuration;

import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;

import java.time.Duration;

public class NewbieConfiguration extends OkaeriConfig {

    @Comment("# If true, player will be protected on join based on bukkit world players list")
    public boolean protectionOnJoin = true;

    @Comment
    @Comment("# If true, player will have prefix")
    public boolean protectionNameTag = true;

    @Comment
    @Comment("# Protection time for newbies")
    public Duration protectionTime = Duration.ofMinutes(3);

    @Comment
    @Comment("# If true, player will have protection message")
    public boolean announceProtection = true;
    public Duration announceProtectionTime = Duration.ofSeconds(25);



}
