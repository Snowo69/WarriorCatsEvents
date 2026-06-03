package net.snowteb.warriorcats_events.network.packet.s2c.others;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class StCFishingScreenPacket {

    public final BlockPos clickedPos;
    public StCFishingScreenPacket(BlockPos pos) {
        this.clickedPos = pos;
    }


    public StCFishingScreenPacket(FriendlyByteBuf buf) {
        this.clickedPos = buf.readBlockPos();
    }


    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBlockPos(this.clickedPos);
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
