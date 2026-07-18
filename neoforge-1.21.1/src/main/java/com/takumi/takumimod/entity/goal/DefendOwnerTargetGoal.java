package com.takumi.takumimod.entity.goal;

import com.takumi.takumimod.entity.CardboardBoxEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;

import java.util.EnumSet;

/**
 * Targets whatever last hurt the owner, so the box steps in to defend them.
 */
public class DefendOwnerTargetGoal extends TargetGoal
{
    private final CardboardBoxEntity box;
    private LivingEntity ownerLastHurtBy;
    private int timestamp;

    public DefendOwnerTargetGoal(CardboardBoxEntity box)
    {
        super(box, false);
        this.box = box;
        this.setFlags(EnumSet.of(Flag.TARGET));
    }

    @Override
    public boolean canUse()
    {
        LivingEntity owner = this.box.getOwnerEntity();
        if (owner == null)
        {
            return false;
        }

        this.ownerLastHurtBy = owner.getLastHurtByMob();
        int lastHurtByTimestamp = owner.getLastHurtByMobTimestamp();
        return lastHurtByTimestamp != this.timestamp
                && this.ownerLastHurtBy != null
                && this.box.canAttack(this.ownerLastHurtBy)
                && this.canAttack(this.ownerLastHurtBy, TargetingConditions.DEFAULT);
    }

    @Override
    public void start()
    {
        this.mob.setTarget(this.ownerLastHurtBy);
        LivingEntity owner = this.box.getOwnerEntity();
        if (owner != null)
        {
            this.timestamp = owner.getLastHurtByMobTimestamp();
        }
        super.start();
    }
}
