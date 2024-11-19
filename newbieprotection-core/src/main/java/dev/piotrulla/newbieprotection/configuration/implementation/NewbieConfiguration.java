package dev.piotrulla.newbieprotection.configuration.implementation;

import dev.piotrulla.newbieprotection.NewbieProtectionTime;
import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;

import java.time.Duration;

public class NewbieConfiguration extends OkaeriConfig {

    @Comment("# Protection time for newbies")
    public Duration protectionTime = Duration.ofMinutes(3);

    @Comment({
            "# Protection time type",
            "# REAL_TIME - protection time will be counted in real time",
            "# GAME_TIME - protection time will be counted only when player is online"
    })
    public NewbieProtectionTime protectionTimeType = NewbieProtectionTime.REAL_TIME;

    @Comment("# If true, protection time will be saved in data storage")
    public boolean saveToMemory = true;

    @Comment
    @Comment("# If true, player will recive protection messages")
    public boolean reminderProtection = true;
    public Duration reminderInterval = Duration.ofSeconds(8);

    @Comment
    public NameTag nameTag = new NameTag();

    public static class NameTag extends OkaeriConfig {

        @Comment("# If true, player will have protection name tag")
        @Comment("# Requires PacketEvents!")
        public boolean enabled = true;

        @Comment("# Name tag type")
        @Comment("# PREFIX, SUFFIX")
        public Type type = Type.PREFIX;

        @Comment("# Name tag content")
        public String content = "&e[Newbie] ";

        public enum Type {
            PREFIX,
            SUFFIX
        }
    }



}
