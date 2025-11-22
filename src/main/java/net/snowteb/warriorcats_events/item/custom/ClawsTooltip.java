package net.snowteb.warriorcats_events.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ClawsTooltip extends ShearsItem {

    private final String tooltipKey;

    public ClawsTooltip(Properties properties, String tooltipKey) {
        super(properties);
        this.tooltipKey = tooltipKey;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable(tooltipKey).withStyle(style -> style
                        .withColor(ChatFormatting.GRAY).withBold(false).withItalic(false)
                ));

    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        return 1.0f;
    }

    @Override
    public boolean isCorrectToolForDrops(BlockState state) {
        return false;
    }

}

