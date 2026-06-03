package net.snowteb.warriorcats_events.screen.menus;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.EnchantmentMenu;
import net.snowteb.warriorcats_events.block.ModBlocks;

public class MoonstoneMenu extends EnchantmentMenu {

    private final ContainerLevelAccess access;

    public MoonstoneMenu(int pContainerId, Inventory pPlayerInventory, ContainerLevelAccess pAccess) {
        super(pContainerId, pPlayerInventory, pAccess);
        this.access = pAccess;
    }

    @Override
    public boolean stillValid(Player player) {
        return AbstractContainerMenu.stillValid(
                this.access,
                player,
                ModBlocks.MOONSTONE_BLOCK.get()
        );
    }
}
