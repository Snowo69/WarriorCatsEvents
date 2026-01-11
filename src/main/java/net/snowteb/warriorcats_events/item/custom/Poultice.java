package net.snowteb.warriorcats_events.item.custom;

import net.minecraft.advancements.Advancement;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
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

    /**
     * If right click with this item on a player or a "Healable" (tag), and its health is less than its max health:
     * Then heal it and consume 1 item,
     * Then unlock the advancement.
     */
    @Override
    public InteractionResult interactLivingEntity(ItemStack pStack, Player pPlayer,
                                                  LivingEntity pTarget, InteractionHand pHand) {

        if (!pPlayer.level().isClientSide) {

            if ((pTarget instanceof Player || pTarget.getType().is(HEALABLE))
                    && pTarget.getHealth() < pTarget.getMaxHealth()) {


                pTarget.heal(5.0F);


                pPlayer.level().playSound(null, pTarget.blockPosition(),
                        SoundEvents.SLIME_JUMP, SoundSource.PLAYERS, 0.7F, 1.1F);

                ((ServerLevel) pPlayer.level()).sendParticles(
                        ParticleTypes.HAPPY_VILLAGER,
                        pTarget.getX(), pTarget.getY() + pTarget.getBbHeight() * 0.6,
                        pTarget.getZ(), 30, 0.3, 0.3, 0.3, 1);


                pStack.shrink(1);

                if (pPlayer instanceof ServerPlayer pPlayer2) {

                    MinecraftServer server = pPlayer2.getServer();
                    if (server != null) {

                        Advancement adv = server.getAdvancements()
                                .getAdvancement(new ResourceLocation("warriorcats_events:healed_warrior"));

                        if (adv != null) {
                            pPlayer2.getAdvancements().award(adv, "healed_warrior");
                        }
                    }
                }

                return InteractionResult.CONSUME;
            }
        }

        return InteractionResult.PASS;
    }

}
