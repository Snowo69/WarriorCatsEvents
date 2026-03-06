package net.snowteb.warriorcats_events.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.Parrot;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.snowteb.warriorcats_events.entity.custom.PigeonEntity;
import net.snowteb.warriorcats_events.item.ModItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mob.class)
public class ParrotDeathLootMobMixin {

    @Inject(method = "dropCustomDeathLoot", at = @At("TAIL"))
    private void onDropCustomDeathLoot(DamageSource pSource, int pLooting, boolean pRecentlyHit, CallbackInfo ci) {
        if ((Object)this instanceof Parrot parrot) {
            if (!((Object)this instanceof PigeonEntity)) {

                Parrot.Variant variant = parrot.getVariant();
                ItemStack itemEarned;

                switch (variant) {
                    case BLUE -> itemEarned = new ItemStack(ModItems.BLUE_PARROT_FEATHER.get());
                    case GRAY -> itemEarned = new ItemStack(ModItems.GRAY_PARROT_FEATHER.get());
                    case GREEN -> itemEarned = new ItemStack(ModItems.GREEN_PARROT_FEATHER.get());
                    case RED_BLUE ->  itemEarned = new ItemStack(ModItems.RED_PARROT_FEATHER.get());
                    case YELLOW_BLUE ->   itemEarned = new ItemStack(ModItems.LIGHTBLUE_PARROT_FEATHER.get());
                    default -> itemEarned = new ItemStack(ModItems.RED_PARROT_FEATHER.get());
                }

                if (itemEarned.isEmpty()) return;

                ItemEntity drop = new ItemEntity(
                        parrot.level(),
                        parrot.getX(),
                        parrot.getY() + 0.2,
                        parrot.getZ(),
                        itemEarned.copyWithCount(parrot.getRandom().nextInt(4))
                );

                if (parrot.level() instanceof ServerLevel sLevel) {
                    sLevel.addFreshEntity(drop);
                }

            }
        }
    }
}
