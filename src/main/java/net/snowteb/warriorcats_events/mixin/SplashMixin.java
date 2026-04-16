package net.snowteb.warriorcats_events.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.SplashManager;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(SplashManager.class)
public class SplashMixin {

    @Shadow
    List<String> splashes;

    @Inject(method = "apply(Ljava/util/List;Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)V"
    , at = @At("TAIL"))
    public void addCustomSplashes(List<String> pObject, ResourceManager pResourceManager, ProfilerFiller pProfiler, CallbackInfo ci) {

        List<String> wceSplashes = new ArrayList<>();
        wceSplashes.add("Meow!");
        wceSplashes.add("Purr...");
        wceSplashes.add("Bluestar would be proud of you!");
        wceSplashes.add("Don't be a Tigerstar!");
        wceSplashes.add("Let all cats old enough to...");
        wceSplashes.add("Meowing catches no prey!");
        wceSplashes.add("You of all cats might understand, Fireheart. Sometimes there are no right choices.");
        wceSplashes.add("Whimsical");
        wceSplashes.add("Psychokitty");
        wceSplashes.add("Rawr");
        wceSplashes.add("Damn it Moonmoon...");
        wceSplashes.add("Did you miss the squirrel?");
        wceSplashes.add("Deathberries are yummy, trust!");
        wceSplashes.add("Do you promise to uphold the warrior code?");
        wceSplashes.add("I ask my warrior ancestors to look down on you.");
        wceSplashes.add("Paws and claws!");
        wceSplashes.add("Is that a kittypet?");
        wceSplashes.add("Watch out for two-legs!");
        wceSplashes.add("There is a gathering tonight, don't be late!");
        wceSplashes.add("Into the wild");
        wceSplashes.add("Silverstream I'm drowning!");
        wceSplashes.add("Protect your clan with your life.");

        String name = Minecraft.getInstance().getUser().getName();

        String text = "From this day, <name>paw, you will be known as <name>heart.";
        text = text.replace("<name>", name);
        wceSplashes.add(text);

        String text2 = "Greetings, <name>star!";
        text2 = text2.replace("<name>", name);
        wceSplashes.add(text2);

        this.splashes.addAll(wceSplashes);
    }
}
