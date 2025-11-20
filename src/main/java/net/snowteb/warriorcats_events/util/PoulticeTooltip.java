package net.snowteb.warriorcats_events.util;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.snowteb.warriorcats_events.item.custom.Poultice;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PoulticeTooltip extends Poultice {
    public PoulticeTooltip(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("item.warriorcats_events.dock_poultice.tooltip")

                .withStyle(style -> style
                        .withColor(ChatFormatting.GRAY)  // Color dorado
                        .withBold(false)  // Negrita
                        .withItalic(false) // Cursiva
                ));

    }
}

