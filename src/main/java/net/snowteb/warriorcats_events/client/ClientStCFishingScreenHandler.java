package net.snowteb.warriorcats_events.client.network;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;
import net.snowteb.warriorcats_events.screen.FishingScreen;

import java.util.function.Supplier;

@OnlyIn(Dist.CLIENT)
public class ClientStCFishingScreenHandler {

    public static void handle(Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = ctxSupplier.get();
        ctx.enqueueWork(() -> {
            Minecraft.getInstance().setScreen(new FishingScreen());
        });
        ctx.setPacketHandled(true);
    }
}
