package net.snowteb.warriorcats_events.network.packet;

import net.minecraft.ChatFormatting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.network.NetworkEvent;
import net.snowteb.warriorcats_events.skills.ISkillData;
import net.snowteb.warriorcats_events.skills.PlayerSkill;
import net.snowteb.warriorcats_events.skills.PlayerSkillProvider;

import java.util.function.Supplier;

import static net.snowteb.warriorcats_events.skills.PlayerSkill.SPEED_SKILL_UUID;

public class CtSMoreSpeedPacket {

    public CtSMoreSpeedPacket() {

    }

    public CtSMoreSpeedPacket(FriendlyByteBuf buf) {

    }

    public void toBytes(FriendlyByteBuf buf) {

    }


    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player == null) return;

            int currentLevel = player.getCapability(PlayerSkillProvider.SKILL_DATA)
                    .map(ISkillData::getSpeedLevel)
                    .orElse(player.getPersistentData().getInt("skill_speed_level"));
            int cost = PlayerSkill.defaultSpeedCost * (currentLevel + 1);
            int remaining = cost - player.totalExperience;



            if (player.totalExperience < cost && currentLevel < PlayerSkill.maxSpeedLevel) {
                player.sendSystemMessage(Component.literal("⚠ You need " + remaining + " XP more.").withStyle(ChatFormatting.RED));
                return;
            }

            if (currentLevel < PlayerSkill.maxSpeedLevel) {
                player.giveExperiencePoints(-cost);
                var attribute = player.getAttribute(Attributes.MOVEMENT_SPEED);
                if (attribute == null) return;
                attribute.removeModifier(SPEED_SKILL_UUID);
                double bonus = 0.025 * (currentLevel + 1);
                attribute.addPermanentModifier(new AttributeModifier(
                        SPEED_SKILL_UUID,
                        "skill_speed_bonus",
                        bonus,
                        AttributeModifier.Operation.MULTIPLY_TOTAL
                ));

                player.getCapability(PlayerSkillProvider.SKILL_DATA).ifPresent(data -> {
                    data.setSpeedLevel(currentLevel + 1);
                });

                player.getPersistentData().putInt("skill_speed_level", currentLevel + 1);

                player.sendSystemMessage(Component.literal("Speed level increased to: " + (currentLevel + 1)));
            }
            else {
                player.sendSystemMessage(Component.literal("Speed skill is maxed! : Level " + (currentLevel)).withStyle(ChatFormatting.YELLOW));
            }

        });
        return true;
    }

}
