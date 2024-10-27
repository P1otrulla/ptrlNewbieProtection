package dev.piotrulla.newbieprotection.nametag;

import dev.piotrulla.newbieprotection.NewbieProtectionNameTagService;
import dev.piotrulla.newbieprotection.configuration.implementation.NewbieConfiguration;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.megavex.scoreboardlibrary.api.ScoreboardLibrary;
import net.megavex.scoreboardlibrary.api.team.ScoreboardTeam;
import net.megavex.scoreboardlibrary.api.team.TeamDisplay;
import net.megavex.scoreboardlibrary.api.team.TeamManager;
import org.bukkit.entity.Player;

public class NameTagService implements NewbieProtectionNameTagService {

    private final NewbieConfiguration.NameTag nameTagConfig;
    private final ScoreboardLibrary scoreboardLibrary;
    private final MiniMessage miniMessage;

    public NameTagService(NewbieConfiguration.NameTag nameTagConfig, ScoreboardLibrary scoreboardLibrary, MiniMessage miniMessage) {
        this.nameTagConfig = nameTagConfig;
        this.scoreboardLibrary = scoreboardLibrary;
        this.miniMessage = miniMessage;
    }

    @Override
    public void applyNameTag(Player player) {
        TeamManager teamManager = this.scoreboardLibrary.createTeamManager();
        ScoreboardTeam team = teamManager.createIfAbsent("protected_players");

        TeamDisplay display = team.createDisplay();

        switch (this.nameTagConfig.type) {
            case PREFIX -> display.prefix(this.miniMessage.deserialize(this.nameTagConfig.content));
            case SUFFIX -> display.suffix(this.miniMessage.deserialize(this.nameTagConfig.content));
            case COLORED_NICK -> display.playerColor(NamedTextColor.NAMES.value(this.nameTagConfig.color.name()));
        }
        display.addEntry(player.getName());

        teamManager.addPlayer(player);
        teamManager.close();
    }

    @Override
    public void removeNameTag(Player player) {
        TeamManager teamManager = this.scoreboardLibrary.createTeamManager();
        ScoreboardTeam team = teamManager.createIfAbsent("protected_players");

        TeamDisplay display = team.defaultDisplay();
        display.removeEntry(player.getName());

        teamManager.close();
    }

}
