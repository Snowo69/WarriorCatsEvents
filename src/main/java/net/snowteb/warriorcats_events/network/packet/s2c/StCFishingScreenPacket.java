package net.snowteb.warriorcats_events.network.packet.s2c;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

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
