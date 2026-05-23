package net.snowteb.warriorcats_events.compat.curios;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;

import java.util.UUID;

public class WCECurios {

    public static boolean hasCuriosItem(UUID boundUUID, Item item) {
        if (Minecraft.getInstance().level != null) {
            Player player = Minecraft.getInstance().level.getPlayerByUUID(boundUUID);
            if (player != null) {
                return CuriosApi.getCuriosHelper().findFirstCurio(player,
                        stack -> stack.is(item)).isPresent();
            }
        }
        return false;
    }

    public static boolean hasCuriosItem(UUID boundUUID,  Class<?> clazz) {
        if (Minecraft.getInstance().level != null) {
            Player player = Minecraft.getInstance().level.getPlayerByUUID(boundUUID);
            if (player != null) {
                return CuriosApi.getCuriosHelper().findFirstCurio(player,
                        stack -> clazz.isInstance(stack.getItem())).isPresent();
            }
        }
        return false;
    }

    public static ItemStack getCuriosItem(UUID boundUUID, Item item) {
        if (Minecraft.getInstance().level != null) {
            Player player = Minecraft.getInstance().level.getPlayerByUUID(boundUUID);
            if (player != null) {
                return CuriosApi.getCuriosHelper()
                        .findFirstCurio(player, stack -> stack.is(item))
                        .map(SlotResult::stack)
                        .orElse(ItemStack.EMPTY);
            }
        }
        return ItemStack.EMPTY;
    }

    public static ItemStack getCuriosItem(UUID boundUUID, Class<?> clazz) {
        if (Minecraft.getInstance().level != null) {
            Player player = Minecraft.getInstance().level.getPlayerByUUID(boundUUID);
            if (player != null) {
                return CuriosApi.getCuriosHelper()
                        .findFirstCurio(player, stack -> clazz.isInstance(stack.getItem()))
                        .map(SlotResult::stack)
                        .orElse(ItemStack.EMPTY);
            }
        }
        return ItemStack.EMPTY;
    }

}
