package net.snowteb.warriorcats_events.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.snowteb.warriorcats_events.WarriorCatsEvents;
import net.snowteb.warriorcats_events.diseases.Disease;
import net.snowteb.warriorcats_events.diseases.DiseaseTypes;
import net.snowteb.warriorcats_events.diseases.Diseaseable;
import net.snowteb.warriorcats_events.diseases.kinds.BrokenPaw;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ComfreyPoultice extends Item {

    public ComfreyPoultice(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack pStack, Player pPlayer,
                                                  LivingEntity pTarget, InteractionHand pHand) {

        if (pHand != InteractionHand.MAIN_HAND) return InteractionResult.PASS;

        if (!pPlayer.level().isClientSide()) {
            if (pTarget instanceof Diseaseable<?> diseaseable) {
                if (diseaseable.getDisease(DiseaseTypes.BROKEN_PAW) instanceof BrokenPaw brokenPaw) {
                    if (brokenPaw.isBoneWrapped() && pPlayer.level() instanceof ServerLevel sLevel){
                        if (brokenPaw.applyComfreyPoultice(diseaseable)){
                            if (!pPlayer.getAbilities().instabuild) pStack.shrink(1);

                            pPlayer.level().playSound(null, pTarget.blockPosition(),
                                    SoundEvents.SLIME_JUMP, SoundSource.PLAYERS, 0.7F, 1.1F);

                            sLevel.sendParticles(
                                    ParticleTypes.HAPPY_VILLAGER,
                                    pTarget.getX(), pTarget.getY() + pTarget.getBbHeight() * 0.6,
                                    pTarget.getZ(), 30, 0.3, 0.3, 0.3, 1);

                            return InteractionResult.SUCCESS;
                        }
                    }
                }
            }
        }

        return InteractionResult.PASS;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {

        if (pUsedHand != InteractionHand.MAIN_HAND) return InteractionResultHolder.pass(pPlayer.getItemInHand(pUsedHand));

        if (!pLevel.isClientSide()) {
            if (pPlayer instanceof Diseaseable<?> diseaseable) {
                if (diseaseable.getDisease(DiseaseTypes.BROKEN_PAW) instanceof BrokenPaw brokenPaw) {
                    if (brokenPaw.isBoneWrapped() && pPlayer.level() instanceof ServerLevel sLevel){
                        if (brokenPaw.applyComfreyPoultice(diseaseable)) {

                            if (!pPlayer.getAbilities().instabuild) pPlayer.getItemInHand(pUsedHand).shrink(1);

                            pPlayer.level().playSound(null, pPlayer.blockPosition(),
                                    SoundEvents.SLIME_JUMP, SoundSource.PLAYERS, 0.7F, 1.1F);

                            sLevel.sendParticles(
                                    ParticleTypes.HAPPY_VILLAGER,
                                    pPlayer.getX(), pPlayer.getY() + pPlayer.getBbHeight() * 0.6,
                                    pPlayer.getZ(), 30, 0.3, 0.3, 0.3, 1);

                            return InteractionResultHolder.success(pPlayer.getItemInHand(pUsedHand));
                        }
                    }
                }
            }
        }

        return super.use(pLevel, pPlayer, pUsedHand);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        String raw = Component.translatable("item.warriorcats_events.comfrey_poultice.tooltip").getString();

        String[] lines = raw.split("\\\\n");

        for (String line : lines) {
            tooltipComponents.add(Component.literal(line).withStyle(ChatFormatting.GRAY));
        }
    }
}
