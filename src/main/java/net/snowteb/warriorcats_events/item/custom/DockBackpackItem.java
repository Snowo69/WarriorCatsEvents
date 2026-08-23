package net.snowteb.warriorcats_events.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.snowteb.warriorcats_events.compat.CompatibilitiesServer;
import net.snowteb.warriorcats_events.datacomponents.ModDataComponents;
import net.snowteb.warriorcats_events.screen.menus.DockBackpackMenu;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class DockBackpackItem extends Item implements Equipable {
    public DockBackpackItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {

        ItemStack stack = pPlayer.getItemInHand(pUsedHand);

        if (!pLevel.isClientSide() && pPlayer instanceof ServerPlayer sPlayer) {
            sPlayer.openMenu(
                    new SimpleMenuProvider(
                            (id, inv, p) -> new DockBackpackMenu(id, inv, stack),
                            Component.translatable("item.warriorcats_events.dock_bag")),
                    buf -> buf.writeByte(pUsedHand == InteractionHand.MAIN_HAND
                            ? DockBackpackMenu.BackpackSlot.MAIN_HAND.ordinal()
                            : DockBackpackMenu.BackpackSlot.OFF_HAND.ordinal()));
        }

        pLevel.playSound(null, pPlayer.blockPosition(),
                SoundEvents.CHERRY_LEAVES_FALL, SoundSource.BLOCKS, 0.5F, 0.8F);

        return InteractionResultHolder.sidedSuccess(stack, pLevel.isClientSide);
    }

    @Override
    public @NotNull EquipmentSlot getEquipmentSlot() {
        return EquipmentSlot.CHEST;
    }

    public record EquippedBackpack(ItemStack stack, DockBackpackMenu.BackpackSlot slot) {}

    public static Optional<EquippedBackpack> findEquippedBackpack(Player player) {
        ItemStack chest = player.getItemBySlot(EquipmentSlot.CHEST);
        if (chest.getItem() instanceof DockBackpackItem) {
            return Optional.of(new EquippedBackpack(chest, DockBackpackMenu.BackpackSlot.CHEST));
        }

        return CompatibilitiesServer.findCuriosItem(player, DockBackpackItem.class)
                .map(stack -> new EquippedBackpack(stack, DockBackpackMenu.BackpackSlot.CURIO));
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        DockBackpackContent contents = stack.getOrDefault(
                ModDataComponents.BACKPACK_CONTENTS, DockBackpackContent.empty());

        int occupiedSlots = 0;
        for (int i = 0; i < contents.getSlots(); i++) {
            if (!contents.getStackInSlot(i).isEmpty()) occupiedSlots++;
        }

        if (occupiedSlots > 0) {
            int freeSlots = contents.getSlots() - occupiedSlots;

            tooltipComponents.add(
                    Component.literal(freeSlots + "/" + contents.getSlots())
                            .withStyle(freeSlots > 0 ? ChatFormatting.GREEN : ChatFormatting.RED));

            tooltipComponents.add(Component.empty());

            if (InventoryScreen.hasShiftDown()) {
                for (int i = 0; i < contents.getSlots(); i++) {
                    ItemStack current = contents.getStackInSlot(i);
                    if (!current.isEmpty()) {
                        tooltipComponents.add(
                                Component.empty()
                                        .append("• ")
                                        .append(current.getHoverName().copy().withStyle(ChatFormatting.GRAY))
                                        .append(Component.literal(" [×" + current.getCount() + "]").withStyle(ChatFormatting.AQUA))
                        );
                    }
                }
            } else {
                for (int i = 0, j = 0; i < contents.getSlots() && j < 3; i++) {
                    ItemStack current = contents.getStackInSlot(i);
                    if (!current.isEmpty()) {
                        tooltipComponents.add(
                                Component.empty()
                                        .append("• ")
                                        .append(current.getHoverName().copy().withStyle(ChatFormatting.GRAY))
                                        .append(Component.literal(" [×" + current.getCount() + "]").withStyle(ChatFormatting.AQUA))
                        );
                        j++;
                    }
                }
                if (occupiedSlots > 3) tooltipComponents.add(Component.literal("..."));
            }
            tooltipComponents.add(Component.empty());

            tooltipComponents.add(Component.literal("≽^• ˕ •^≼"));
        }

    }

    public static int getOccupiedSlots(ItemStack stack) {
        DockBackpackContent contents = stack.getOrDefault(
                ModDataComponents.BACKPACK_CONTENTS, DockBackpackContent.empty());

        int occupiedSlots = 0;
        for (int i = 0; i < contents.getSlots(); i++) {
            if (!contents.getStackInSlot(i).isEmpty()) occupiedSlots++;
        }

        return occupiedSlots;
    }

}
