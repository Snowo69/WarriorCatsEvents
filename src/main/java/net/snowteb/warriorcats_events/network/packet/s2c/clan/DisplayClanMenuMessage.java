package net.snowteb.warriorcats_events.network.packet.s2c.clan;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.snowteb.warriorcats_events.WarriorCatsEvents;
import net.snowteb.warriorcats_events.client.ClientPacketHandles;

public class DisplayClanMenuMessage implements CustomPacketPayload {

    private final Component message;
    private final boolean isAnError;

    public DisplayClanMenuMessage(Component message, boolean isAnError) {
        this.message = message;
        this.isAnError = isAnError;
    }


    public DisplayClanMenuMessage(FriendlyByteBuf buf) {
        this.isAnError = buf.readBoolean();
        this.message = buf.readJsonWithCodec(ComponentSerialization.CODEC);
    }


    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBoolean(isAnError);
        buf.writeJsonWithCodec(ComponentSerialization.CODEC, this.message);
    }

    public void handle(IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            ClientPacketHandles.displayScreenMessage(this.isAnError, this.message);
        });
    }

    public static final Type<DisplayClanMenuMessage> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "display_clan_menu_message"));

    public static final StreamCodec<FriendlyByteBuf, DisplayClanMenuMessage> CODEC =
            StreamCodec.of(
                    (buf, pkt) -> pkt.toBytes(buf),
                    buf -> new DisplayClanMenuMessage(buf)
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}



