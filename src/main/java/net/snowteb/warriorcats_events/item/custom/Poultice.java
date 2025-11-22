package net.snowteb.warriorcats_events.item.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class Poultice extends Item {

    private static final TagKey<EntityType<?>> HEALABLE =
            TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation("warriorcats_events", "healable"));

    public Poultice(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack pStack, Player pPlayer,
                                                  LivingEntity pTarget, InteractionHand pHand) {

        if (!pPlayer.level().isClientSide) {

            if ((pTarget instanceof Player || pTarget.getType().is(HEALABLE))
                    && pTarget.getHealth() < pTarget.getMaxHealth()) {


                pTarget.heal(5.0F);


                pPlayer.level().playSound(null, pTarget.blockPosition(),
                        SoundEvents.SLIME_JUMP, SoundSource.PLAYERS, 0.7F, 1.1F);

                ((net.minecraft.server.level.ServerLevel) pPlayer.level()).sendParticles(
                        ParticleTypes.HAPPY_VILLAGER,
                        pTarget.getX(), pTarget.getY() + pTarget.getBbHeight() * 0.6,
                        pTarget.getZ(), 30, 0.3, 0.3, 0.3, 1);


                pStack.shrink(1);

                return InteractionResult.CONSUME;
            }
        }

        return InteractionResult.PASS;
    }

}
