package net.snowteb.warriorcats_events.entity.client;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.snowteb.warriorcats_events.WarriorCatsEvents;
import net.snowteb.warriorcats_events.entity.custom.BadgerEntity;
import net.snowteb.warriorcats_events.entity.custom.PigeonEntity;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class BadgerModel extends GeoModel<BadgerEntity> {


    @Override
    public ResourceLocation getModelResource(BadgerEntity object) {
        return new ResourceLocation(WarriorCatsEvents.MODID, "geo/badger.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(BadgerEntity entity) {
        return new ResourceLocation(WarriorCatsEvents.MODID, "textures/entity/badger/badger_1.png");
    }

    @Override
    public ResourceLocation getAnimationResource(BadgerEntity animatable) {
        return new ResourceLocation(WarriorCatsEvents.MODID, "animations/badger.animation.json");
    }


    @Override
    public void setCustomAnimations(BadgerEntity animatable, long instanceId, AnimationState<BadgerEntity> animationState) {
        CoreGeoBone head = getAnimationProcessor().getBone("mainhead");

        if (head != null) {
            EntityModelData entityModelData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);

            head.setRotX(entityModelData.headPitch() * Mth.DEG_TO_RAD);
            head.setRotY(entityModelData.netHeadYaw() * Mth.DEG_TO_RAD);
        }

    }
}
