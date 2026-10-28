package net.snowteb.warriorcats_events.mixin;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.snowteb.warriorcats_events.attachments.ModAttachments;
import net.snowteb.warriorcats_events.clan.ClanData;
import net.snowteb.warriorcats_events.entity.custom.wcat.WCatEntity;
import net.snowteb.warriorcats_events.managers.MorphNameUtils;
import net.snowteb.warriorcats_events.util.CuteTextUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tocraft.walkers.api.PlayerShape;

import java.util.UUID;

@Mixin(value = ChatType.class, priority = 9999)
public class ChatTypeMorphNameMixin {

    @Inject(method = "bind(Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/world/entity/Entity;)Lnet/minecraft/network/chat/ChatType$Bound;"
    , at = @At("HEAD"), cancellable = true)
    private static void redirectChatMessage(ResourceKey<ChatType> pChatTypeKey, Entity pEntity, CallbackInfoReturnable<ChatType.Bound> cir) {

        if (pEntity instanceof ServerPlayer player) {
            boolean shouldRewriteName = player.getData(ModAttachments.PLAYER_WCE_DATA).isMorphNameInChat();
            if (shouldRewriteName && PlayerShape.getCurrentShape(player) instanceof WCatEntity){

                Component newName = MorphNameUtils.getChatMorphName(player);

                cir.setReturnValue(net.minecraft.network.chat.ChatType.bind(pChatTypeKey, pEntity.level().registryAccess(), newName));
                cir.cancel();
            }
        }
    }

}
