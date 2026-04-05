package net.snowteb.warriorcats_events.entity.client;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.snowteb.warriorcats_events.WarriorCatsEvents;
import net.snowteb.warriorcats_events.entity.custom.WCatEntity;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.Animation;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.model.GeoModel;

public class ElytraModel extends GeoModel<WCatEntity> {

    private ResourceLocation texture;

    public static final ResourceLocation DEV_TEXTURE =
            new ResourceLocation(WarriorCatsEvents.MODID, "textures/entity/accessories/elytra_dev.png");
    public static final ResourceLocation DEV_TEXTURE2 =
            new ResourceLocation(WarriorCatsEvents.MODID, "textures/entity/accessories/elytra_dev2.png");

    public ElytraModel() {
        texture = new ResourceLocation("minecraft", "textures/entity/elytra.png");
    }

    @Override
    public ResourceLocation getModelResource(WCatEntity animatable) {
        return new ResourceLocation(WarriorCatsEvents.MODID, "geo/wcat.elytra.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(WCatEntity animatable) {
        return texture;
    }

    @Override
    public ResourceLocation getAnimationResource(WCatEntity squirrelEntity) {
        return new ResourceLocation(WarriorCatsEvents.MODID, "animations/elytra.animation.json");
    }

    @Override
    public void setCustomAnimations(WCatEntity animatable, long instanceId, AnimationState<WCatEntity> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);

    }

    public void setTexture(ResourceLocation texture) {
        this.texture = texture;
    }
}
