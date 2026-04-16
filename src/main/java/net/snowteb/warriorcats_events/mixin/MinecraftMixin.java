package net.snowteb.warriorcats_events.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraftforge.entity.PartEntity;
import net.snowteb.warriorcats_events.effect.ModEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tocraft.walkers.api.PlayerShape;

@Mixin(Minecraft.class)
public class MinecraftMixin {

    @Inject(method = "shouldEntityAppearGlowing", at = @At("HEAD"), cancellable = true)
    public void modifyGlowing(Entity pEntity, CallbackInfoReturnable<Boolean> cir) {
        Minecraft minecraft = (Minecraft)(Object)this;
        if (minecraft.player != null && minecraft.player.hasEffect(ModEffects.SHARP_SCENT.get())){
            if (PlayerShape.getCurrentShape(minecraft.player) == pEntity) return;
            if (!(pEntity instanceof LivingEntity
                    || pEntity instanceof ItemEntity
                    || pEntity instanceof PartEntity<?>
                    || pEntity instanceof PrimedTnt
            )) return;

            if (pEntity instanceof ArmorStand) return;

            if (minecraft.player.distanceTo(pEntity) < 20) {
                cir.setReturnValue(true);
            }

        }

    }
}
