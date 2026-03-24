package net.snowteb.warriorcats_events.mixin;

import net.minecraft.network.protocol.game.ServerboundChatPacket;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ServerGamePacketListenerImpl.class)
public class ServerChatListenerMixin {

    @ModifyVariable(method = "handleChat", at = @At("HEAD"), argsOnly = true)
    private ServerboundChatPacket modifyMessage(ServerboundChatPacket packet) {

        String text = packet.message();

        text = text.replace(":sob:", "\uD83D\uDE2D");
        text = text.replace(":cry:", "\uD83D\uDE2D");
        text = text.replace(":sparkles:", "✨");
        text = text.replace(":sparkle:", "✨");
        text = text.replace(":cat:", "\uD83D\uDC08");
        text = text.replace(":cat2:", "\uD83D\uDC31");
        text = text.replace(":plead:", "\uD83E\uDD79");
        text = text.replace(":pleading:", "\uD83E\uDD79");
        text = text.replace(":wilted_rose:", "\uD83E\uDD40");
        text = text.replace(":heart:", "❤");
        text = text.replace(":fire:", "\uD83D\uDD25");
        text = text.replace(":flame:", "\uD83D\uDD25");
        text = text.replace(":smile:", "\uD83D\uDE3A");
        text = text.replace(":popcorn:", "\uD83C\uDF7F");
        text = text.replace(":eyes:", "\uD83D\uDC40");
        text = text.replace(":skull:", "\uD83D\uDC80");
        text = text.replace(":tada:", "\uD83C\uDF89");
        text = text.replace(":confetti:", "\uD83C\uDF89");
        text = text.replace(":knife:", "\uD83D\uDD2A");
        text = text.replace(":pensive:", "\uD83D\uDE14");
        text = text.replace(":salute:", "\uD83E\uDEE1");
        text = text.replace(":thumb_up:", "\uD83D\uDC4D");
        text = text.replace(":thumbs_up:", "\uD83D\uDC4D");
        text = text.replace(":thumbup:", "\uD83D\uDC4D");
        text = text.replace(":handshake:", "\uD83E\uDD1D");
        text = text.replace(":v:", "✌");


        return new ServerboundChatPacket(text, packet.timeStamp(), packet.salt(), packet.signature(), packet.lastSeenMessages());
    }

}
