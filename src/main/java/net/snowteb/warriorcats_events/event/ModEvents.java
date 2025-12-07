package net.snowteb.warriorcats_events.event;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.snowteb.warriorcats_events.WarriorCatsEvents;
import net.snowteb.warriorcats_events.entity.ModEntities;
import net.snowteb.warriorcats_events.entity.custom.*;
import net.snowteb.warriorcats_events.item.ModFoodHerbs;
import net.snowteb.warriorcats_events.network.ModPackets;
import net.snowteb.warriorcats_events.network.packet.ThirstDataSyncStCPacket;
import net.snowteb.warriorcats_events.thirst.PlayerThirstProvider;
import net.snowteb.warriorcats_events.util.ModAttributes;

@Mod.EventBusSubscriber(modid = WarriorCatsEvents.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEvents {

    @SubscribeEvent
    public static void entityAttributeEvent(EntityAttributeCreationEvent event) {
        event.put(ModEntities.MOUSE.get(), MouseEntity.setAttributes().build());
        event.put(ModEntities.SQUIRREL.get(), SquirrelEntity.setAttributes().build());
        event.put(ModEntities.WCAT.get(), WCatEntity.setAttributes().build());
        event.put(ModEntities.PIGEON.get(), PigeonEntity.setAttributes().build());
        event.put(ModEntities.BADGER.get(), BadgerEntity.setAttributes().build());

    }

    @SubscribeEvent
    public static void onEntityAttributeModification(EntityAttributeModificationEvent event) {
        event.add(EntityType.PLAYER, ModAttributes.PLAYER_JUMP.get());
    }

}