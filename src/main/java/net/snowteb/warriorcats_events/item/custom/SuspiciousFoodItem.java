package net.snowteb.warriorcats_events.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SuspiciousFoodItem extends Item {
    public SuspiciousFoodItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        if (InventoryScreen.hasShiftDown()) {
            pTooltipComponents.add(Component.empty());
            pTooltipComponents.add(Component.translatable("item.suspicious_food.tooltip")
                    .withStyle(ChatFormatting.DARK_RED).withStyle(ChatFormatting.ITALIC));
        }
    }
}
