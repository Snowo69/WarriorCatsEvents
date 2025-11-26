package net.snowteb.warriorcats_events.screen;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

public class SkillMenu extends AbstractContainerMenu {

    public SkillMenu(int id, Inventory inv) {
        super(ModMenuTypes.SKILL_MENU.get(), id);
    }

    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        return null;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }
}
