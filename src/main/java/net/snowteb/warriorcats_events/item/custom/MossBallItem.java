package net.snowteb.warriorcats_events.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.snowteb.warriorcats_events.entity.custom.MossBallEntity;
import org.jetbrains.annotations.Nullable;
import tocraft.walkers.api.PlayerShape;

import java.util.List;

public class MossBallItem extends Item {

    private static final String WATER_KEY = "WaterLevel";

    public MossBallItem(Properties pProperties) {
        super(pProperties.stacksTo(16));
    }

    public static int getWaterLevel(ItemStack stack) {
        return stack.getOrCreateTag().getInt(WATER_KEY);
    }

    public static void setWaterLevel(ItemStack stack, int value) {
        stack.getOrCreateTag().putInt(WATER_KEY, value);
    }

    @Override
    public ItemStack getDefaultInstance() {
        ItemStack stack = super.getDefaultInstance();
        setWaterLevel(stack, 0);
        return stack;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {

        ItemStack itemstack = pPlayer.getItemInHand(pUsedHand);

        BlockHitResult hit = Item.getPlayerPOVHitResult(pLevel, pPlayer, ClipContext.Fluid.SOURCE_ONLY);

        if (hit.getType() == HitResult.Type.BLOCK) {
            BlockPos pos = hit.getBlockPos();
            BlockState state = pLevel.getBlockState(pos);

            if (state.getFluidState().isSource()) {

                if (!pLevel.isClientSide) {
                    if (PlayerShape.getCurrentShape(pPlayer) instanceof Animal) {
                        ItemStack single;
                        boolean isMany = false;
                        if (itemstack.getCount() > 1) {
                            isMany = true;
                            single = itemstack.split(1);
                        } else {
                            single = itemstack;
                        }

                        if (pPlayer.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof MossBallItem item) {
                            if (getWaterLevel(single) >= 10) {
                                pPlayer.displayClientMessage(Component.literal("Already full of water!").withStyle(ChatFormatting.GRAY), true);
                                return InteractionResultHolder.fail(single);
                            }
                        }

                        if (pPlayer instanceof ServerPlayer sPlayer) {

                            setWaterLevel(single,getWaterLevel(single) + 1);

                            if (isMany) {
                                if (!sPlayer.addItem(single)) {
                                    sPlayer.drop(single, false);
                                }
                            }


                            pLevel.playSound(
                                    null,
                                    sPlayer.blockPosition(),
                                    SoundEvents.CAT_EAT,
                                    SoundSource.PLAYERS,
                                    0.8F,
                                    1.0F
                            );

                            ((ServerLevel) pLevel).sendParticles(
                                    ParticleTypes.SPLASH,
                                    sPlayer.getX(),
                                    sPlayer.getY() + 0.5,
                                    sPlayer.getZ(),
                                    30,
                                    0.4, 0.2, 0.4,
                                    0.02
                            );
                        }


                    }
                }

                return InteractionResultHolder.sidedSuccess(itemstack, pLevel.isClientSide());
            }
        }


        ItemStack stack = pPlayer.getItemInHand(pUsedHand);
        if (stack.getItem() instanceof MossBallItem) {
            pPlayer.startUsingItem(pUsedHand);
            return InteractionResultHolder.consume(stack);
        }
        return InteractionResultHolder.fail(stack);

    }

//    @Override
//    public void onStopUsing(ItemStack stack, LivingEntity entity, int count) {
//
//    }

    @Override
    public void releaseUsing(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity, int pTimeCharged) {
        if (!(pLivingEntity instanceof Player player)) return;
        float throwStrength = this.getThrowStrength(this.getUseDuration(pStack) - pTimeCharged);
        if (throwStrength < 0.25f) return;
        if (!(player.level() instanceof ServerLevel sLevel)) return;

        {
            sLevel.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.SNOWBALL_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (sLevel.getRandom().nextFloat() * 0.4F + 0.8F));
            ItemStack ball = pStack.copy();
            ball.setCount(1);
            MossBallEntity mossBall = new MossBallEntity(sLevel, player);
            mossBall.setStack(ball);
            mossBall.setWater(getWaterLevel(ball));
            mossBall.shootFromRotation(
                    player, player.getXRot(), player.getYRot(), 0.0F, throwStrength * 1.2f, 0.25f
            );
            player.swing(InteractionHand.MAIN_HAND);
            sLevel.addFreshEntity(mossBall);
        }

        if (!player.getAbilities().instabuild) {
            pStack.shrink(1);
        }
    }

    private float getThrowStrength(int ticksUsed) {
        float seconds = ticksUsed / 20f;
        return Math.min(seconds, 1.0f);
    }

    @Override
    public int getUseDuration(ItemStack pStack) {
        return 72000;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        if (getWaterLevel(pStack) > 0) {
            pTooltipComponents.add(Component.literal("Water: " + getWaterLevel(pStack)).withStyle(ChatFormatting.AQUA));
        }
    }
}
