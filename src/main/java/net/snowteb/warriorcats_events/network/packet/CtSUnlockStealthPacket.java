package net.snowteb.warriorcats_events.network.packet;

import net.minecraft.ChatFormatting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.snowteb.warriorcats_events.skills.ISkillData;
import net.snowteb.warriorcats_events.skills.PlayerSkill;
import net.snowteb.warriorcats_events.skills.PlayerSkillProvider;
import net.snowteb.warriorcats_events.stealth.PlayerStealthProvider;

import java.util.function.Supplier;

public class CtSUnlockStealthPacket {

    public CtSUnlockStealthPacket() {}

    public static void encode(CtSUnlockStealthPacket msg, FriendlyByteBuf buf) {}

    public static CtSUnlockStealthPacket decode(FriendlyByteBuf buf) {
        return new CtSUnlockStealthPacket();
    }

    public static void handle(CtSUnlockStealthPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();

            if (player == null) return;

            int cost = PlayerSkill.getDefaultStealthcost();
            int remaining = cost - player.totalExperience;

            int currentArmorLevel = player.getCapability(PlayerSkillProvider.SKILL_DATA)
                    .map(ISkillData::getArmorLevel)
                    .orElse(player.getPersistentData().getInt("skill_armor_level"));
            int currentJumpLevel = player.getCapability(PlayerSkillProvider.SKILL_DATA)
                    .map(ISkillData::getJumpLevel)
                    .orElse(player.getPersistentData().getInt("skill_jump_level"));

            if (currentJumpLevel == PlayerSkill.maxJumpLevel && currentArmorLevel == PlayerSkill.maxArmorLevel) {
                player.getCapability(PlayerStealthProvider.STEALTH_MODE).ifPresent(cap -> {

                    if (cap.isUnlocked()) {
                        player.sendSystemMessage(Component.literal("Stealth is already unlocked!")
                                .withStyle(ChatFormatting.YELLOW));
                        return;
                    }

                    if (player.totalExperience < cost) {
                        player.sendSystemMessage(Component.literal("⚠ You need " + remaining + " XP more.")
                                .withStyle(ChatFormatting.RED));
                        return;
                    }

                    player.giveExperiencePoints(-cost);

                    cap.setUnlocked(true);
                    cap.sync(player);


                    player.sendSystemMessage(Component.literal("Stealth ability unlocked!")
                            .withStyle(ChatFormatting.GREEN));
                });
            }

        });
        ctx.get().setPacketHandled(true);
    }

}
