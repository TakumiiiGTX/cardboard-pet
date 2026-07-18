package com.takumi.takumimod.entity.goal;

import com.takumi.takumimod.entity.CardboardBoxEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;

import java.util.EnumSet;

/**
 * Targets whatever the owner last attacked, so the box piles on to help.
 */
public class AssistOwnerTargetGoal extends TargetGoal
{
    private final CardboardBoxEntity box;
    private LivingEntity ownerLastHurt;
    private int timestamp;

    public AssistOwnerTargetGoal(CardboardBoxEntity box)
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

        this.ownerLastHurt = owner.getLastHurtMob();
        int lastHurtTimestamp = owner.getLastHurtMobTimestamp();
        return lastHurtTimestamp != this.timestamp
                && this.ownerLastHurt != null
                && this.box.canAttack(this.ownerLastHurt)
                && this.canAttack(this.ownerLastHurt, TargetingConditions.DEFAULT);
    }

    @Override
    public void start()
    {
        this.mob.setTarget(this.ownerLastHurt);
        LivingEntity owner = this.box.getOwnerEntity();
        if (owner != null)
        {
            this.timestamp = owner.getLastHurtMobTimestamp();
        }
        super.start();
    }
}
