package net.snowteb.warriorcats_events.sound;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.snowteb.warriorcats_events.WarriorCatsEvents;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(BuiltInRegistries.SOUND_EVENT ,WarriorCatsEvents.MODID);

    public static final DeferredHolder<SoundEvent, SoundEvent> GENERATIONS =
            registerSoundEvents("generations");
    public static final DeferredHolder<SoundEvent, SoundEvent> GENERATIONS_BG =
            registerSoundEvents("generations_bg");

    public static final DeferredHolder<SoundEvent, SoundEvent> MOUSE_AMBIENT =
            registerSoundEvents("entity.mouse.ambient");
    public static final DeferredHolder<SoundEvent, SoundEvent> MOUSE_HURT =
            registerSoundEvents("entity.mouse.hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> MOUSE_DEATH =
            registerSoundEvents("entity.mouse.death");

    public static final DeferredHolder<SoundEvent, SoundEvent> BADGER_AMBIENT =
            registerSoundEvents("entity.badger.ambient");
    public static final DeferredHolder<SoundEvent, SoundEvent> BADGER_HURT_ATTACK =
            registerSoundEvents("entity.badger.hurt_and_attack");
    public static final DeferredHolder<SoundEvent, SoundEvent> BADGER_SCREECH =
            registerSoundEvents("entity.badger.scream");

    public static final DeferredHolder<SoundEvent, SoundEvent> SQUIRREL_AMBIENT =
            registerSoundEvents("entity.squirrel.ambient");
    public static final DeferredHolder<SoundEvent, SoundEvent> SQUIRREL_HURT =
            registerSoundEvents("entity.squirrel.hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> SQUIRREL_DEATH =
            registerSoundEvents("entity.squirrel.death");


    public static final DeferredHolder<SoundEvent, SoundEvent> WILDCAT_SCREAM =
            registerSoundEvents("entity.wildcat.scream");
    public static final DeferredHolder<SoundEvent, SoundEvent> WILDCAT_COUGH =
            registerSoundEvents("entity.cat_cough");

    public static final DeferredHolder<SoundEvent, SoundEvent> SCRAPING_WOOD =
            registerSoundEvents("ambient.scraping_wood");
    public static final DeferredHolder<SoundEvent, SoundEvent> SHORT_WOOSH =
            registerSoundEvents("ambient.short_woosh");
    public static final DeferredHolder<SoundEvent, SoundEvent> LONG_WOOSH =
            registerSoundEvents("ambient.long_woosh");
    public static final DeferredHolder<SoundEvent, SoundEvent> STEALTH_WOOSH =
            registerSoundEvents("ambient.stealth_woosh");


    public static final DeferredHolder<SoundEvent, SoundEvent> LEADER_CALL =
            registerSoundEvents("leader.cat_call");

    public static final DeferredHolder<SoundEvent, SoundEvent> COLLAR_BELL =
            registerSoundEvents("entity.collar_bell");


    public static final DeferredHolder<SoundEvent, SoundEvent> MENU_BUTTON =
            registerSoundEvents("menu.button");
    public static final DeferredHolder<SoundEvent, SoundEvent> MENU_CLICK =
            registerSoundEvents("menu.click");
    public static final DeferredHolder<SoundEvent, SoundEvent> MENU_OPEN =
            registerSoundEvents("menu.open");
    public static final DeferredHolder<SoundEvent, SoundEvent> MENU_SECTION =
            registerSoundEvents("menu.section");
    public static final DeferredHolder<SoundEvent, SoundEvent> MENU_SELECT =
            registerSoundEvents("menu.select");
    public static final DeferredHolder<SoundEvent, SoundEvent> MENU_ACCEPT =
            registerSoundEvents("menu.accept");


    public static final DeferredHolder<SoundEvent, SoundEvent> EAGLE_AMBIENT =
            registerSoundEvents("entity.eagle.ambient");
    public static final DeferredHolder<SoundEvent, SoundEvent> EAGLE_HURT =
            registerSoundEvents("entity.eagle.hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> EAGLE_DEATH =
            registerSoundEvents("entity.eagle.death");
    public static final DeferredHolder<SoundEvent, SoundEvent> EAGLE_ATTACK =
            registerSoundEvents("entity.eagle.attack");
    public static final DeferredHolder<SoundEvent, SoundEvent> EAGLE_CALL =
            registerSoundEvents("entity.eagle.call");

    public static final DeferredHolder<SoundEvent, SoundEvent> LIZARD_AMBIENT =
            registerSoundEvents("entity.lizard.ambient");

    public static final DeferredHolder<SoundEvent, SoundEvent> PIGEON_AMBIENT =
            registerSoundEvents("entity.pigeon.ambient");



    public static final DeferredHolder<SoundEvent, SoundEvent> ANCIENT_TUNNELS =
            registerSoundEvents("ancient_tunnels");
    public static final DeferredHolder<SoundEvent, SoundEvent> CROSSING_THE_RIVER =
            registerSoundEvents("crossing_the_river");
    public static final DeferredHolder<SoundEvent, SoundEvent> DAWN_PATROL =
            registerSoundEvents("dawn_patrol");
    public static final DeferredHolder<SoundEvent, SoundEvent> LEAFBARE =
            registerSoundEvents("leafbare");
    public static final DeferredHolder<SoundEvent, SoundEvent> MOONHIGH_VIGIL =
            registerSoundEvents("moonhigh_vigil");
    public static final DeferredHolder<SoundEvent, SoundEvent> NEWLEAF =
            registerSoundEvents("newleaf");
    public static final DeferredHolder<SoundEvent, SoundEvent> THE_MEDICINE_CATS_DEN =
            registerSoundEvents("the_medicine_cats_den");
    public static final DeferredHolder<SoundEvent, SoundEvent> THE_MOONSTONE =
            registerSoundEvents("the_moonstone");
    public static final DeferredHolder<SoundEvent, SoundEvent> DOES_IT_HAVE_TO_END_ALREADY =
            registerSoundEvents("does_it_have_to_end_already");
    public static final DeferredHolder<SoundEvent, SoundEvent> I_MISS_YOU_ALREADY =
            registerSoundEvents("i_miss_you_already");
    public static final DeferredHolder<SoundEvent, SoundEvent> I_WANTED_IT_TO_BE_YOU =
            registerSoundEvents("i_wanted_it_to_be_you");
    public static final DeferredHolder<SoundEvent, SoundEvent> OH_TO_RELIVE_IT_ONCE_MORE =
            registerSoundEvents("oh_to_relive_it_once_more");
    public static final DeferredHolder<SoundEvent, SoundEvent> YOU_USED_TO_REMEMBER =
            registerSoundEvents("you_used_to_remember");

    public static final DeferredHolder<SoundEvent, SoundEvent> DIALOGUE_BOOP_1 =
            registerSoundEvents("dialogue_boop_1");


    private static DeferredHolder<SoundEvent, SoundEvent> registerSoundEvents(String name) {
        return SOUND_EVENTS.register(name,
                () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, name)));
    }

    public static void register(IEventBus bus) {SOUND_EVENTS.register(bus);}
}
