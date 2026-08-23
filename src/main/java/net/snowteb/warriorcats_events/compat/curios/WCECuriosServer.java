package net.snowteb.warriorcats_events.compat.curios;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class WCECuriosServer {

    public static Optional<ItemStack> findCuriosItem(Player player, Class<?> clas) {
        return CuriosApi.getCuriosHelper()
                .findFirstCurio(player, stack -> clas.isInstance(stack.getItem()))
                .map(SlotResult::stack);
    }

    public static List<ItemStack> getAllCuriosItems(Player player) {
        return CuriosApi.getCuriosHelper().getEquippedCurios(player).map(handler -> {
            List<ItemStack> list = new ArrayList<>();
            for (int i = 0; i < handler.getSlots(); i++) {
                ItemStack stack = handler.getStackInSlot(i);
                list.add(stack);
            }
            return list;
        }).orElse(List.of());
    }

}
