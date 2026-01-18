package net.snowteb.warriorcats_events.screen;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.CraftingMenu;
import net.snowteb.warriorcats_events.block.ModBlocks;

public class StoneCraftingTableMenu extends CraftingMenu {

    private final ContainerLevelAccess access;

    public StoneCraftingTableMenu(int pContainerId, Inventory pPlayerInventory, ContainerLevelAccess pAccess) {
        super(pContainerId, pPlayerInventory, pAccess);
        this.access = pAccess;
    }

    @Override
    public boolean stillValid(Player player) {
        return AbstractContainerMenu.stillValid(
                this.access,
                player,
                ModBlocks.STONE_CRAFTING_TABLE.get()
        );
    }
}
