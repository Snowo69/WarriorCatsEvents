package net.snowteb.warriorcats_events.entity.client;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.snowteb.warriorcats_events.WarriorCatsEvents;
import net.snowteb.warriorcats_events.entity.custom.SquirrelEntity;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class SquirrelModel extends GeoModel<SquirrelEntity> {
    @Override
    public ResourceLocation getModelResource(SquirrelEntity squirrelEntity) {
        return ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "geo/squirrel.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(SquirrelEntity squirrelEntity) {
        return ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "textures/entity/squirrel.png");
    }

    @Override
    public ResourceLocation getAnimationResource(SquirrelEntity squirrelEntity) {
        return ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "animations/squirrel.animation.json");
    }

    @Override
    public void setCustomAnimations(SquirrelEntity animatable, long instanceId, AnimationState<SquirrelEntity> animationState) {
        GeoBone head = getAnimationProcessor().getBone("head");

        if (head != null) {
            EntityModelData entityModelData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);

            head.setRotX(entityModelData.headPitch() * Mth.DEG_TO_RAD);
            head.setRotY(entityModelData.netHeadYaw() * Mth.DEG_TO_RAD);
        }
    }
}
