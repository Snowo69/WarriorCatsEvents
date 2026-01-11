package net.snowteb.warriorcats_events.network.packet.c2s;

import net.minecraft.ChatFormatting;
import net.minecraft.advancements.Advancement;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.network.NetworkEvent;
import net.snowteb.warriorcats_events.skills.ISkillData;
import net.snowteb.warriorcats_events.skills.PlayerSkill;
import net.snowteb.warriorcats_events.skills.PlayerSkillProvider;

import java.util.function.Supplier;

import static net.snowteb.warriorcats_events.skills.PlayerSkill.DMG_SKILL_UUID;

public class CtSMoreDMGPacket {

    public CtSMoreDMGPacket() {

    }

    public CtSMoreDMGPacket(FriendlyByteBuf buf) {

    }

    public void toBytes(FriendlyByteBuf buf) {

    }


    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player == null) return;

            int currentLevel = player.getCapability(PlayerSkillProvider.SKILL_DATA)
                    .map(ISkillData::getDMGLevel)
                    .orElse(player.getPersistentData().getInt("skill_dmg_level"));

            int cost = PlayerSkill.getDefaultDMGcost() * (currentLevel + 1);
            int remaining = cost - player.totalExperience;



            if (player.totalExperience < cost && currentLevel < PlayerSkill.maxDMGLevel) {
                player.sendSystemMessage(Component.literal("⚠ You need " + remaining + " XP more.").withStyle(ChatFormatting.RED));
                return;
            }

            if (currentLevel < PlayerSkill.maxDMGLevel) {
                player.giveExperiencePoints(-cost);
                var attribute = player.getAttribute(Attributes.ATTACK_DAMAGE);
                if (attribute == null) return;

                attribute.removeModifier(DMG_SKILL_UUID);
                double bonus = 0.12 * (currentLevel + 1);
                attribute.addPermanentModifier(new AttributeModifier(
                        DMG_SKILL_UUID,
                        "skill_dmg_bonus",
                        bonus,
                        AttributeModifier.Operation.ADDITION
                ));

                player.getCapability(PlayerSkillProvider.SKILL_DATA).ifPresent(data -> {
                    data.setDMGLevel(currentLevel + 1);
                });

                player.getPersistentData().putInt("skill_dmg_level", currentLevel + 1);

                player.sendSystemMessage(Component.literal("Claws level increased to: " + (currentLevel + 1)));

                if (currentLevel + 1 == PlayerSkill.maxDMGLevel) {
                    MinecraftServer server = player.getServer();
                    if (server != null) {

                        Advancement adv = server.getAdvancements()
                                .getAdvancement(new ResourceLocation("warriorcats_events:skill_damage_advancement"));

                        if (adv != null) {
                            player.getAdvancements().award(adv, "unlock_skill");
                        }
                    }
                }
            }
            else {
                player.sendSystemMessage(Component.literal("Claws skill is maxed! : Level " + (currentLevel)).withStyle(ChatFormatting.YELLOW));
            }

        });
        return true;
    }

}
