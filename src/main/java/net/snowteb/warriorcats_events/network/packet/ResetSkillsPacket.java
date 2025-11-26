package net.snowteb.warriorcats_events.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.network.NetworkEvent;
import net.snowteb.warriorcats_events.skills.PlayerSkill;
import net.snowteb.warriorcats_events.skills.PlayerSkillProvider;
import net.snowteb.warriorcats_events.stealth.PlayerStealthProvider;

import java.util.UUID;
import java.util.function.Supplier;

public class ResetSkillsPacket {
    public ResetSkillsPacket() {}
    public ResetSkillsPacket(FriendlyByteBuf buf) {}
    public void toBytes(FriendlyByteBuf buf) {}

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player == null) return;

            player.getCapability(PlayerSkillProvider.SKILL_DATA).ifPresent(data -> data.setSpeedLevel(0));
            player.getCapability(PlayerSkillProvider.SKILL_DATA).ifPresent(data -> data.setHPLevel(0));

            int speedLevel = player.getPersistentData().getInt("skill_speed_level");
            int hpLevel = player.getPersistentData().getInt("skill_hp_level");

            player.giveExperiencePoints( (PlayerSkill.defaultSpeedCost* speedLevel)/2);
            player.giveExperiencePoints( (PlayerSkill.defaultHPcost* hpLevel)/2 );

            player.getPersistentData().putInt("skill_speed_level", 0);
            player.getPersistentData().putInt("skill_hp_level", 0);

            resetAttribute(Attributes.MAX_HEALTH, PlayerSkill.HP_SKILL_UUID, player);
            resetAttribute(Attributes.MOVEMENT_SPEED, PlayerSkill.SPEED_SKILL_UUID, player);

            player.getCapability(PlayerStealthProvider.STEALTH_MODE).ifPresent(cap -> {
                cap.setUnlocked(false);
                cap.sync(player);
            });


        });
        return true;
    }



    private void resetAttribute(Attribute attribute, UUID skillData, ServerPlayer player) {
        var Attr = player.getAttribute(attribute);
        if (Attr != null) Attr.removeModifier(skillData);
    }
}
