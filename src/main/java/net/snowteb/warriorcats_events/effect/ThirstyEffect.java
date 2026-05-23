package net.snowteb.warriorcats_events.effect;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.snowteb.warriorcats_events.attachments.CapabilityManager;
import net.snowteb.warriorcats_events.attachments.ModAttachments;
import net.snowteb.warriorcats_events.network.ModPackets;
import net.snowteb.warriorcats_events.network.packet.s2c.others.ThirstDataSyncStCPacket;

public class ThirstyEffect extends MobEffect {
    public ThirstyEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);


    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {

        if (entity instanceof ServerPlayer player) {
            CapabilityManager.attachmentProvider(player, ModAttachments.PLAYER_THIRST, cap -> {
                if (cap.getThirst() > 0) {
                    cap.subThirst(1);
                    ModPackets.sendToPlayer(new ThirstDataSyncStCPacket(cap.getThirst()), player);
                }
            });
        }
        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return duration % 20 == 0;
    }
}

