package net.snowteb.warriorcats_events.event;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.snowteb.warriorcats_events.WarriorCatsEvents;
import net.snowteb.warriorcats_events.effect.ModEffects;
import net.snowteb.warriorcats_events.entity.ModEntities;
import net.snowteb.warriorcats_events.entity.custom.WCatEntity;
import net.snowteb.warriorcats_events.item.ModFoodHerbs;
import net.snowteb.warriorcats_events.item.ModItems;
import net.snowteb.warriorcats_events.network.ModPackets;
import net.snowteb.warriorcats_events.network.packet.SyncSkillDataPacket;
import net.snowteb.warriorcats_events.network.packet.ThirstDataSyncStCPacket;
import net.snowteb.warriorcats_events.skills.ISkillData;
import net.snowteb.warriorcats_events.skills.PlayerSkillProvider;
import net.snowteb.warriorcats_events.stealth.PlayerStealthProvider;
import net.snowteb.warriorcats_events.thirst.PlayerThirstProvider;
import tocraft.walkers.api.PlayerShape;

@Mod.EventBusSubscriber(modid = WarriorCatsEvents.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModEventsForge {
    @SubscribeEvent
    public static void onFoodEaten(LivingEntityUseItemEvent.Finish event) {
        if (!(event.getEntity() instanceof Player player)) return;
        ItemStack stack = event.getItem();

        if (!stack.isEdible()) return;

        player.getCapability(PlayerThirstProvider.PLAYER_THIRST).ifPresent(thirst -> {

            if (stack.getItem().getFoodProperties() == ModFoodHerbs.SORREL) {
                int randomThirst = 2 + player.getRandom().nextInt(3);
                thirst.addThirst(randomThirst);
            }

            if (stack.getItem().getFoodProperties() == ModFoodHerbs.TRAVELING_HERBS) {
                thirst.addThirst(4);
            }

            if (stack.getItem().getFoodProperties() == ModFoodHerbs.MOUSE_FOOD) {
                int randomThirst = 1 + player.getRandom().nextInt(2);
                thirst.addThirst(randomThirst);
            }

            if (stack.getItem().getFoodProperties() == ModFoodHerbs.SQUIRREL_FOOD) {
                int randomThirst = 2 + player.getRandom().nextInt(2);
                thirst.addThirst(randomThirst);
            }

            if (stack.getItem().getFoodProperties() == ModFoodHerbs.PIGEON_FOOD) {
                int randomThirst = 2 + player.getRandom().nextInt(2);
                thirst.addThirst(randomThirst);
            }

            if (stack.is(Items.CHICKEN) || stack.is(Items.PORKCHOP)
                    || stack.is(Items.BEEF) || stack.is(Items.MUTTON)
                    || stack.is(Items.RABBIT) || stack.is(Items.SALMON)
                    || stack.is(Items.COD) || stack.is(Items.TROPICAL_FISH)
                    || stack.is(Items.SWEET_BERRIES)) {
                int randomThirst = 1;
                thirst.addThirst(randomThirst);
                player.getFoodData().eat(4, 0.75f);
            }


            ModPackets.sendToPlayer(
                    new ThirstDataSyncStCPacket(thirst.getThirst()),
                    (ServerPlayer) player
            );
        });

        if (stack.getItem() == ModItems.YARROW.get()) {

            if (player.hasEffect(MobEffects.POISON)) {
                player.removeEffect(MobEffects.POISON);
            }
            if (player.hasEffect(ModEffects.DEATHBERRIES.get())) {
                player.removeEffect(ModEffects.DEATHBERRIES.get());
            }

        }
    }



    @SubscribeEvent
    public static void onFall(LivingFallEvent event) {
        LivingEntity entity = event.getEntity();

        ServerPlayer owner = null;
        if (entity instanceof ServerPlayer sp) {
            owner = sp;
        } else {
            if (entity.level() == null || !entity.level().isClientSide()) {
                var server = entity.getServer();
                if (server != null) {
                    for (ServerPlayer candidate : server.getPlayerList().getPlayers()) {
                        LivingEntity currentShape = PlayerShape.getCurrentShape(candidate);
                        if (currentShape != null && currentShape.getUUID().equals(entity.getUUID())) {
                            owner = candidate;
                            break;
                        }
                    }
                }
            }
        }
        if (owner == null) {
            return;
        }
        owner.getCapability(PlayerSkillProvider.SKILL_DATA).ifPresent(cap -> {
            if (cap.getJumpLevel() > 2) {
                event.setDistance(event.getDistance() - 3f);
            }
        });
    }

    @SubscribeEvent
    public static void onEntityJoinWorld(EntityJoinLevelEvent event) {
        if (event.getEntity() instanceof Creeper creeper) {
            creeper.goalSelector.addGoal(3,
                    new AvoidEntityGoal<>(
                            creeper,
                            WCatEntity.class,
                            8.0F,
                            1.0D,
                            1.2D
                    )
            );
        }

    }

}
