package net.snowteb.warriorcats_events.network.packet;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.snowteb.warriorcats_events.screen.FishingScreen;
import net.snowteb.warriorcats_events.stealth.PlayerStealthProvider;

import java.util.function.Supplier;

public class StCFishingScreenPacket {

    public StCFishingScreenPacket() {
    }


    public StCFishingScreenPacket(FriendlyByteBuf buf) {
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
