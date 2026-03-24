package net.snowteb.warriorcats_events.network.packet.s2c.others;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.snowteb.warriorcats_events.client.ClientPacketHandles;
import net.snowteb.warriorcats_events.entity.custom.WCGenetics;

import java.util.function.Supplier;

public class StCFinallySaveMorph {

    private final String Key;

    private final WCGenetics genetics;
    private final WCGenetics.GeneticalVariants variants;
    private final WCGenetics chimeraGenetics;
    private final WCGenetics.GeneticalChimeraVariants chimeraVariants;


    public StCFinallySaveMorph(String key, WCGenetics genetics, WCGenetics.GeneticalVariants variants,
                               WCGenetics chimeraGens, WCGenetics.GeneticalChimeraVariants chimeraVariants) {
        this.Key = key;
        this.genetics = genetics;
        this.variants = variants;
        this.chimeraGenetics = chimeraGens;
        this.chimeraVariants = chimeraVariants;
    }

    public static StCFinallySaveMorph decode(FriendlyByteBuf buf) {

        String key = buf.readUtf();

        WCGenetics genetics = WCGenetics.decode(buf);
        WCGenetics.GeneticalVariants variants = WCGenetics.GeneticalVariants.decode(buf);

        WCGenetics chimeraGens = WCGenetics.decode(buf);
        WCGenetics.GeneticalChimeraVariants chimeraVariants = WCGenetics.GeneticalChimeraVariants.decode(buf);

        return new StCFinallySaveMorph( key ,genetics, variants, chimeraGens, chimeraVariants);
    }

    public static void encode(StCFinallySaveMorph packet, FriendlyByteBuf buf) {

        buf.writeUtf(packet.Key);

        packet.genetics.encode(buf);
        packet.variants.encode(buf);
        packet.chimeraGenetics.encode(buf);
        packet.chimeraVariants.encode(buf);

    }

    public void handle(Supplier<NetworkEvent.Context> packet) {
        NetworkEvent.Context ctx = packet.get();
        ctx.enqueueWork(() -> {

            ClientPacketHandles.openSaveMorphScreen( Key ,genetics, variants, chimeraGenetics, chimeraVariants);

        });

        ctx.setPacketHandled(true);
    }

}
