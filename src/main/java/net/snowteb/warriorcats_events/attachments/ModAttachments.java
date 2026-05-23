package net.snowteb.warriorcats_events.attachments;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.snowteb.warriorcats_events.WarriorCatsEvents;

import java.util.function.Supplier;

public class ModAttachments {

    public static final DeferredRegister<AttachmentType<?>> ATTACHMENTS =
            DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, WarriorCatsEvents.MODID);

    public static final Supplier<AttachmentType<PlayerThirst>> PLAYER_THIRST =
            ATTACHMENTS.register(
                    "player_thirst", () -> AttachmentType.serializable(PlayerThirst::new).build()
            );

    public static final Supplier<AttachmentType<PlayerStealth>> PLAYER_STEALTH =
            ATTACHMENTS.register(
                    "stealth", () -> AttachmentType.serializable(PlayerStealth::new).build()
            );

    public static final Supplier<AttachmentType<PlayerSkill>> PLAYER_SKILL =
            ATTACHMENTS.register(
                    "skill", () -> AttachmentType.serializable(PlayerSkill::new).build()
            );

    public static final Supplier<AttachmentType<WCEPlayerData>> PLAYER_WCE_DATA =
            ATTACHMENTS.register(
                    "wce_data", () -> AttachmentType.serializable(WCEPlayerData::new).build()
            );

    public static void register(IEventBus bus) {
        ATTACHMENTS.register(bus);
    }
}
