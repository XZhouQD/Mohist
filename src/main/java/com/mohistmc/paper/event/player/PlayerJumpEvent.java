package com.mohistmc.paper.event.player;

import com.google.common.base.Preconditions;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Called when the server detects the player is jumping.
 * <p>
 * Added to avoid the overhead and special case logic that many plugins use
 * when checking for jumps via PlayerMoveEvent, this event is fired whenever
 * the server detects that the player is jumping.
 */
public class PlayerJumpEvent extends PlayerEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancel = false;
    @NotNull private Location from;
    @NotNull private Location to;

    public PlayerJumpEvent(@NotNull final Player player, @NotNull final Location from, @NotNull final Location to) {
        super(player);
        this.from = from;
        this.to = to;
    }

    /**
     * Gets the cancellation state of this event. A cancelled event will not
     * be executed in the server, but will still pass to other plugins
     * <p>
     * If a jump event is cancelled, the player will be moved or
     * teleported back to the Location as defined by getFrom(). This will not
     * fire an event
     *
     * @return true if this event is cancelled
     */
    public boolean isCancelled() {
        return cancel;
    }

    /**
     * Sets the cancellation state of this event. A cancelled event will not
     * be executed in the server, but will still pass to other plugins
     * <p>
     * If a jump event is cancelled, the player will be moved or
     * teleported back to the Location as defined by getFrom(). This will not
     * fire an event
     *
     * @param cancel true if you wish to cancel this event
     */
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    /**
     * Gets the location this player jumped from
     *
     * @return Location the player jumped from
     */
    @NotNull
    public Location getFrom() {
        return from;
    }

    /**
     * Sets the location to mark as where the player jumped from
     *
     * @param from New location to mark as the players previous location
     */
    public void setFrom(@NotNull Location from) {
        validateLocation(from);
        this.from = from;
    }

    /**
     * Gets the location this player jumped to
     *
     * This information is based on what the client sends, it typically
     * has little relation to the arc of the jump at any given point.
     *
     * @return Location the player jumped to
     */
    @NotNull
    public Location getTo() {
        return to;
    }

    private void validateLocation(Location loc) {
        Preconditions.checkArgument(loc != null, "Cannot use null location!");
        Preconditions.checkArgument(loc.getWorld() != null, "Cannot use location with null world!");
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
