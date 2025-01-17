package org.bukkit.craftbukkit.v1_20_R1.entity;

import org.bukkit.craftbukkit.v1_20_R1.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Illusioner;

public class CraftIllusioner extends CraftSpellcaster implements Illusioner {

    public CraftIllusioner(CraftServer server, net.minecraft.world.entity.monster.Illusioner entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.monster.Illusioner getHandle() {
        return (net.minecraft.world.entity.monster.Illusioner) super.getHandle();
    }

    @Override
    public String toString() {
        return "CraftIllusioner";
    }
}
