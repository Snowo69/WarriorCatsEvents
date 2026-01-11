package net.snowteb.warriorcats_events.sound;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.snowteb.warriorcats_events.WarriorCatsEvents;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS ,WarriorCatsEvents.MODID);

//

    public static final RegistryObject<SoundEvent> GENERATIONS =
            registerSoundEvents("generations");
    public static final RegistryObject<SoundEvent> GENERATIONS_BG =
            registerSoundEvents("generations_bg");

    public static final RegistryObject<SoundEvent> MOUSE_AMBIENT =
            registerSoundEvents("entity.mouse.ambient");
    public static final RegistryObject<SoundEvent> MOUSE_HURT =
            registerSoundEvents("entity.mouse.hurt");
    public static final RegistryObject<SoundEvent> MOUSE_DEATH =
            registerSoundEvents("entity.mouse.death");

    public static final RegistryObject<SoundEvent> BADGER_AMBIENT =
            registerSoundEvents("entity.badger.ambient");
    public static final RegistryObject<SoundEvent> BADGER_HURT_ATTACK =
            registerSoundEvents("entity.badger.hurt_and_attack");
    public static final RegistryObject<SoundEvent> BADGER_SCREECH =
            registerSoundEvents("entity.badger.scream");

    public static final RegistryObject<SoundEvent> SQUIRREL_AMBIENT =
            registerSoundEvents("entity.squirrel.ambient");
    public static final RegistryObject<SoundEvent> SQUIRREL_HURT =
            registerSoundEvents("entity.squirrel.hurt");
    public static final RegistryObject<SoundEvent> SQUIRREL_DEATH =
            registerSoundEvents("entity.squirrel.death");


    public static final RegistryObject<SoundEvent> WILDCAT_SCREAM =
            registerSoundEvents("entity.wildcat.scream");


    public static final RegistryObject<SoundEvent> SCRAPING_WOOD =
            registerSoundEvents("ambient.scraping_wood");
    public static final RegistryObject<SoundEvent> SHORT_WOOSH =
            registerSoundEvents("ambient.short_woosh");
    public static final RegistryObject<SoundEvent> LONG_WOOSH =
            registerSoundEvents("ambient.long_woosh");
    public static final RegistryObject<SoundEvent> STEALTH_WOOSH =
            registerSoundEvents("ambient.stealth_woosh");


    public static final RegistryObject<SoundEvent> LEADER_CALL =
            registerSoundEvents("leader.cat_call");


    private static RegistryObject<SoundEvent> registerSoundEvents(String name) {
        return SOUND_EVENTS.register(name,
                () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(WarriorCatsEvents.MODID, name)));
    }

    public static void register(IEventBus bus) {SOUND_EVENTS.register(bus);}
}
