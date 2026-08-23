package net.snowteb.warriorcats_events.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.network.NetworkHooks;
import net.snowteb.warriorcats_events.compat.CompatibilitiesServer;
import net.snowteb.warriorcats_events.screen.menus.DockBackpackMenu;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class DockBackpackItem extends Item implements Equipable {
    public DockBackpackItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return new ICapabilityProvider() {

            private final DockBackpackHandler handler = new DockBackpackHandler(stack);
            private final LazyOptional<IItemHandler> optional = LazyOptional.of(() -> handler);

            @Override
            public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
                return cap == ForgeCapabilities.ITEM_HANDLER
                        ? optional.cast()
                        : LazyOptional.empty();
            }
        };
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {

        ItemStack stack = pPlayer.getItemInHand(pUsedHand);
        if (!pLevel.isClientSide() && pPlayer instanceof ServerPlayer sPlayer) {
            NetworkHooks.openScreen(sPlayer,
                    new SimpleMenuProvider(
                            (id, inv, p) -> new DockBackpackMenu(id, inv, stack, new SimpleContainerData(9)),
                            Component.translatable("item.warriorcats_events.dock_bag")),
                    buf -> buf.writeByte(pUsedHand == InteractionHand.MAIN_HAND
                            ? DockBackpackMenu.BackpackSlot.MAIN_HAND.ordinal()
                            : DockBackpackMenu.BackpackSlot.OFF_HAND.ordinal()));
        }

        pLevel.playSound(null, pPlayer.blockPosition(),
                SoundEvents.CHERRY_LEAVES_FALL,
                SoundSource.BLOCKS, 0.5F, 0.8F);

        return InteractionResultHolder.sidedSuccess(stack, pLevel.isClientSide);
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
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pStack.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handler -> {
            int occupiedSlots = 0;
            for (int i = 0; i < handler.getSlots(); i++) {
                if (!handler.getStackInSlot(i).isEmpty()) occupiedSlots++;
            }

            if (occupiedSlots > 0) {
                int freeSlots = handler.getSlots() - occupiedSlots;

                pTooltipComponents.add(
                        Component.literal(freeSlots + "/" + handler.getSlots())
                                .withStyle(freeSlots > 0 ? ChatFormatting.GREEN : ChatFormatting.RED));

                pTooltipComponents.add(Component.empty());

                if (InventoryScreen.hasShiftDown()) {
                    for (int i = 0; i < handler.getSlots(); i++) {
                        ItemStack current = handler.getStackInSlot(i);
                        if (!current.isEmpty()) {
                            pTooltipComponents.add(
                                    Component.empty()
                                            .append("• ")
                                            .append(current.getHoverName().copy().withStyle(ChatFormatting.GRAY))
                                            .append(Component.literal(" [×" + current.getCount() + "]").withStyle(ChatFormatting.AQUA))
                            );
                        }
                    }
                } else {
                    for (int i = 0, j = 0; i < handler.getSlots() && j < 3; i++) {
                        ItemStack current = handler.getStackInSlot(i);
                        if (!current.isEmpty()) {
                            pTooltipComponents.add(
                                    Component.empty()
                                            .append("• ")
                                            .append(current.getHoverName().copy().withStyle(ChatFormatting.GRAY))
                                            .append(Component.literal(" [×" + current.getCount() + "]").withStyle(ChatFormatting.AQUA))
                            );
                            j++;
                        }
                    }
                    if (occupiedSlots > 3) pTooltipComponents.add(Component.literal("..."));
                }
                pTooltipComponents.add(Component.empty());

                pTooltipComponents.add(Component.literal("≽^• ˕ •^≼"));
            }
        });

        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

    public static int getOccupiedSlots(ItemStack stack) {
        IItemHandler handler = stack.getCapability(ForgeCapabilities.ITEM_HANDLER).orElse(new ItemStackHandler(1));

        int occupiedSlots = 0;
        for (int i = 0; i < handler.getSlots(); i++) {
            if (!handler.getStackInSlot(i).isEmpty()) occupiedSlots++;
        }

        return occupiedSlots;
    }

    @Override
    public @NotNull EquipmentSlot getEquipmentSlot() {
        return EquipmentSlot.CHEST;
    }
}
