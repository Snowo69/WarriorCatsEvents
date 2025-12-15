package net.snowteb.warriorcats_events.item.custom;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ClawsTooltip extends ShearsItem {
    private static final UUID ATTACK_DAMAGE_UUID = UUID.fromString("11111111-1111-1111-1111-111111111111");
    private static final UUID ATTACK_SPEED_UUID = UUID.fromString("22222222-2222-2222-2222-222222222222");
    private final String tooltipKey;

    public ClawsTooltip(Properties properties, String tooltipKey) {
        super(properties);
        this.tooltipKey = tooltipKey;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> modifier = ImmutableMultimap.builder();
        if (slot == EquipmentSlot.MAINHAND) {
            modifier.put(Attributes.ATTACK_DAMAGE,
                    new AttributeModifier(ATTACK_DAMAGE_UUID, "Weapon modifier",
                            4.0, AttributeModifier.Operation.ADDITION));
            modifier.put(Attributes.ATTACK_SPEED,
                    new AttributeModifier(ATTACK_SPEED_UUID, "Weapon modifier",
                            1.6, AttributeModifier.Operation.ADDITION));
        }
        Multimap<Attribute, AttributeModifier> modifiers = modifier.build();

        return modifiers;
    }


    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flag) {
        String raw = (Component.translatable(tooltipKey).getString());

        String[] lines = raw.split("\\\\n");

        for (String line : lines) {
            tooltip.add(Component.literal(line).withStyle(ChatFormatting.GRAY));
        }

    }

    /**
     * Allows performing axe actions and shield actions.
     */
    @Override
    public boolean canPerformAction(ItemStack stack, ToolAction action) {
        return ToolActions.DEFAULT_AXE_ACTIONS.contains(action)
                || ToolActions.DEFAULT_SHIELD_ACTIONS.contains(action)
                || super.canPerformAction(stack, action);
    }


    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        return 1.0f;
    }

    @Override
    public boolean isCorrectToolForDrops(BlockState state) {
        return false;
    }

    /**
     * Too hard to explain
     * It is basically the same thing axes do with logs, but instead of damaging the tool, this will repair the claws.
     */
    public InteractionResult useOn(UseOnContext pContext) {

        Level level = pContext.getLevel();
        BlockPos blockpos = pContext.getClickedPos();
        Player player = pContext.getPlayer();
        BlockState blockstate = level.getBlockState(blockpos);
        Optional<BlockState> optional = Optional.ofNullable(blockstate.getToolModifiedState(pContext, ToolActions.AXE_STRIP, false));
        ItemStack itemstack = pContext.getItemInHand();
        Optional<BlockState> optional3 = Optional.empty();

            if (optional.isPresent()) {
                level.playSound(player, blockpos, SoundEvents.CROSSBOW_QUICK_CHARGE_3, SoundSource.BLOCKS, 0.7F, 1.2F);
                level.playSound(player, blockpos, SoundEvents.CROSSBOW_LOADING_END, SoundSource.BLOCKS, 0.7F, 0.5F);
                optional3 = optional;
            }

            if (optional3.isPresent()) {
                if (player instanceof ServerPlayer) {
                    CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger((ServerPlayer)player, blockpos, itemstack);
                    level.playSound(player, blockpos, SoundEvents.CHERRY_WOOD_FALL, SoundSource.BLOCKS, 0.7F, 1.0F);

                }

                if (player != null) {
                    itemstack.hurtAndBreak(-3, player, (p_150686_) -> {
                        p_150686_.broadcastBreakEvent(pContext.getHand());

                    });
                }

                if (!level.isClientSide() && level.getRandom().nextInt(40) == 0) {
                    level.setBlock(blockpos, optional.get(), 11);
                    level.gameEvent(GameEvent.BLOCK_CHANGE, blockpos, GameEvent.Context.of(player, optional3.get()));
                }

                return InteractionResult.sidedSuccess(level.isClientSide);
            } else {

                return InteractionResult.PASS;
            }




    }

    /**
     * Every time you hurt an entity, damage the tool.
     */
    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        pStack.hurtAndBreak(1, pAttacker, (p_43296_) -> {
            p_43296_.broadcastBreakEvent(EquipmentSlot.MAINHAND);
        });

        return true;
    }

    /**
     * If you use right click, then start using the item.
     * And since we previously defined the item can perform shield actions, then it will perform the shield action.
     */
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        pPlayer.startUsingItem(pHand);
        pLevel.playSound(pPlayer, pPlayer.blockPosition(), SoundEvents.MOSS_STEP, SoundSource.PLAYERS, 0.7F, 0.6F);

        return InteractionResultHolder.consume(itemstack);
    }

    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.BLOCK;
    }

    public int getUseDuration(ItemStack pStack) {
        return 72000;
    }


}

