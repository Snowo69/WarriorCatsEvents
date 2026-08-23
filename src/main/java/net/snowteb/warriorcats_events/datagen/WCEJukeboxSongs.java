package net.snowteb.warriorcats_events.datagen;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.JukeboxSong;
import net.snowteb.warriorcats_events.WarriorCatsEvents;
import net.snowteb.warriorcats_events.sound.ModSounds;

public class WCEJukeboxSongs {

    public static final ResourceKey<JukeboxSong> GENERATIONS = create("generations_music_disc");

    public static ResourceKey<JukeboxSong> create(String name) {
        return ResourceKey.create(Registries.JUKEBOX_SONG,
                ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, name));
    }

    public static void bootstrap(BootstrapContext<JukeboxSong> context) {
        context.register(GENERATIONS, new JukeboxSong(
                ModSounds.GENERATIONS,
                Component.translatable("item.warriorcats_events.generations_music_disc.desc"),
                2650f,6
        ));
    }

}
