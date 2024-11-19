package com.mohistmc.bukkit;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.bukkit.craftbukkit.v1_20_R1.event.CraftEventFactory;

public class DeathEventFix {

    public static org.bukkit.event.entity.EntityDeathEvent fix(DamageSource p_21192_, LivingEntity livingEntity) {
         return CraftEventFactory.callEntityDeathEvent(livingEntity, livingEntity.drops, () -> {
             final LivingEntity entityliving = livingEntity.getKillCredit();
             if (livingEntity.deathScore >= 0 && entityliving != null) {
                 entityliving.awardKillScore(livingEntity, livingEntity.deathScore, p_21192_);
             }
         });
    }
}
