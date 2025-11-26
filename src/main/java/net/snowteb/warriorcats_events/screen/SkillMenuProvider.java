package net.snowteb.warriorcats_events.screen;

import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class SkillMenuProvider implements MenuProvider {

    @Override
    public Component getDisplayName() {
        return Component.literal("Skill Menu");
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inv, Player player) {
        return new SkillMenu(id, inv);
    }
}
