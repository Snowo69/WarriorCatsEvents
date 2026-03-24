package net.snowteb.warriorcats_events.mixin;

import com.mojang.authlib.minecraft.client.MinecraftClient;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.snowteb.warriorcats_events.network.ModPackets;
import net.snowteb.warriorcats_events.network.packet.c2s.others.CtSSaveChatMorphPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Screen.class)
public class ScreenClickedComponentMixin {

    @Inject(method = "handleComponentClicked", at = @At("HEAD"), cancellable = true)
    public void redirectClickEvent(Style pStyle, CallbackInfoReturnable<Boolean> cir) {

        if (pStyle != null) {
            if (pStyle.getClickEvent() != null) {
                if (pStyle.getClickEvent().getAction() == ClickEvent.Action.OPEN_URL) {
                    if (pStyle.getClickEvent().getValue().startsWith("/wce:")) {
                        Minecraft mc = Minecraft.getInstance();
                        mc.setScreen(null);

                        String[] parts = pStyle.getClickEvent().getValue().split(":");
                        if (mc.player != null) {
                            if (parts.length < 3) {
                                mc.player.displayClientMessage(Component.literal("Invalid key provided.").withStyle(ChatFormatting.GRAY), false);
                                return;
                            }

                            String key = parts[2];

                            ModPackets.sendToServer(new CtSSaveChatMorphPacket(key));
                            cir.setReturnValue(true);
                            cir.cancel();
                        }
                    }
                }
            }
        }

    }

}
