package net.snowteb.warriorcats_events.event;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.FoliageColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.snowteb.warriorcats_events.WarriorCatsEvents;
import net.snowteb.warriorcats_events.block.ModBlocks;
import net.snowteb.warriorcats_events.entity.ModEntities;
import net.snowteb.warriorcats_events.entity.client.MouseRenderer;
import net.snowteb.warriorcats_events.entity.client.SquirrelRenderer;
import net.snowteb.warriorcats_events.entity.client.WCRenderer;
import tocraft.walkers.api.model.ClassArmProvider;
import tocraft.walkers.api.model.EntityArms;

@Mod.EventBusSubscriber(modid = WarriorCatsEvents.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModEvents3 {
    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.MOUSE.get(), MouseRenderer::new);
        event.registerEntityRenderer(ModEntities.SQUIRREL.get(), SquirrelRenderer::new);
        event.registerEntityRenderer(ModEntities.WCAT.get(), WCRenderer::new);


    }


    @SubscribeEvent
    public static void registerBlockColors(RegisterColorHandlersEvent.Block event) {
        event.register(
                (state, world, pos, tintIndex) -> {
                    return world != null && pos != null
                            ? BiomeColors.getAverageFoliageColor(world, pos)
                            : FoliageColor.getDefaultColor();
                },
                ModBlocks.LEAF_DOOR.get()
        );
    }


    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {



        EntityArms.register(HumanoidModel.class, new ClassArmProvider<HumanoidModel>() {
            @Override
            public ModelPart getArm(LivingEntity entity, HumanoidModel model) {
                return model.rightArm;
            }
        });
    }


}
