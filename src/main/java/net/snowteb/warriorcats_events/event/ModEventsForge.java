package net.snowteb.warriorcats_events.event;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
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
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.snowteb.warriorcats_events.WarriorCatsEvents;
import net.snowteb.warriorcats_events.effect.ModEffects;
import net.snowteb.warriorcats_events.entity.ModEntities;
import net.snowteb.warriorcats_events.entity.custom.WCatEntity;
import net.snowteb.warriorcats_events.item.ModFoodHerbs;
import net.snowteb.warriorcats_events.item.ModItems;
import net.snowteb.warriorcats_events.item.custom.FlowerCrownItem;
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

    /**
     * This modifies some foods so that some will fill thirsts, and some other will fill more hunger.
     */
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
                int randomThirst = 1 + player.getRandom().nextInt(1);
                thirst.addThirst(randomThirst);
                if (!stack.is(Items.SWEET_BERRIES)){
                    player.getFoodData().eat(2, 0.75f);
                }
            }


            if (player instanceof ServerPlayer serverPlayer) {
                ModPackets.sendToPlayer(new ThirstDataSyncStCPacket(thirst.getThirst()), serverPlayer);
            }
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


    /**
     * Every time an entity falls:
     * Check if it is a player, if it is not:
     * Then make a list of all the players in the server, and for every player verify if their UUID is the same as the shape that fell.
     * If it is, then choose it as the owner of the shape.
     * Then check if the owner has Jump level greater than 2. If it does, reduce it's fall distance.
     *
     * If the entity is a Wild cat, reduce its fall distance too.
     */
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
                event.setDistance(Math.max(0f, event.getDistance() - 3f));
            }
        });


        if (entity instanceof WCatEntity) {
            event.setDistance(Math.max(0f, event.getDistance() - 4f));
        }

    }

    /**
     * Every time a creeper spawns, add this goal to it.
     * This so that creepers run away from Wild Cats.
     */
    @SubscribeEvent
    public static void onEntityJoinWorld(EntityJoinLevelEvent event) {
        if (event.getEntity() instanceof Creeper creeper) {
            creeper.goalSelector.addGoal(3,
                    new AvoidEntityGoal<>(
                            creeper,
                            WCatEntity.class,
                            11.0F,
                            1.3D,
                            1.2D
                    )
            );

        }

    }

    /**
     * Every time a player wants to attack a wild cat, check certain conditions, and under that criteria decide whether to let the player attack it or not.
     */
    @SubscribeEvent
    public static void onAttackEntity(AttackEntityEvent event) {
        Player player = event.getEntity();
        Entity target = event.getTarget();

        if (!(target instanceof WCatEntity wcat)) {
            return;
        }

        if (player.level().isClientSide()) {
            return;
        }

        if (!wcat.isTame()) {
            return;
        }

        LivingEntity owner = wcat.getOwner();
        if (owner == null) {
            return;
        }

        if (!owner.getUUID().equals(player.getUUID())) {
            return;
        }

        if (!player.isShiftKeyDown()) {
            event.setCanceled(true);
        }
    }


    @SubscribeEvent
    public static void onPlayerHurt(LivingHurtEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        ItemStack head = player.getItemBySlot(EquipmentSlot.HEAD);

        if (head.getItem() instanceof FlowerCrownItem) {
            head.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(EquipmentSlot.HEAD));
        }
    }


}
