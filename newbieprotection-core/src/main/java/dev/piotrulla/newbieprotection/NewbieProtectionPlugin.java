package dev.piotrulla.newbieprotection;

import com.eternalcode.commons.adventure.AdventureLegacyColorPostProcessor;
import com.eternalcode.commons.adventure.AdventureLegacyColorPreProcessor;
import com.eternalcode.commons.adventure.AdventureUrlPostProcessor;
import com.eternalcode.multification.shared.Formatter;
import dev.piotrulla.newbieprotection.configuration.ConfigService;
import dev.piotrulla.newbieprotection.configuration.implementation.CommandConfiguration;
import dev.piotrulla.newbieprotection.configuration.implementation.MessagesConfiguration;
import dev.piotrulla.newbieprotection.configuration.implementation.NewbieConfiguration;
import dev.piotrulla.newbieprotection.configuration.implementation.command.Command;
import dev.piotrulla.newbieprotection.configuration.implementation.command.SubCommand;
import dev.piotrulla.newbieprotection.event.EventCaller;
import dev.piotrulla.newbieprotection.metrics.NewbieProtectionMetricsRepository;
import dev.piotrulla.newbieprotection.util.DurationUtil;
import dev.rollczi.litecommands.LiteCommands;
import dev.rollczi.litecommands.adventure.LiteAdventureExtension;
import dev.rollczi.litecommands.bukkit.LiteBukkitFactory;
import dev.rollczi.litecommands.meta.Meta;
import dev.rollczi.litecommands.schematic.Schematic;
import net.kyori.adventure.platform.AudienceProvider;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.megavex.scoreboardlibrary.api.ScoreboardLibrary;
import net.megavex.scoreboardlibrary.api.exception.NoPacketAdapterAvailableException;
import net.megavex.scoreboardlibrary.api.noop.NoopScoreboardLibrary;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SimplePie;
import org.bstats.charts.SingleLineChart;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class NewbieProtectionPlugin extends JavaPlugin {

    private AudienceProvider audienceProvider;
    private ScoreboardLibrary scoreboardLibrary;

    private NewbieProtectionNameTagServiceImpl nameTagService;

    private LiteCommands<CommandSender> liteCommands;

    @Override
    public void onEnable() {
        ConfigService configService = new ConfigService();

        NewbieConfiguration configuration = configService.create(NewbieConfiguration.class, new File(this.getDataFolder(), "config.yml"));
        MessagesConfiguration messages = configService.create(MessagesConfiguration.class, new File(this.getDataFolder(), "messages.yml"));
        CommandConfiguration commandsConfig = configService.create(CommandConfiguration.class, new File(this.getDataFolder(), "commandsConfig.yml"));
        NewbieProtectionMetricsRepository metricsRepository = configService.create(NewbieProtectionMetricsRepository.class, new File(this.getDataFolder(), "data/metrics.dat"));
        NewbieProtectionDataInMemory dataRepository = configService.create(NewbieProtectionDataInMemory.class, new File(this.getDataFolder(), "data/protected.dat"));

        this.audienceProvider = BukkitAudiences.create(this);
        MiniMessage miniMessage = MiniMessage.builder()
                .postProcessor(new AdventureUrlPostProcessor())
                .postProcessor(new AdventureLegacyColorPostProcessor())
                .preProcessor(new AdventureLegacyColorPreProcessor())
                .build();

        NewbieProtectionMultification multification = new NewbieProtectionMultification(this.audienceProvider, messages, miniMessage);

        Server server = this.getServer();

        EventCaller eventCaller = new EventCaller(server);

        if (configuration.nameTag.enabled) {
            try {
                this.scoreboardLibrary = ScoreboardLibrary.loadScoreboardLibrary(this);
                System.out.println("Scoreboard library loaded");
            }
            catch (NoPacketAdapterAvailableException ignored) {
                this.scoreboardLibrary = new NoopScoreboardLibrary();
            }
        }

        this.nameTagService = new NewbieProtectionNameTagServiceImpl(configuration, this.scoreboardLibrary, miniMessage, server, this.getLogger());
        this.nameTagService.initialize();

        NewbieProtectionService newbieProtectionService = new NewbieProtectionServiceImpl(configuration, eventCaller);
        NewbieProtectionAPIProvider.initialize(new NewbieProtectionAPIImpl(this.nameTagService, dataRepository, newbieProtectionService));

        server.getPluginManager().registerEvents(new NewbieProtectionController(multification, newbieProtectionService, configuration, metricsRepository, this.nameTagService, dataRepository), this);

        server.getScheduler().runTaskTimer(this, new NewbieProtectionTask(newbieProtectionService, dataRepository, multification, configuration, metricsRepository, this.nameTagService, server), 20L, 20L);

        if (configuration.reminderProtection) {
            long reminderTime = configuration.reminderInterval.toMillis() / 50;

            server.getScheduler().runTaskTimer(this, new NewbieProtectionReminderTask(newbieProtectionService, multification, server), reminderTime, reminderTime);
        }

        Metrics metrics = new Metrics(this, 23724);
        metrics.addCustomChart(new SingleLineChart("protected_players", () -> metricsRepository.protectedPlayers));
        metrics.addCustomChart(new SimplePie("total_time_during_protection", () -> DurationUtil.format(metricsRepository.timeOnProtection)));

        this.liteCommands = LiteBukkitFactory.builder("ptrl-newbieprotection", this, server)
                .extension(new LiteAdventureExtension<>(), settings -> settings
                        .miniMessage(true)
                        .legacyColor(true)
                        .colorizeArgument(true)
                        .serializer(miniMessage)
                )

                .missingPermission((invocation, missingPermissions, resultHandlerChain) -> {
                    Formatter formatter = new Formatter()
                            .register("{PERMISSIONS}", missingPermissions.asJoinedText());

                    multification.viewer(invocation.sender(), messagesConfig -> messagesConfig.commands.noPermission, formatter);
                })
                .editorGlobal(context -> {
                    Command command = commandsConfig.commands.get(context.name());

                    if (command == null) {
                        return context;
                    }

                    for (String child : command.subCommands().keySet()) {
                        SubCommand subCommand = command.subCommands().get(child);

                        context = context.editChild(child, editor -> editor.name(subCommand.name())
                                .aliases(subCommand.aliases())
                                .applyMeta(meta -> meta.list(Meta.PERMISSIONS, permissions -> permissions.addAll(command.permissions())))
                                .enabled(subCommand.isEnabled())
                        );
                    }

                    return context.name(command.name())
                            .aliases(command.aliases())
                            .applyMeta(meta -> meta.list(Meta.PERMISSIONS, permissions -> permissions.addAll(command.permissions())))
                            .enabled(command.isEnabled());
                })
                .invalidUsage((invocation, invalidUsage, resultHandlerChain) -> {
                    CommandSender sender = invocation.sender();
                    Schematic schematic = invalidUsage.getSchematic();

                    if (schematic.isOnlyFirst()) {
                        Formatter formatter = new Formatter()
                                .register("{USAGE}", schematic.first());

                        multification.viewer(sender, messagesConfig -> messagesConfig.commands.invalidUsageOne, formatter);
                        return;
                    }

                    multification.viewer(sender, messagesConfig -> messagesConfig.commands.invalidUsageMany);
                    schematic.all().forEach(usage -> {
                        Formatter formatter = new Formatter()
                                .register("{USAGE}", usage);

                        multification.viewer(sender, messagesConfig -> messagesConfig.commands.invalidUsageManyItem, formatter);
                    });
                })
                .commands(
                        new NewbieProtectionAdminCommand(this.nameTagService, newbieProtectionService, multification, metricsRepository),
                        new NewbieProtectionCommand(this.nameTagService, newbieProtectionService, multification, metricsRepository)
                )
                .build();

    }

    @Override
    public void onDisable() {
        this.scoreboardLibrary.close();
        this.audienceProvider.close();
        this.liteCommands.unregister();
        this.nameTagService.shutdown();
    }
}
