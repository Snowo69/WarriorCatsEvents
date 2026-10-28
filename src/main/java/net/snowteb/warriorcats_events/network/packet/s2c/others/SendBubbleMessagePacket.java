package net.snowteb.warriorcats_events.network.packet.s2c.others;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.snowteb.warriorcats_events.WarriorCatsEvents;
import net.snowteb.warriorcats_events.client.ClientPacketHandles;

import java.util.UUID;

public class SendBubbleMessagePacket implements CustomPacketPayload {
    private final UUID sender;
    private final Component message;

    public SendBubbleMessagePacket(UUID sender, Component message) {
        this.sender = sender;
        this.message = message;
    }

    public SendBubbleMessagePacket(FriendlyByteBuf buf) {
        this.sender = buf.readUUID();
        this.message = buf.readJsonWithCodec(ComponentSerialization.CODEC);;

    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUUID(this.sender);
        buf.writeJsonWithCodec(ComponentSerialization.CODEC, this.message);
    }

    public boolean handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            ClientPacketHandles.addBubbleMessage(sender, message);
        });
        return true;
    }

    public static final Type<SendBubbleMessagePacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "add_bubble_message"));

    public static final StreamCodec<FriendlyByteBuf, SendBubbleMessagePacket> CODEC =
            StreamCodec.of(
                    (buf, pkt) -> pkt.toBytes(buf),
                    buf -> new SendBubbleMessagePacket(buf)
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
