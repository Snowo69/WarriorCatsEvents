package net.snowteb.warriorcats_events.entity.client;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.snowteb.warriorcats_events.WarriorCatsEvents;
import net.snowteb.warriorcats_events.entity.custom.SquirrelEntity;
import net.snowteb.warriorcats_events.entity.custom.WCatEntity;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class SquirrelAccessoryModel extends GeoModel<WCatEntity> {
    @Override
    public ResourceLocation getModelResource(WCatEntity squirrelEntity) {
        return new ResourceLocation(WarriorCatsEvents.MODID, "geo/squirrel.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(WCatEntity squirrelEntity) {
        return new ResourceLocation(WarriorCatsEvents.MODID, "textures/entity/squirrel.png");
    }

    @Override
    public ResourceLocation getAnimationResource(WCatEntity squirrelEntity) {
        return new ResourceLocation(WarriorCatsEvents.MODID, "animations/squirrel.animation.json");
    }

}
