package net.snowteb.warriorcats_events.particles;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.snowteb.warriorcats_events.compat.Compatibilities;
import net.snowteb.warriorcats_events.item.custom.CollarArmorItem;
import net.snowteb.warriorcats_events.sound.ModSounds;
import tocraft.walkers.api.PlayerShape;

@Mod.EventBusSubscriber
public class LivEntTickHandler {

    @SubscribeEvent
    public static void onLivingTick(LivingEvent.LivingTickEvent event) {
        LivingEntity entity = event.getEntity();

        if (!entity.level().isClientSide()) return;

        if (entity.tickCount % 5 == 0) {
            if (entity.onGround() && entity.getDeltaMovement().horizontalDistanceSqr() > 0.001) {
                spawnFootprint(entity);


                ItemStack collarStack = ItemStack.EMPTY;
                ItemStack bodyCollarStack = entity.getItemBySlot(EquipmentSlot.CHEST);
                if (bodyCollarStack.getItem() instanceof CollarArmorItem) {
                    collarStack = bodyCollarStack;
                }

                if (collarStack.isEmpty()) {
                    ItemStack curiosStack = Compatibilities.getCuriosItem(entity.getUUID(), CollarArmorItem.class);

                    if (!curiosStack.isEmpty()) {
                        collarStack = curiosStack;
                    }
                }

                if (collarStack.getItem() instanceof CollarArmorItem collar) {
                    ItemStack stack = entity.getItemBySlot(EquipmentSlot.CHEST);
                    if (collar.hasBell(stack)) {
                        entity.level().playLocalSound(entity.getX(), entity.getY(), entity.getZ(),
                                ModSounds.COLLAR_BELL.get(), SoundSource.AMBIENT, 0.25f, 1.13f + (entity.getRandom().nextFloat() - 0.5f) * 0.03f, false);
                    }
                }
            }
        }
    }

    private static void spawnFootprint(LivingEntity entity) {

        ClientLevel level = (ClientLevel) entity.level();

        double x = entity.getX();
        double y = entity.getY() + 0.01;
        double z = entity.getZ();

        double dx = entity.getDeltaMovement().x;
        double dz = entity.getDeltaMovement().z;

        float yaw = entity.getYRot();

        double forwardX = -Mth.sin((float) Math.toRadians(yaw));
        double forwardZ = Mth.cos((float) Math.toRadians(yaw));

        float sideOffset = (entity.getRandom().nextFloat() - 0.5f) * 0.34f * entity.getScale();

        double finalX = x + -forwardZ * sideOffset;
        double finalZ = z + forwardX * sideOffset;

        if (entity instanceof Player player && player != Minecraft.getInstance().player) {
            LivingEntity shape = PlayerShape.getCurrentShape(player);
            if (shape != null) {
                entity = shape;
            }
        }

        level.addParticle(
                WCEParticles.FOOTPRINT.get().setEntity(entity),
                finalX, y, finalZ,
                dx, 0, dz
        );
    }
}