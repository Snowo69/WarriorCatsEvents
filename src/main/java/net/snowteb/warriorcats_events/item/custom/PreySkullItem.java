package net.snowteb.warriorcats_events.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.snowteb.warriorcats_events.block.ModBlocks;
import net.snowteb.warriorcats_events.block.custom.PreyBonesBlock;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PreySkullItem extends Item {

    private final PreyBonesBlock.Bones property;

    public PreySkullItem(Properties pProperties, PreyBonesBlock.Bones property) {
        super(pProperties);
        this.property = property;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        return super.use(pLevel, pPlayer, pUsedHand);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        BlockPos blockPos = pContext.getClickedPos();
        BlockState blockState = pContext.getLevel().getBlockState(blockPos);
        Player player = pContext.getPlayer();
        Level level = pContext.getLevel();
        InteractionHand interactionHand = pContext.getHand();

        if (player == null) return InteractionResult.PASS;

        if (interactionHand == InteractionHand.MAIN_HAND && player.getItemInHand(interactionHand).getItem() == this) {
            if (blockState.is(ModBlocks.PREY_BONES.get())) {
                if (!level.isClientSide) {
                    if (blockState.getValue(PreyBonesBlock.BONES) == PreyBonesBlock.Bones.STAGE_3) {
                        BlockState newBlockState = ModBlocks.PREY_BONES.get().defaultBlockState()
                                .setValue(PreyBonesBlock.BONES, this.property)
                                .setValue(PreyBonesBlock.FACING, blockState.getValue(PreyBonesBlock.FACING));

                        player.getItemInHand(interactionHand).shrink(1);

                        level.setBlockAndUpdate(blockPos, newBlockState);
                        level.playSound(null, blockPos, SoundEvents.BONE_BLOCK_PLACE, SoundSource.BLOCKS,
                                0.8F, 1.4F);

                        return InteractionResult.SUCCESS;
                    }
                }
            }
        }

        return super.useOn(pContext);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.translatable("prey_skull_item.tooltip").withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.ITALIC));
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }
}
