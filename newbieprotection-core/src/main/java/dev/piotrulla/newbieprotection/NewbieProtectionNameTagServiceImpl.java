package dev.piotrulla.newbieprotection;

import dev.piotrulla.newbieprotection.configuration.implementation.NewbieConfiguration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.megavex.scoreboardlibrary.api.ScoreboardLibrary;
import net.megavex.scoreboardlibrary.api.team.ScoreboardTeam;
import net.megavex.scoreboardlibrary.api.team.TeamDisplay;
import net.megavex.scoreboardlibrary.api.team.TeamManager;
import org.bukkit.Server;
import org.bukkit.entity.Player;

import java.util.logging.Logger;

public class NewbieProtectionNameTagServiceImpl implements NewbieProtectionNameTagService {

    private final NewbieConfiguration newbieConfiguration;
    private final ScoreboardLibrary scoreboardLibrary;
    private final MiniMessage miniMessage;
    private final Server server;
    private final Logger logger;
    private TeamManager teamManager;
    
    public NewbieProtectionNameTagServiceImpl(
            NewbieConfiguration newbieConfiguration, ScoreboardLibrary scoreboardLibrary,
            MiniMessage miniMessage, Server server, Logger logger
    ) {
        this.newbieConfiguration = newbieConfiguration;
        this.scoreboardLibrary = scoreboardLibrary;
        this.miniMessage = miniMessage;
        this.server = server;
        this.logger = logger;
    }

    @Override
    public void initialize() {
        this.teamManager = this.scoreboardLibrary.createTeamManager();
    }

    @Override
    public void applyNameTag(Player player) {
        ScoreboardTeam team = this.teamManager.createIfAbsent("protected_players");

        TeamDisplay display = team.defaultDisplay();

        switch (this.newbieConfiguration.nameTag.type) {
            case PREFIX -> display.prefix(this.miniMessage.deserialize(this.newbieConfiguration.nameTag.content));
            case SUFFIX -> display.suffix(this.miniMessage.deserialize(this.newbieConfiguration.nameTag.content));
            default -> {
                display.prefix(this.miniMessage.deserialize(this.newbieConfiguration.nameTag.content));

                this.logger.warning("Unexpected value: " + this.newbieConfiguration.nameTag.type + " in configuration file in section NameTagType, using prefix as default");
            }
        }
        display.addEntry(player.getName());
        this.teamManager.addPlayer(player);

        team.display(player, display);

        for (Player onlinePlayer : this.server.getOnlinePlayers()) {
            if (onlinePlayer.equals(player)) {
                continue;
            }

            this.teamManager.addPlayer(onlinePlayer);
        }
    }

    @Override
    public void removeNameTag(Player player) {
        ScoreboardTeam team = this.teamManager.createIfAbsent("protected_players");

        TeamDisplay display = team.defaultDisplay();
        display.removeEntry(player.getName());
    }

    @Override
    public void shutdown() {
        this.teamManager.close();
    }

}
