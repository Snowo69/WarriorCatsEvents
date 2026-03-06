package net.snowteb.warriorcats_events.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundPlayerChatPacket;
import net.minecraft.world.entity.player.Player;
import net.snowteb.warriorcats_events.client.EntityChatBubbleManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(ClientPacketListener.class)
public class ClientChatPacketMixin {

    @Inject(method = "handlePlayerChat", at = @At("HEAD"))
    private void onChat(ClientboundPlayerChatPacket packet, CallbackInfo ci) {

        UUID sender = packet.sender();
        Component message = Component.literal(packet.body().content());

        Minecraft mc = Minecraft.getInstance();

        if (mc.level != null) {
            Player player = mc.level.getPlayerByUUID(sender);

            int time = (int) (200 + message.getString().length()*0.7f);

            if(player != null) {
                EntityChatBubbleManager.add(sender, message, time);
            }
        }
    }
}
