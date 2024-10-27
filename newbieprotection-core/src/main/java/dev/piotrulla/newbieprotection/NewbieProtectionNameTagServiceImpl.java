package dev.piotrulla.newbieprotection;

import dev.piotrulla.newbieprotection.configuration.implementation.NewbieConfiguration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.megavex.scoreboardlibrary.api.ScoreboardLibrary;
import net.megavex.scoreboardlibrary.api.team.ScoreboardTeam;
import net.megavex.scoreboardlibrary.api.team.TeamDisplay;
import net.megavex.scoreboardlibrary.api.team.TeamManager;
import org.bukkit.entity.Player;

public class NewbieProtectionNameTagServiceImpl implements NewbieProtectionNameTagService {

    private final NewbieConfiguration newbieConfiguration;
    private final ScoreboardLibrary scoreboardLibrary;
    private final MiniMessage miniMessage;
    private TeamManager teamManager;

    public NewbieProtectionNameTagServiceImpl(NewbieConfiguration newbieConfiguration, ScoreboardLibrary scoreboardLibrary, MiniMessage miniMessage) {
        this.newbieConfiguration = newbieConfiguration;
        this.scoreboardLibrary = scoreboardLibrary;
        this.miniMessage = miniMessage;
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
            default -> throw new IllegalStateException("Unexpected value: " + this.newbieConfiguration.nameTag.type + " in NameTagType");
        }
        display.addEntry(player.getName());
        this.teamManager.addPlayer(player);

        team.display(player, display);
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
