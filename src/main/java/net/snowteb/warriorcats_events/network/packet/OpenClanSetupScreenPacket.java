package net.snowteb.warriorcats_events.network.packet;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.snowteb.warriorcats_events.screen.clandata.ClanSetupScreen;

import java.util.function.Supplier;

public class OpenClanSetupScreenPacket {

    public OpenClanSetupScreenPacket() {
    }


    public OpenClanSetupScreenPacket(FriendlyByteBuf buf) {
    }


    public void toBytes(FriendlyByteBuf buf) {
    }


    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = supplier.get();
//        ctx.enqueueWork(() -> {
//            Minecraft mc = Minecraft.getInstance();
//            mc.setScreen(new FishingScreen());
//        });
        ctx.setPacketHandled(true);
    }
}
