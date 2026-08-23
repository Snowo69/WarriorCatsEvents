package net.snowteb.warriorcats_events.network.packet.c2s.others;

import net.minecraft.core.component.DataComponents;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.snowteb.warriorcats_events.WarriorCatsEvents;
import net.snowteb.warriorcats_events.item.ModItems;
import net.snowteb.warriorcats_events.item.custom.DockBackpackItem;
import net.snowteb.warriorcats_events.screen.menus.DockBackpackMenu;

public class CtSOpenBackpackPacket implements CustomPacketPayload {

    public CtSOpenBackpackPacket() {
    }

    public static void encode(CtSOpenBackpackPacket msg, FriendlyByteBuf buf) {
    }

    public static CtSOpenBackpackPacket decode(FriendlyByteBuf buf) {
        return new CtSOpenBackpackPacket();
    }

    public static void handle(CtSOpenBackpackPacket msg, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {

            ServerPlayer sPlayer = (ServerPlayer) ctx.player();

            DockBackpackItem.findEquippedBackpack(sPlayer).ifPresent(backpack -> {
                        sPlayer.openMenu(
                                new SimpleMenuProvider(
                                        (windowId, inv, p) -> new DockBackpackMenu(windowId, inv, backpack.stack()),
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
    }

    public static final Type<CtSOpenBackpackPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "open_dock_bag"));

    public static final StreamCodec<FriendlyByteBuf, CtSOpenBackpackPacket> CODEC =
            StreamCodec.of(
                    (buf, pkt) -> encode(pkt, buf),
                    buf -> decode(buf)
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}

