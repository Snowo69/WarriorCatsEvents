package net.snowteb.warriorcats_events.entity.client;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.snowteb.warriorcats_events.WarriorCatsEvents;
import net.snowteb.warriorcats_events.entity.custom.EagleEntity;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class EagleModel extends GeoModel<EagleEntity> {


    @Override
    public ResourceLocation getModelResource(EagleEntity object) {
        return new ResourceLocation(WarriorCatsEvents.MODID, "geo/eagle.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(EagleEntity entity) {
        return new ResourceLocation(WarriorCatsEvents.MODID, "textures/entity/eagle.png");
    }

    @Override
    public ResourceLocation getAnimationResource(EagleEntity animatable) {
        return new ResourceLocation(WarriorCatsEvents.MODID, "animations/eagle.animation.json");
    }


    @Override
    public void setCustomAnimations(EagleEntity animatable, long instanceId, AnimationState<EagleEntity> animationState) {
        CoreGeoBone body = getAnimationProcessor().getBone("body");
        if (body != null && animatable.isFlying()) {
            EntityModelData entityModelData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);

            body.setRotX(entityModelData.headPitch() * -Mth.DEG_TO_RAD);
            body.setRotY(entityModelData.netHeadYaw() * -Mth.DEG_TO_RAD);
        }

        getBone("feather_crown").ifPresent(bone -> bone.setHidden(!animatable.isTame()));


        CoreGeoBone head = getAnimationProcessor().getBone("mainHead");

        if (head != null && !animatable.isFlying()) {
            EntityModelData entityModelData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);

            head.setRotX(entityModelData.headPitch() * Mth.DEG_TO_RAD);
            head.setRotY((entityModelData.netHeadYaw() * Mth.DEG_TO_RAD)/1.5f);

        }

    }
}
