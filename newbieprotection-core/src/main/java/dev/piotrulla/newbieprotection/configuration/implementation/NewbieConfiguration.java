package dev.piotrulla.newbieprotection.configuration.implementation;

import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.ChatColor;
import org.checkerframework.checker.units.qual.C;

import java.time.Duration;

public class NewbieConfiguration extends OkaeriConfig {

    @Comment
    @Comment("# Protection time for newbies")
    public Duration protectionTime = Duration.ofMinutes(3);

    @Comment
    @Comment("# If true, player will have protection message")
    public boolean reminderProtection = true;
    public Duration reminderInterval = Duration.ofSeconds(8);

    @Comment
    public NameTag nameTag = new NameTag();

    public static class NameTag extends OkaeriConfig {

        @Comment("# If true, player will have protection name tag")
        @Comment("# Requires PacketEvents!")
        public boolean enabled = true;

        @Comment
        @Comment("# Name tag type")
        public Type type = Type.PREFIX;

        @Comment
        @Comment("# Name tag content")
        public String content = "&e[Newbie] &r";

        @Comment
        @Comment("# Name tag color")
        public ChatColor color = ChatColor.YELLOW;

        public enum Type {
            PREFIX,
            SUFFIX,
            COLORED_NICK
        }
    }



}
