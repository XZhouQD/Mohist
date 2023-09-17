package org.spigotmc;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.player.EntityPlayerMP;

public class TrackingRange {

    /**
     * Gets the range an entity should be 'tracked' by players and visible in
     * the client.
     *
     * @param entity
     * @param defaultRange Default range defined by Mojang
     * @return
     */
    public static int getEntityTrackingRange(Entity entity, int defaultRange) {
        SpigotWorldConfig config = entity.world.spigotConfig;
        if (entity instanceof EntityPlayerMP) {
            return config.playerTrackingRange;
        } else if (entity.activationType == 1) {
            return config.monsterTrackingRange;
        } else if (entity instanceof EntityGhast) {
            return config.monsterTrackingRange > config.monsterActivationRange ? config.monsterTrackingRange : config.monsterActivationRange;
        } else if (entity.activationType == 2) {
            return config.animalTrackingRange;
        } else {
            return !(entity instanceof EntityItemFrame)
                    && !(entity instanceof EntityPainting)
                    && !(entity instanceof EntityItem)
                    && !(entity instanceof EntityXPOrb)
                    ? config.otherTrackingRange
                    : config.miscTrackingRange;
        }
    }

}
