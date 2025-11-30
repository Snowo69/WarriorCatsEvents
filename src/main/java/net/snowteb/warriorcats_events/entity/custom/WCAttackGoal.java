package net.snowteb.warriorcats_events.entity.custom;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;

public class WCAttackGoal extends MeleeAttackGoal {
    private final WCatEntity entity;
    private int attackDelay = 10;
    private int ticksUntilNextAttack = 10;
    private boolean shouldCountTillNextAttack = false;

    public WCAttackGoal(PathfinderMob pMob, double pSpeedModifier, boolean pFollowingTargetEvenIfNotSeen) {
        super(pMob, pSpeedModifier, pFollowingTargetEvenIfNotSeen);
        entity = ((WCatEntity)pMob);
    }


    @Override
    public void start() {
        super.start();
        attackDelay = 17;
        ticksUntilNextAttack = 17;
    }

    @Override
    public void tick() {
        super.tick();
        if (shouldCountTillNextAttack) {
            this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
        }
    }

    @Override
    protected void checkAndPerformAttack(LivingEntity enemy, double distToEnemySqr) {

        if (distToEnemySqr <= this.getAttackReachSqr(enemy)) {

            if (!shouldCountTillNextAttack) {
                shouldCountTillNextAttack = true;
                ticksUntilNextAttack = attackDelay;
            }

            if (ticksUntilNextAttack == attackDelay) {
                entity.setAttacking(true);
            }

            if (ticksUntilNextAttack <= 0) {
                this.mob.getLookControl().setLookAt(enemy.getX(), enemy.getEyeY(), enemy.getZ());
                performAttack(enemy);

                ticksUntilNextAttack = attackDelay;
            }

        } else {

            shouldCountTillNextAttack = false;
            ticksUntilNextAttack = attackDelay;
            entity.setAttacking(false);
            entity.attackAnimationTimeout = 0;
        }
    }


    @Override
    protected double getAttackReachSqr(LivingEntity target) {
        return 7.3;
    }


    protected void resetAttackCooldown() {
        this.ticksUntilNextAttack = this.adjustedTickDelay(attackDelay);
    }

    protected boolean isTimeToAttack() {
        return this.ticksUntilNextAttack <= 0;
    }

    protected int getTicksUntilNextAttack() {
        return this.ticksUntilNextAttack;
    }


    protected void performAttack(LivingEntity pEnemy) {
        this.resetAttackCooldown();
        this.mob.swing(InteractionHand.MAIN_HAND);
        this.mob.doHurtTarget(pEnemy);
    }


    @Override
    public void stop() {
        entity.setAttacking(false);
        super.stop();
    }
}
