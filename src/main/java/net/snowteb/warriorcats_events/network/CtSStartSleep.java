//package net.snowteb.warriorcats_events.network;
//
//import net.minecraft.network.FriendlyByteBuf;
//import net.minecraft.network.chat.Component;
//import net.minecraft.server.level.ServerLevel;
//import net.minecraft.server.level.ServerPlayer;
//import net.minecraft.world.entity.Entity;
//import net.minecraft.world.level.Level;
//import net.minecraftforge.network.NetworkEvent;
//import net.snowteb.warriorcats_events.entity.custom.WCatEntity;
//import net.snowteb.warriorcats_events.network.packet.c2s.cats.RetrieveLastCatModePacket;
//
//import java.util.function.Supplier;
//
//public class CtSStartSleep {
//
//    public CtSStartSleep() {
//    }
//
//    public static void encode(CtSStartSleep msg, FriendlyByteBuf buf) {
//    }
//
//    public static CtSStartSleep decode(FriendlyByteBuf buf) {
//        return new CtSStartSleep();
//    }
//
//    public static void handle(CtSStartSleep msg, Supplier<NetworkEvent.Context> ctx) {
//        ctx.get().enqueueWork(() -> {
//            ServerPlayer player = ctx.get().getSender();
//            if (player == null) return;
//
//            if (player.isSpectator()) {
//                player.displayClientMessage(Component.literal("You can not sleep while in spectator mode."), true);
//                return;
//            }
//
//            if (player.level().dimension() != Level.OVERWORLD) {
//                player.displayClientMessage(Component.literal("You cannot sleep in other dimensions."), true);
//                return;
//            }
//
//            ServerLevel level = player.getServer().overworld();
//
//
//            if (level.isNight()) {
//                player.startSleeping(player.blockPosition());
//            } else {
//                player.displayClientMessage(Component.literal("You can sleep only at night."), true);
//            }
//
//        });
//        ctx.get().setPacketHandled(true);
//    }
//}
