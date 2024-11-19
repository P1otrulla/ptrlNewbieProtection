package dev.piotrulla.newbieprotection;

import com.eternalcode.multification.adventure.AudienceConverter;
import com.eternalcode.multification.bukkit.BukkitMultification;
import com.eternalcode.multification.translation.TranslationProvider;
import dev.piotrulla.newbieprotection.configuration.implementation.MessagesConfiguration;
import net.kyori.adventure.platform.AudienceProvider;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.ComponentSerializer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class NewbieProtectionMultification extends BukkitMultification<MessagesConfiguration> {

    private final AudienceProvider audienceProvider;
    private final MessagesConfiguration messagesCfg;
    private final MiniMessage miniMessage;

    public NewbieProtectionMultification(
            AudienceProvider audienceProvider, MessagesConfiguration messagesCfg,
            MiniMessage miniMessage
    ) {
        this.audienceProvider = audienceProvider;
        this.messagesCfg = messagesCfg;
        this.miniMessage = miniMessage;
    }

    @Override
    protected @NotNull TranslationProvider<MessagesConfiguration> translationProvider() {
        return locale -> this.messagesCfg;
    }

    @Override
    protected @NotNull ComponentSerializer<Component, Component, String> serializer() {
        return this.miniMessage;
    }

    @Override
    protected @NotNull AudienceConverter<CommandSender> audienceConverter() {
        return commandSender -> {
            if (commandSender instanceof Player player) {
                return this.audienceProvider.player(player.getUniqueId());
            }

            return this.audienceProvider.console();
        };
    }
}
