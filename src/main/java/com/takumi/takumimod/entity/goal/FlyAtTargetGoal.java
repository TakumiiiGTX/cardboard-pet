package com.takumi.takumimod.entity.goal;

import com.takumi.takumimod.entity.CardboardBoxEntity;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

/**
 * Normally CardboardBoxEntity walks on the ground; while this goal is active it
 * ignores gravity and flies straight through the air toward its target, then lands
 * and hands off to the melee goal once close enough. It can also land a hit on the
 * target mid-flight once within striking distance, rather than only after landing.
 */
public class FlyAtTargetGoal extends Goal
{
    private static final double TRIGGER_DISTANCE_SQR = 9.0D;
    private static final double LAND_DISTANCE_SQR = 2.25D;
    private static final double FLIGHT_SPEED = 0.5D;
    private static final int MAX_FLIGHT_TICKS = 60;
    private static final int LANDING_SLOW_FALLING_TICKS = 100;
    private static final int ATTACK_INTERVAL_TICKS = 20;

    private final CardboardBoxEntity mob;
    private LivingEntity target;
    private int flightTicks;
    private int attackCooldown;

    public FlyAtTargetGoal(CardboardBoxEntity mob)
    {
        this.mob = mob;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.JUMP));
    }

    @Override
    public boolean canUse()
    {
        LivingEntity target = this.mob.getTarget();
        if (target == null || !target.isAlive())
        {
            return false;
        }
        if (this.mob.distanceToSqr(target) < TRIGGER_DISTANCE_SQR)
        {
            return false;
        }
        return this.mob.getRandom().nextInt(10) == 0;
    }

    @Override
    public boolean canContinueToUse()
    {
        if (this.target == null || !this.target.isAlive() || this.flightTicks >= MAX_FLIGHT_TICKS)
        {
            return false;
        }
        return this.mob.distanceToSqr(this.target) > LAND_DISTANCE_SQR;
    }

    @Override
    public void start()
    {
        this.target = this.mob.getTarget();
        this.flightTicks = 0;
        this.attackCooldown = 0;
        this.mob.setNoGravity(true);
    }

    @Override
    public void stop()
    {
        this.target = null;
        this.mob.setNoGravity(false);
        this.mob.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, LANDING_SLOW_FALLING_TICKS, 0));
    }

    @Override
    public boolean requiresUpdateEveryTick()
    {
        return true;
    }

    @Override
    public void tick()
    {
        this.flightTicks++;
        if (this.attackCooldown > 0)
        {
            this.attackCooldown--;
        }
        if (this.target == null)
        {
            return;
        }

        Vec3 toTarget = this.target.position().add(0.0D, this.target.getBbHeight() * 0.5D, 0.0D)
                .subtract(this.mob.position());
        double distance = toTarget.length();
        if (distance > 1.0E-4D)
        {
            Vec3 direction = toTarget.scale(1.0D / distance);
            this.mob.setDeltaMovement(this.mob.getDeltaMovement().scale(0.6D).add(direction.scale(FLIGHT_SPEED)));
        }

        this.mob.getLookControl().setLookAt(this.target, 30.0F, 30.0F);

        if (this.attackCooldown <= 0 && this.mob.distanceToSqr(this.target) <= LAND_DISTANCE_SQR)
        {
            this.attackCooldown = ATTACK_INTERVAL_TICKS;
            this.mob.swing(InteractionHand.MAIN_HAND);
            this.mob.doHurtTarget(this.target);
        }
    }
}
