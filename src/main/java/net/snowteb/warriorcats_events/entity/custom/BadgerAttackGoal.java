package net.snowteb.warriorcats_events.entity.custom;

import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.level.block.Blocks;
import net.snowteb.warriorcats_events.sound.ModSounds;

public class BadgerAttackGoal extends MeleeAttackGoal {
    private final BadgerEntity entity;
    private int attackDelay = 10;
    private int ticksUntilNextAttack = 10;
    private boolean shouldCountTillNextAttack = false;

    BlockParticleOption particle = new BlockParticleOption(
            ParticleTypes.BLOCK,
            Blocks.REDSTONE_BLOCK.defaultBlockState()
    );

    public BadgerAttackGoal(PathfinderMob pMob, double pSpeedModifier, boolean pFollowingTargetEvenIfNotSeen) {
        super(pMob, pSpeedModifier, pFollowingTargetEvenIfNotSeen);
        entity = ((BadgerEntity)pMob);
    }

    /**
     * These values were carefully changed for hours until it properly worked.
     * I can't really explain this, i just know it works somehow
     */

    @Override
    public void start() {
        super.start();
        attackDelay = 17;
        ticksUntilNextAttack = 17;
    }

    /**
     * If it should count until next attack, then decrease the counter.
     * But makes sure it doesn't turn into negatives.
     */
    @Override
    public void tick() {
        super.tick();
        if (shouldCountTillNextAttack) {
            this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
        }
    }

    /**
     * If the distance is less than the acceptable distance, then:
     * It should count until next attack and the ticks until next attack is the default delay.
     * If the ticks until next attack equals the default delay, then setAttacking is true, which will allow to perfomr the attack animation.
     * If the counter is less or equals to zero, then perform the attack and reset the counter.
     *
     * Otherwise
     * It should not count, and reset the counter, and this is not attacking, and the timeout is zero.
     */
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

    /**
     * The acceptable distance to attack
     */
    @Override
    protected double getAttackReachSqr(LivingEntity target) {
        return 8.0;
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
        if (!mob.level().isClientSide()){
            ServerLevel level = ((ServerLevel) mob.level());
            level.sendParticles(particle,
                    pEnemy.getX(), pEnemy.getY(), pEnemy.getZ(),
                    10,
                    0.1,0.2,0.1, 0.1);
            if (level.random.nextInt(3) == 0) {
                level.playSound(mob, mob.blockPosition(), ModSounds.BADGER_SCREECH.get(), SoundSource.HOSTILE, 1.2F, 1.0F);
            }
        }

    }


    @Override
    public void stop() {
        entity.setAttacking(false);
        super.stop();
    }
}
