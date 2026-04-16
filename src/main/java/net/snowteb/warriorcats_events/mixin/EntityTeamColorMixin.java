package net.snowteb.warriorcats_events.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraftforge.entity.PartEntity;
import net.snowteb.warriorcats_events.effect.ModEffects;
import net.snowteb.warriorcats_events.util.ModTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(Entity.class)
public class EntityTeamColorMixin {

    @Inject(method = "getTeamColor", at = @At("HEAD"), cancellable = true)
    public void getNewTeamColor(CallbackInfoReturnable<Integer> cir) {
        Entity entity =  (Entity)(Object)this;
        if (!entity.level().isClientSide) return;

        if (!(entity instanceof LivingEntity
                || entity instanceof ItemEntity
                || entity instanceof PartEntity<?>
                || entity instanceof PrimedTnt
        )) return;

        if (entity instanceof ArmorStand) return;

        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return;
        if (!player.hasEffect(ModEffects.SHARP_SCENT.get())) return;
        if (player.distanceTo(entity) > 20) return;

        int color = 0xd4b883;

        MobCategory category = entity.getType().getCategory();
        if (category == MobCategory.MONSTER || entity.getType().is(ModTags.EntityTypes.PREDATOR_MOBS)) {
            color = 0xba5454;
        } else if (category == MobCategory.MISC) {
            color = 0xc9c9c9;
        } else if (category == MobCategory.WATER_AMBIENT
                || category == MobCategory.WATER_CREATURE
                || category == MobCategory.UNDERGROUND_WATER_CREATURE) {
            color = 0x7470ab;
        }

        if (entity instanceof ItemEntity) {
            color = 0xFFB244;
        }

        if (entity.getType().is(ModTags.EntityTypes.PREY_MOBS)) {
            color = 0xd4b883;
        }

        if (entity instanceof OwnableEntity ownable && Objects.equals(ownable.getOwnerUUID(), player.getUUID())) {
            color = 0x83b7de;
        }

        cir.setReturnValue(color);
        cir.cancel();
    }
}
