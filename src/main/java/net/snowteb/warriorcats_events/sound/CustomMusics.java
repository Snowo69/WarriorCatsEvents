package net.snowteb.warriorcats_events.sound;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.Music;

public class CustomMusics {

    public record BackgroundMusic(Music music, String title) {

        public Component message() {
            return Component.empty()
                    .append(Component.translatable("wce.music_now_playing").withStyle(ChatFormatting.WHITE))
                    .append("\n")
                    .append(Component.literal(this.title())
                            .withStyle(ChatFormatting.AQUA).withStyle(ChatFormatting.UNDERLINE));
        }
    }

    public static final BackgroundMusic MUSIC_ANCIENT_TUNNELS = new BackgroundMusic(new Music(
            ModSounds.ANCIENT_TUNNELS.getHolder().get(),
            280,
            800,
            true
    ), "Ancient Tunnels - Sharon Hurvitz");

    public static final BackgroundMusic MUSIC_CROSSING_THE_RIVER = new BackgroundMusic(new Music(
            ModSounds.CROSSING_THE_RIVER.getHolder().get(),
            280,
            800,
            true
    ), "Crossing The River - Sharon Hurvitz, Carl-Isaak Krulewitch");

    public static final BackgroundMusic MUSIC_DAWN_PATROL = new BackgroundMusic(new Music(
            ModSounds.DAWN_PATROL.getHolder().get(),
            280,
            800,
            true
    ), "Dawn Patrol - Sharon Hurvitz");

    public static final BackgroundMusic MUSIC_LEAFBARE = new BackgroundMusic(new Music(
            ModSounds.LEAFBARE.getHolder().get(),
            280,
            800,
            true
    ), "Leafbare - Sharon Hurvitz");

    public static final BackgroundMusic MUSIC_MOONHIGH_VIGIL = new BackgroundMusic(new Music(
            ModSounds.MOONHIGH_VIGIL.getHolder().get(),
            280,
            800,
            true
    ), "Moonhigh Vigil - Sharon Hurvitz");

    public static final BackgroundMusic MUSIC_NEWLEAF = new BackgroundMusic(new Music(
            ModSounds.NEWLEAF.getHolder().get(),
            280,
            800,
            true
    ), "Newleaf - Sharon Hurvitz");

    public static final BackgroundMusic MUSIC_THE_MEDICINE_CATS_DEN = new BackgroundMusic(new Music(
            ModSounds.THE_MEDICINE_CATS_DEN.getHolder().get(),
            280,
            800,
            true
    ), "The Medicine Cat's Den - Sharon Hurvitz, Carl-Isaak Krulewitch");

    public static final BackgroundMusic MUSIC_THE_MOONSTONE = new BackgroundMusic(new Music(
            ModSounds.THE_MOONSTONE.getHolder().get(),
            280,
            800,
            true
    ), "The Moonstone - Sharon Hurvitz");

    public static final BackgroundMusic MUSIC_DOES_IT_HAVE_TO_END_ALREADY = new BackgroundMusic(new Music(
            ModSounds.DOES_IT_HAVE_TO_END_ALREADY.getHolder().get(),
            280,
            800,
            true
    ), "does it have to end already - Joabi");

    public static final BackgroundMusic MUSIC_I_MISS_YOU_ALREADY = new BackgroundMusic(new Music(
            ModSounds.I_MISS_YOU_ALREADY.getHolder().get(),
            280,
            800,
            true
    ), "i miss you already - Joabi");

    public static final BackgroundMusic MUSIC_I_WANTED_IT_TO_BE_YOU = new BackgroundMusic(new Music(
            ModSounds.I_WANTED_IT_TO_BE_YOU.getHolder().get(),
            280,
            800,
            true
    ), "i wanted it to be you - Joabi");

    public static final BackgroundMusic MUSIC_OH_TO_RELIVE_IT_ONCE_MORE = new BackgroundMusic(new Music(
            ModSounds.OH_TO_RELIVE_IT_ONCE_MORE.getHolder().get(),
            280,
            800,
            true
    ), "oh to relive it once more - Joabi");

    public static final BackgroundMusic MUSIC_YOU_USED_TO_REMEMBER = new BackgroundMusic(new Music(
            ModSounds.YOU_USED_TO_REMEMBER.getHolder().get(),
            280,
            800,
            true
    ), "you used to remember - Joabi");

}
