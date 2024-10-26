package dev.piotrulla.newbieprotection.event;

import dev.piotrulla.newbieprotection.Newbie;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class NewbieProtectionStartEvent extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Player player;
    private final Newbie newbie;

    public NewbieProtectionStartEvent(Player player, Newbie newbie) {
        this.player = player;
        this.newbie = newbie;
    }

    public Player getPlayer() {
        return this.player;
    }

    public Newbie getNewbie() {
        return this.newbie;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
