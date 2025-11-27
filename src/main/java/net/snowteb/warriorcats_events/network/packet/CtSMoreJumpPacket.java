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
import net.snowteb.warriorcats_events.util.ModAttributes;

import java.util.function.Supplier;

import static net.snowteb.warriorcats_events.skills.PlayerSkill.DMG_SKILL_UUID;
import static net.snowteb.warriorcats_events.skills.PlayerSkill.JUMP_SKILL_UUID;

public class CtSMoreJumpPacket {

    public CtSMoreJumpPacket() {

    }

    public CtSMoreJumpPacket(FriendlyByteBuf buf) {

    }

    public void toBytes(FriendlyByteBuf buf) {

    }


    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player == null) return;

            int currentLevel = player.getCapability(PlayerSkillProvider.SKILL_DATA)
                    .map(ISkillData::getJumpLevel)
                    .orElse(player.getPersistentData().getInt("skill_jump_level"));

            int currentSpeedLevel = player.getCapability(PlayerSkillProvider.SKILL_DATA)
                    .map(ISkillData::getSpeedLevel)
                    .orElse(player.getPersistentData().getInt("skill_speed_level"));
            int currentHPLevel = player.getCapability(PlayerSkillProvider.SKILL_DATA)
                    .map(ISkillData::getHPLevel)
                    .orElse(player.getPersistentData().getInt("skill_hp_level"));

            int cost = PlayerSkill.defaultJumpcost * (currentLevel + 1);
            int remaining = cost - player.totalExperience;

            if (currentSpeedLevel == PlayerSkill.maxSpeedLevel && currentHPLevel == PlayerSkill.maxHPLevel) {

            if (player.totalExperience < cost && currentLevel < PlayerSkill.maxJumpLevel) {
                player.sendSystemMessage(Component.literal("⚠ You need " + remaining + " XP more.").withStyle(ChatFormatting.RED));
                return;
            }
                if (currentLevel < PlayerSkill.maxJumpLevel) {
                    player.giveExperiencePoints(-cost);
                    var attribute = player.getAttribute(ModAttributes.PLAYER_JUMP.get());
                    if (attribute == null) return;

                    attribute.removeModifier(JUMP_SKILL_UUID);
                    double bonus = 0.095 * (currentLevel + 1);
                    attribute.addPermanentModifier(new AttributeModifier(
                            JUMP_SKILL_UUID,
                            "skill_jump_bonus",
                            bonus,
                            AttributeModifier.Operation.ADDITION
                    ));

                    player.getCapability(PlayerSkillProvider.SKILL_DATA).ifPresent(data -> {
                        data.setJumpLevel(currentLevel + 1);
                    });

                    player.getPersistentData().putInt("skill_jump_level", currentLevel + 1);

                    player.sendSystemMessage(Component.literal("Jump level increased to: " + (currentLevel + 1)));
                }
                else {
                    player.sendSystemMessage(Component.literal("Jump skill is maxed! : Level " + (currentLevel)).withStyle(ChatFormatting.YELLOW));
                }
            } else {player.sendSystemMessage(Component.literal("Requierements not present").withStyle(ChatFormatting.RED));}


        });
        return true;
    }

}
