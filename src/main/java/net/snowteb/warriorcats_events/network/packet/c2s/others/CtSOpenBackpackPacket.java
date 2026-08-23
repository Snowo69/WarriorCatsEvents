package net.snowteb.warriorcats_events.network.packet.c2s.others;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkHooks;
import net.snowteb.warriorcats_events.item.custom.DockBackpackItem;
import net.snowteb.warriorcats_events.screen.menus.DockBackpackMenu;

import java.util.function.Supplier;

public class CtSOpenBackpackPacket {

    public CtSOpenBackpackPacket() {
    }

    public static void encode(CtSOpenBackpackPacket msg, FriendlyByteBuf buf) {
    }

    public static CtSOpenBackpackPacket decode(FriendlyByteBuf buf) {
        return new CtSOpenBackpackPacket();
    }

    public static void handle(CtSOpenBackpackPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {

            ServerPlayer sPlayer = ctx.get().getSender();
            if (sPlayer == null) return;

            DockBackpackItem.findEquippedBackpack(sPlayer).ifPresent(backpack -> {
                        NetworkHooks.openScreen(sPlayer,
                                new SimpleMenuProvider(
                                        (windowId, inv, p) -> new DockBackpackMenu(windowId, inv, backpack.stack(), new SimpleContainerData(9)),
                                        Component.translatable("item.warriorcats_events.dock_bag")
                                ),
                                buf -> buf.writeByte(backpack.slot().ordinal())
                        );

                        sPlayer.level().playSound(null, sPlayer.blockPosition(),
                                SoundEvents.CHERRY_LEAVES_FALL,
                                SoundSource.BLOCKS, 0.5F, 0.8F);
                    }
            );
        });
        ctx.get().setPacketHandled(true);
    }

}

