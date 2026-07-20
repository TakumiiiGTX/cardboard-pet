package com.takumi.takumimod.entity.goal;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;

/**
 * Vanilla's MeleeAttackGoal always swings once every 20 ticks (1s): resetAttackCooldown()
 * hardcodes adjustedTickDelay(20) rather than consulting the overridable getAttackInterval(),
 * and the backing ticksUntilNextAttack field is private, so neither can be tuned by overriding
 * them alone. This overrides checkAndPerformAttack() instead, tracking its own cooldown field
 * to attack twice as often.
 */
public class CardboardMeleeAttackGoal extends MeleeAttackGoal
{
    private static final int ATTACK_INTERVAL_TICKS = 10;

    private final PathfinderMob mob;
    private int attackCooldown;

    public CardboardMeleeAttackGoal(PathfinderMob mob, double speedModifier, boolean followingTargetEvenIfNotSeen)
    {
        super(mob, speedModifier, followingTargetEvenIfNotSeen);
        this.mob = mob;
    }

    @Override
    public void start()
    {
        super.start();
        this.attackCooldown = 0;
    }

    @Override
    protected void checkAndPerformAttack(LivingEntity target, double distToTargetSqr)
    {
        double reach = this.getAttackReachSqr(target);
        if (this.attackCooldown > 0)
        {
            this.attackCooldown--;
        }
        if (this.attackCooldown <= 0 && distToTargetSqr <= reach && this.mob.getSensing().hasLineOfSight(target))
        {
            this.attackCooldown = ATTACK_INTERVAL_TICKS;
            this.mob.swing(InteractionHand.MAIN_HAND);
            this.mob.doHurtTarget(target);
        }
    }
}
