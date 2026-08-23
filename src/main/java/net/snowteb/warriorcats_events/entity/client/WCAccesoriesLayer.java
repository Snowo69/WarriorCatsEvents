package net.snowteb.warriorcats_events.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.PlayerModelPart;
import net.minecraft.world.item.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.snowteb.warriorcats_events.WarriorCatsEvents;
import net.snowteb.warriorcats_events.compat.CompatibilitiesClient;
import net.snowteb.warriorcats_events.entity.custom.LizardEntity;
import net.snowteb.warriorcats_events.entity.custom.wcat.WCGenetics;
import net.snowteb.warriorcats_events.entity.custom.wcat.WCatEntity;
import net.snowteb.warriorcats_events.item.ModItems;
import net.snowteb.warriorcats_events.item.custom.*;
import org.joml.Matrix4f;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;
import software.bernie.geckolib.util.RenderUtils;
import tocraft.walkers.api.PlayerShape;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class WCAccesoriesLayer extends GeoRenderLayer<WCatEntity> {

    private enum Bones {
        HALF_BODY("up"),
        UPPER_BODY("moreup"),
        LOWER_BODY("DOWN"),
        LOWER_BODY_SUB("bodydown2"),
        HEAD("head"),
        TAIL_1("tail"),
        TAIL_2("tailsub"),
        TAIL_3("tail2"),
        FRONT_LEFT_LEG("front_left_leg2upper"),
        FRONT_RIGHT_LEG("front_right_legUPPER"),
        FRONT_LEFT_PAW("front_left_leg2down"),
        FRONT_RIGHT_PAW("front_right_legDOWN"),
        BACK_LEFT_LEG("back_left_leg"),
        BACK_RIGHT_LEG("back_right_leg"),
        UP("up");

        private final String name;

        Bones(String name) {
            this.name = name;
        }

        public String string() {
            return name;
        }
    }

    private final SquirrelAccessoryModel squirrelAccessoryModel = new SquirrelAccessoryModel();

    private final AccessoryModel flowerCrownAccesoryModel = new AccessoryModel(
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "geo/wcat.flowercrown.geo.json"),
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "textures/entity/accessories/flowercrown.png")
    );





    private final AccessoryModel leafManeAccesoryModel = new AccessoryModel(
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "geo/leaf_mane.geo.json"),
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "textures/entity/accessories/leaf_mane.png")
    );





    private final AccessoryModel flowerAccesoryModel = new AccessoryModel(
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "geo/wcat.floweraccessory.geo.json"),
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "textures/entity/accessories/flower_accessory.png")
    );





    private final AccessoryModel tailVinesModel = new AccessoryModel(
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "geo/wcat.tail_vine_wrap.geo2.json"),
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "textures/entity/accessories/green_texture.png")
    );





    private final AccessoryModel socksModel = new AccessoryModel(
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "geo/wcat.socks.geo.json"),
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "textures/entity/accessories/cat_socks.png")
    );





    private final AccessoryModel leafShieldModel = new AccessoryModel(
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "geo/wcat.leaf_shield.geo.json"),
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "textures/entity/accessories/leaf_shield.png")
    );




    private final AccessoryModel tailLichenModel = new AccessoryModel(
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "geo/wcat.tail_lichen.geo.json"),
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "textures/entity/accessories/tail_lichen.png")
    );




    private final AccessoryModel dandelionModel = new AccessoryModel(
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "geo/wcat.head_dandelion.geo.json"),
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "textures/entity/accessories/head_dandelion.png")
    );





    private final AccessoryModel bodyFeathersModelUp = new AccessoryModel(
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "geo/wcat.body_feathers_up.geo.json"),
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "textures/entity/accessories/chicken_feathers.png")
    );
    private final AccessoryModel bodyFeathersModelMid = new AccessoryModel(
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "geo/wcat.body_feathers_mid.geo.json"),
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "textures/entity/accessories/chicken_feathers.png")
    );
    private final AccessoryModel bodyFeathersModelDown = new AccessoryModel(
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "geo/wcat.body_feathers_down.geo.json"),
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "textures/entity/accessories/chicken_feathers.png")
    );

    private final AccessoryModel bodyVultureFeathersModelMid = new AccessoryModel(
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "geo/wcat.body_vulture_feathers_mid.geo.json"),
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "textures/entity/accessories/black_vulture_feathers.png")
    );
    private final AccessoryModel bodyVultureFeathersModelUp = new AccessoryModel(
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "geo/wcat.body_vulture_feathers_up.geo.json"),
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "textures/entity/accessories/black_vulture_feathers.png")
    );





    private final AccessoryModel collarModel = new AccessoryModel(
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "geo/wcat.collar.geo.json"),
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "textures/entity/accessories/collar_white.png")
    );

    private final AccessoryModel collarExtraModel = new AccessoryModel(
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "geo/wcat.collar_extra.geo.json"),
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "textures/entity/accessories/collar_white.png")
    );





    private final AccessoryModel berryModel = new AccessoryModel(
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "geo/wcat.head_berry.geo.json"),
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "textures/entity/accessories/head_sweetberry.png")
    );




    private final AccessoryModel catHatModel = new AccessoryModel(
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "geo/wcat.hat.geo.json"),
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "textures/entity/accessories/cat_hat.png")
    );





    private final AccessoryModel catBowModel = new AccessoryModel(
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "geo/wcat.head_bow.geo.json"),
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "textures/entity/accessories/red_bow.png")
    );




    private final AccessoryModel skullMaskModel = new AccessoryModel(
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "geo/wcat.skull_mask.geo.json"),
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "textures/entity/accessories/skull_mask.png")
    );





    private final AccessoryModel pawWrapModel0 = new AccessoryModel(
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "geo/wcat.paw_wrap_0.geo.json"),
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "textures/entity/accessories/paw_wrap.png")
    );
    private final AccessoryModel pawWrapModel1 = new AccessoryModel(
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "geo/wcat.paw_wrap_1.geo.json"),
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "textures/entity/accessories/paw_wrap.png")
    );





    private final AccessoryModel butterflyWingModel = new AccessoryModel(
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "geo/wcat.head_butterfly_wing.geo.json"),
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "textures/entity/accessories/bluemorphowing.png")
    );





    private final AccessoryModel flowerArmorModelFront = new AccessoryModel(
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "geo/wcat.flower_armor_front.geo.json"),
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "textures/entity/accessories/flower_armor.png")
    );
    private final AccessoryModel flowerArmorModelMid = new AccessoryModel(
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "geo/wcat.flower_armor_mid.geo.json"),
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "textures/entity/accessories/flower_armor.png")
    );
    private final AccessoryModel flowerArmorModelBack = new AccessoryModel(
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "geo/wcat.flower_armor_back.geo.json"),
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "textures/entity/accessories/flower_armor.png")
    );



    private final AccessoryModel beeSuitHead = new AccessoryModel(
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "geo/wcat.bee_head.geo.json"),
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "textures/entity/accessories/bee_suit.png")
    );
    private final AccessoryModel beeSuitBody = new AccessoryModel(
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "geo/wcat.bee_body.geo.json"),
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "textures/entity/accessories/bee_suit.png")
    );
    private final AccessoryModel beeSuitBodyWings = new AccessoryModel(
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "geo/wcat.bee_body_wings.geo.json"),
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "textures/entity/accessories/bee_suit.png")
    );
    private final AccessoryModel beeSuitUpperPaw = new AccessoryModel(
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "geo/wcat.bee_upper_paw.geo.json"),
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "textures/entity/accessories/bee_suit.png")
    );
    private final AccessoryModel beeSuitlowerPaw = new AccessoryModel(
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "geo/wcat.bee_lower_paw.geo.json"),
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "textures/entity/accessories/bee_suit.png")
    );


    private final AccessoryModel sunglassesModel = new AccessoryModel(
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "geo/wcat.sunglasses.geo.json"),
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "textures/entity/accessories/sunglasses.png")
    );


    private final AccessoryModel mossCoatHead = new AccessoryModel(
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "geo/wcat.moss_coat_head.geo.json"),
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "textures/entity/accessories/moss_coat.png")
    );
    private final AccessoryModel mossCoatUpperBody = new AccessoryModel(
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "geo/wcat.moss_coat_upper_body.geo.json"),
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "textures/entity/accessories/moss_coat.png")
    );
    private final AccessoryModel mossCoatMidBody = new AccessoryModel(
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "geo/wcat.moss_coat_mid_body.geo.json"),
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "textures/entity/accessories/moss_coat.png")
    );
    private final AccessoryModel mossCoatLowerBody = new AccessoryModel(
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "geo/wcat.moss_coat_lower_body.geo.json"),
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "textures/entity/accessories/moss_coat.png")
    );
    private final AccessoryModel mossCoatLeg = new AccessoryModel(
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "geo/wcat.moss_coat_leg.geo.json"),
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "textures/entity/accessories/moss_coat.png")
    );
    private final AccessoryModel mossCoatPaw = new AccessoryModel(
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "geo/wcat.moss_coat_paw.geo.json"),
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "textures/entity/accessories/moss_coat.png")
    );


    private final AccessoryModel dockBagModel = new AccessoryModel(
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "geo/wcat.dock_bag.geo.json"),
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "textures/entity/accessories/dock_bag.png")
    );


    private final AccessoryModel braceletModel = new AccessoryModel(
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "geo/wcat.bracelet.geo.json"),
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "textures/entity/accessories/collar_white.png")
    );

    private final AccessoryModel braceletExtraModel = new AccessoryModel(
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "geo/wcat.bracelet_extra.geo.json"),
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "textures/entity/accessories/collar_white.png")
    );


    private final ElytraModel elytraModel = new ElytraModel();


    private final AccessoryRenderer accessoryRenderer;

    private final LizardRenderer lizRenderer;
    private final WCRenderer catRenderer;


    public WCAccesoriesLayer(GeoRenderer<WCatEntity> entityRendererIn, EntityRendererProvider.Context context) {
        super(entityRendererIn);
        this.accessoryRenderer = new AccessoryRenderer(context, flowerCrownAccesoryModel);

        this.lizRenderer = new LizardRenderer(context);
        this.catRenderer = (WCRenderer) entityRendererIn;

    }

    @Override
    public void renderForBone(PoseStack poseStack, WCatEntity animatable, GeoBone bone, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {

        if (bone.getName().equals(Bones.LOWER_BODY_SUB.string()) && (animatable.hasCustomName() && animatable.getCustomName().getString().contains("squirrellover"))) {
            var bakedModel = squirrelAccessoryModel.getBakedModel(squirrelAccessoryModel.getModelResource(animatable));


            poseStack.pushPose();


            RenderType accessoryRenderType = RenderType.entityCutout(squirrelAccessoryModel.getTextureResource(animatable));

            VertexConsumer accessoryBuffer = bufferSource.getBuffer(accessoryRenderType);
            RenderUtils.translateToPivotPoint(poseStack, bone);
            RenderUtils.rotateMatrixAroundBone(poseStack, bone);


            poseStack.translate(0.0D, -0.185D, -0.3D);

            float scale = 0.15F;
            poseStack.scale(scale, scale, scale);
            poseStack.mulPose(Axis.XP.rotationDegrees(180f));


            float interpolatedYaw = Mth.lerp(partialTick, animatable.yBodyRotO, animatable.yBodyRot);


            poseStack.mulPose(Axis.YP.rotationDegrees(interpolatedYaw + 180f));
            getRenderer().reRender(
                    bakedModel,
                    poseStack,
                    bufferSource,
                    animatable,
                    accessoryRenderType,
                    accessoryBuffer,
                    partialTick,
                    packedLight,
                    packedOverlay,
                    1f, 1f, 1f, 1f
            );

            poseStack.popPose();
            buffer = bufferSource.getBuffer(renderType);

        }

        boolean hasCrown = animatable
                .getItemBySlot(EquipmentSlot.HEAD)
                .is(ModItems.FLOWER_CROWN.get());
        boolean hasMane = animatable
                .getItemBySlot(EquipmentSlot.HEAD)
                .is(ModItems.LEAF_MANE.get());
        boolean hasFlower = animatable
                .getItemBySlot(EquipmentSlot.HEAD)
                .is(ModItems.HEAD_FLOWER.get());
        boolean hasDandelion = animatable
                .getItemBySlot(EquipmentSlot.HEAD)
                .is(ModItems.HEAD_DANDELION.get());
        boolean hasLeaf = animatable
                .getItemBySlot(EquipmentSlot.HEAD)
                .is(ModItems.HEAD_LEAF.get());

        boolean hasTailVines = animatable
                .getItemBySlot(EquipmentSlot.LEGS)
                .is(ModItems.TAIL_VINES.get());
        boolean hasDrapedTailVines = animatable
                .getItemBySlot(EquipmentSlot.LEGS)
                .is(ModItems.DRAPED_TAIL_VINES.get());

        boolean hasHat = animatable
                .getItemBySlot(EquipmentSlot.HEAD)
                .is(ModItems.CAT_HAT.get());

        boolean hasChestBow = animatable
                .getItemBySlot(EquipmentSlot.CHEST)
                .is(ModItems.CAT_BLACK_BOW.get());

        boolean hasSkullMask = animatable
                .getItemBySlot(EquipmentSlot.HEAD)
                .is(ModItems.SKULL_MASK.get());

        boolean hasFlowerArmor = animatable
                .getItemBySlot(EquipmentSlot.CHEST)
                .is(ModItems.FLOWER_ARMOR.get());

        boolean hasTeethClaws = animatable
                .getItemBySlot(EquipmentSlot.FEET)
                .is(ModItems.TEETH_CLAWS.get());

        boolean hasBeeSuit = animatable
                .getItemBySlot(EquipmentSlot.CHEST)
                .is(ModItems.BEE_COSTUME.get());


        if (CompatibilitiesClient.hasCuriosItem(animatable.getPlayerBoundUuid(), ModItems.FLOWER_CROWN.get())) hasCrown = true;
        if (CompatibilitiesClient.hasCuriosItem(animatable.getPlayerBoundUuid(), ModItems.LEAF_MANE.get())) hasMane = true;
        if (CompatibilitiesClient.hasCuriosItem(animatable.getPlayerBoundUuid(), ModItems.HEAD_FLOWER.get())) hasFlower = true;
        if (CompatibilitiesClient.hasCuriosItem(animatable.getPlayerBoundUuid(), ModItems.HEAD_DANDELION.get())) hasDandelion = true;
        if (CompatibilitiesClient.hasCuriosItem(animatable.getPlayerBoundUuid(), ModItems.HEAD_LEAF.get())) hasLeaf = true;
        if (CompatibilitiesClient.hasCuriosItem(animatable.getPlayerBoundUuid(), ModItems.TAIL_VINES.get())) hasTailVines = true;
        if (CompatibilitiesClient.hasCuriosItem(animatable.getPlayerBoundUuid(), ModItems.DRAPED_TAIL_VINES.get())) hasDrapedTailVines = true;
        if (CompatibilitiesClient.hasCuriosItem(animatable.getPlayerBoundUuid(), ModItems.CAT_HAT.get())) hasHat = true;
        if (CompatibilitiesClient.hasCuriosItem(animatable.getPlayerBoundUuid(), ModItems.CAT_BLACK_BOW.get())) hasChestBow = true;
        if (CompatibilitiesClient.hasCuriosItem(animatable.getPlayerBoundUuid(), ModItems.SKULL_MASK.get())) hasSkullMask = true;
        if (CompatibilitiesClient.hasCuriosItem(animatable.getPlayerBoundUuid(), ModItems.FLOWER_ARMOR.get())) hasFlowerArmor = true;
        if (CompatibilitiesClient.hasCuriosItem(animatable.getPlayerBoundUuid(), ModItems.TEETH_CLAWS.get())) hasTeethClaws = true;
        if (CompatibilitiesClient.hasCuriosItem(animatable.getPlayerBoundUuid(), ModItems.BEE_COSTUME.get())) hasBeeSuit = true;

        boolean hasBerry = animatable
                .getItemBySlot(EquipmentSlot.HEAD)
                .is(ModItems.HEAD_SWEETBERRY.get()) || animatable
                .getItemBySlot(EquipmentSlot.HEAD)
                .is(ModItems.HEAD_GLOWBERRY.get());

        if (CompatibilitiesClient.hasCuriosItem(animatable.getPlayerBoundUuid(), ModItems.HEAD_GLOWBERRY.get())
        || CompatibilitiesClient.hasCuriosItem(animatable.getPlayerBoundUuid(), ModItems.HEAD_SWEETBERRY.get())) {
            hasBerry = true;
        }

        boolean hasBow = animatable
                .getItemBySlot(EquipmentSlot.HEAD)
                .is(ModItems.CAT_PINK_BOW.get()) || animatable
                .getItemBySlot(EquipmentSlot.HEAD)
                .is(ModItems.CAT_RED_BOW.get());

        boolean hasLegWrap = animatable.isWrappedPaw()
                || animatable.getItemBySlot(EquipmentSlot.FEET).is(ModItems.LEG_WRAP.get())
                || CompatibilitiesClient.hasCuriosItem(animatable.getPlayerBoundUuid(), ModItems.LEG_WRAP.get());

        if (CompatibilitiesClient.hasCuriosItem(animatable.getPlayerBoundUuid(), ModItems.CAT_PINK_BOW.get())
                || CompatibilitiesClient.hasCuriosItem(animatable.getPlayerBoundUuid(), ModItems.CAT_RED_BOW.get())) {
            hasBow = true;
        }

        if (bone.getName().equals(Bones.HEAD.string())) {


            if (hasCrown) {
                var bakedModel = flowerCrownAccesoryModel.getBakedModel(flowerCrownAccesoryModel.getModelResource(animatable));

                poseStack.pushPose();

                poseStack.translate(0.0D, 0.61D, -0.6D);
                poseStack.mulPose(Axis.XP.rotationDegrees(15f));
                float scale = 0.87f;
                poseStack.scale(scale, scale, scale);


                RenderType accessoryRenderType = RenderType.entityCutout(flowerCrownAccesoryModel.getTextureResource(animatable));

                VertexConsumer accessoryBuffer = bufferSource.getBuffer(accessoryRenderType);

                float interpolatedYaw = Mth.lerp(partialTick, animatable.yBodyRotO, animatable.yBodyRot);
                poseStack.mulPose(Axis.YP.rotationDegrees(interpolatedYaw + 180f));


                accessoryRenderer.reRender(
                        bakedModel,
                        poseStack,
                        bufferSource,
                        animatable,
                        accessoryRenderType,
                        accessoryBuffer,
                        partialTick,
                        packedLight,
                        packedOverlay,
                        1f, 1f, 1f, 1f
                );

                poseStack.popPose();
                buffer = bufferSource.getBuffer(renderType);
            }
            if (hasMane) {
                var bakedModel = leafManeAccesoryModel.getBakedModel(leafManeAccesoryModel.getModelResource(animatable));

                poseStack.pushPose();

                poseStack.translate(0.0D, 0.50D, -0.349D);
                poseStack.mulPose(Axis.XP.rotationDegrees(-82f));
                float scale = 0.95f;
                poseStack.scale(scale, scale, scale);


                RenderType accessoryRenderType = RenderType.entityCutout(leafManeAccesoryModel.getTextureResource(animatable));

                VertexConsumer accessoryBuffer = bufferSource.getBuffer(accessoryRenderType);

                float interpolatedYaw = Mth.lerp(partialTick, animatable.yBodyRotO, animatable.yBodyRot);
                poseStack.mulPose(Axis.YP.rotationDegrees(interpolatedYaw + 180f));


                accessoryRenderer.reRender(
                        bakedModel,
                        poseStack,
                        bufferSource,
                        animatable,
                        accessoryRenderType,
                        accessoryBuffer,
                        partialTick,
                        packedLight,
                        packedOverlay,
                        1f, 1f, 1f, 1f
                );

                poseStack.popPose();
                buffer = bufferSource.getBuffer(renderType);
            }
            if (hasDandelion) {
                var bakedModel = dandelionModel.getBakedModel(dandelionModel.getModelResource(animatable));

                poseStack.pushPose();

                poseStack.translate(0.098D, 0.64D, -0.51D);
                poseStack.mulPose(Axis.ZP.rotationDegrees(-92f));
                poseStack.mulPose(Axis.YP.rotationDegrees(25f));
                poseStack.mulPose(Axis.XP.rotationDegrees(5f));

                float scale = 0.8f;
                poseStack.scale(scale, scale, scale);


                RenderType accessoryRenderType = RenderType.entityCutout(dandelionModel.getTextureResource(animatable));

                VertexConsumer accessoryBuffer = bufferSource.getBuffer(accessoryRenderType);

                float interpolatedYaw = Mth.lerp(partialTick, animatable.yBodyRotO, animatable.yBodyRot);
                poseStack.mulPose(Axis.YP.rotationDegrees(interpolatedYaw + 180f));


                accessoryRenderer.reRender(
                        bakedModel,
                        poseStack,
                        bufferSource,
                        animatable,
                        accessoryRenderType,
                        accessoryBuffer,
                        partialTick,
                        packedLight,
                        packedOverlay,
                        1f, 1f, 1f, 1f
                );

                poseStack.popPose();
                buffer = bufferSource.getBuffer(renderType);
            }
            if (hasFlower) {
                var bakedModel = flowerAccesoryModel.getBakedModel(flowerAccesoryModel.getModelResource(animatable));

                poseStack.pushPose();

                poseStack.translate(-0.135D, 0.70D, -0.519D);

                float scale = 1.00f;
                poseStack.scale(scale, scale, scale);

                poseStack.mulPose(Axis.ZP.rotationDegrees(62f));

                RenderType accessoryRenderType = RenderType.entityCutout(flowerAccesoryModel.getTextureResource(animatable));

                VertexConsumer accessoryBuffer = bufferSource.getBuffer(accessoryRenderType);

                float interpolatedYaw = Mth.lerp(partialTick, animatable.yBodyRotO, animatable.yBodyRot);
                poseStack.mulPose(Axis.YP.rotationDegrees(interpolatedYaw + 180f));


                accessoryRenderer.reRender(
                        bakedModel,
                        poseStack,
                        bufferSource,
                        animatable,
                        accessoryRenderType,
                        accessoryBuffer,
                        partialTick,
                        packedLight,
                        packedOverlay,
                        1f, 1f, 1f, 1f
                );

                poseStack.popPose();
                buffer = bufferSource.getBuffer(renderType);

            }
            if (hasBerry) {
                ItemStack stack = animatable.getItemBySlot(EquipmentSlot.HEAD);
                ItemStack glowberry = CompatibilitiesClient.getCuriosItem(animatable.getPlayerBoundUuid(), ModItems.HEAD_GLOWBERRY.get());
                ItemStack sweetberry = CompatibilitiesClient.getCuriosItem(animatable.getPlayerBoundUuid(), ModItems.HEAD_SWEETBERRY.get());

                if (!glowberry.isEmpty()) {
                    stack = glowberry;
                } else if (!sweetberry.isEmpty()) {
                    stack = sweetberry;
                }

                int light = packedLight;

                if (stack.is(ModItems.HEAD_GLOWBERRY.get())) {
                    berryModel.texture = AccessoryModel.BERRY_TEXTURES[1];
                    light = 255;
                }
                else berryModel.texture = AccessoryModel.BERRY_TEXTURES[0];

                var bakedModel = berryModel.getBakedModel(berryModel.getModelResource(animatable));

                poseStack.pushPose();

                poseStack.translate(0.17D, 0.55D, -0.50D);

                float scale = 1.00f;
                poseStack.scale(scale, scale, scale);

                RenderType accessoryRenderType = RenderType.entityCutout(berryModel.getTextureResource(animatable));

                VertexConsumer accessoryBuffer = bufferSource.getBuffer(accessoryRenderType);

                float interpolatedYaw = Mth.lerp(partialTick, animatable.yBodyRotO, animatable.yBodyRot);
                poseStack.mulPose(Axis.YP.rotationDegrees(interpolatedYaw + 180f));

                accessoryRenderer.reRender(
                        bakedModel,
                        poseStack,
                        bufferSource,
                        animatable,
                        accessoryRenderType,
                        accessoryBuffer,
                        partialTick,
                        light,
                        packedOverlay,
                        1f, 1f, 1f, 1f
                );

                poseStack.popPose();
                buffer = bufferSource.getBuffer(renderType);
            }
            if (hasHat) {
                var bakedModel = catHatModel.getBakedModel(catHatModel.getModelResource(animatable));

                poseStack.pushPose();

                poseStack.translate(0.0D, 0.68D, -0.6D);
                float scale = 0.25f;
                poseStack.scale(scale, scale, scale);


                RenderType accessoryRenderType = RenderType.entityCutout(catHatModel.getTextureResource(animatable));

                VertexConsumer accessoryBuffer = bufferSource.getBuffer(accessoryRenderType);

                float interpolatedYaw = Mth.lerp(partialTick, animatable.yBodyRotO, animatable.yBodyRot);
                poseStack.mulPose(Axis.YP.rotationDegrees(interpolatedYaw + 180f));


                accessoryRenderer.reRender(
                        bakedModel,
                        poseStack,
                        bufferSource,
                        animatable,
                        accessoryRenderType,
                        accessoryBuffer,
                        partialTick,
                        packedLight,
                        packedOverlay,
                        1f, 1f, 1f, 1f
                );

                poseStack.popPose();
                buffer = bufferSource.getBuffer(renderType);
            }

            if (hasBow) {
                ItemStack stack = animatable.getItemBySlot(EquipmentSlot.HEAD);
                ItemStack redBow = CompatibilitiesClient.getCuriosItem(animatable.getPlayerBoundUuid(), ModItems.CAT_RED_BOW.get());
                ItemStack pinkBow = CompatibilitiesClient.getCuriosItem(animatable.getPlayerBoundUuid(), ModItems.CAT_PINK_BOW.get());

                if (!redBow.isEmpty()) {
                    stack = redBow;
                } else if (!pinkBow.isEmpty()) {
                    stack = pinkBow;
                }

                if (stack.is(ModItems.CAT_RED_BOW.get())) {
                    catBowModel.texture = AccessoryModel.HEAD_BOW_TEXTURES[1];
                }
                else catBowModel.texture = AccessoryModel.HEAD_BOW_TEXTURES[0];

                var bakedModel = catBowModel.getBakedModel(catBowModel.getModelResource(animatable));

                poseStack.pushPose();

//                poseStack.translate(0.20D, 0.55D, -0.50D);
//                poseStack.mulPose(Axis.YP.rotationDegrees(90f));
//                poseStack.mulPose(Axis.XP.rotationDegrees(-10f));

                poseStack.translate(0.045D, 0.59D, -0.65D);
                poseStack.mulPose(Axis.YP.rotationDegrees(205f));
                poseStack.mulPose(Axis.XP.rotationDegrees(-25f));

                float scale = 0.30f;
                poseStack.scale(scale, scale, scale);

                RenderType accessoryRenderType = RenderType.entityCutout(catBowModel.getTextureResource(animatable));

                VertexConsumer accessoryBuffer = bufferSource.getBuffer(accessoryRenderType);

                float interpolatedYaw = Mth.lerp(partialTick, animatable.yBodyRotO, animatable.yBodyRot);
                poseStack.mulPose(Axis.YP.rotationDegrees(interpolatedYaw + 180f));

                accessoryRenderer.reRender(
                        bakedModel,
                        poseStack,
                        bufferSource,
                        animatable,
                        accessoryRenderType,
                        accessoryBuffer,
                        partialTick,
                        packedLight,
                        packedOverlay,
                        1f, 1f, 1f, 1f
                );

                poseStack.popPose();
                buffer = bufferSource.getBuffer(renderType);
            }

            if (hasSkullMask) {
                var bakedModel = skullMaskModel.getBakedModel(skullMaskModel.getModelResource(animatable));

                poseStack.pushPose();

                poseStack.translate(0.0D, 0.427D, -0.57D);
                float scale = 1f;
                poseStack.scale(scale, scale, scale);


                RenderType accessoryRenderType = RenderType.entityCutout(skullMaskModel.getTextureResource(animatable));

                VertexConsumer accessoryBuffer = bufferSource.getBuffer(accessoryRenderType);

                float interpolatedYaw = Mth.lerp(partialTick, animatable.yBodyRotO, animatable.yBodyRot);
                poseStack.mulPose(Axis.YP.rotationDegrees(interpolatedYaw + 180f));


                accessoryRenderer.reRender(
                        bakedModel,
                        poseStack,
                        bufferSource,
                        animatable,
                        accessoryRenderType,
                        accessoryBuffer,
                        partialTick,
                        packedLight,
                        packedOverlay,
                        1f, 1f, 1f, 1f
                );

                poseStack.popPose();
                buffer = bufferSource.getBuffer(renderType);
            }

        }

        if (bone.getName().equals(Bones.TAIL_1.string())) {
            if (hasTailVines) {
                var bakedModel = tailVinesModel.getBakedModel(tailVinesModel.getModelResource(animatable));

                poseStack.pushPose();

                float scale = 0.25f;
                poseStack.scale(scale, scale, scale);

                poseStack.mulPose(Axis.XP.rotationDegrees(90f));
//                poseStack.mulPose(Axis.XP.rotationDegrees(15f));

                RenderUtils.translateMatrixToBone(poseStack, bone);

                poseStack.translate(0.00D, 1.82D, -2.00D);
                poseStack.mulPose(Axis.YP.rotationDegrees(90f));


                RenderType accessoryRenderType = RenderType.entityCutout(tailVinesModel.getTextureResource(animatable));

                VertexConsumer accessoryBuffer = bufferSource.getBuffer(accessoryRenderType);

                float interpolatedYaw = Mth.lerp(partialTick, animatable.yBodyRotO, animatable.yBodyRot);
                poseStack.mulPose(Axis.YP.rotationDegrees(interpolatedYaw + 180f));


                accessoryRenderer.reRender(
                        bakedModel,
                        poseStack,
                        bufferSource,
                        animatable,
                        accessoryRenderType,
                        accessoryBuffer,
                        partialTick,
                        packedLight,
                        packedOverlay,
                        1f, 1f, 1f, 1f
                );

                poseStack.popPose();

                buffer = bufferSource.getBuffer(renderType);
            }
        }

        if (bone.getName().equals(Bones.TAIL_2.string())) {
            if (hasTailVines && !WCGenetics.Bobtail.isBobtail(animatable.getGeneticsModule().getGenetics().bobtail)) {
                var bakedModel = tailVinesModel.getBakedModel(tailVinesModel.getModelResource(animatable));

                poseStack.pushPose();

                float scale = 0.25f;
                poseStack.scale(scale, scale, scale);

                poseStack.mulPose(Axis.XP.rotationDegrees(90f));

                RenderUtils.translateMatrixToBone(poseStack, bone);

                poseStack.translate(0.00D, 1.82D, -1.00D);
                poseStack.mulPose(Axis.YP.rotationDegrees(90f));


                RenderType accessoryRenderType = RenderType.entityCutout(tailVinesModel.getTextureResource(animatable));

                VertexConsumer accessoryBuffer = bufferSource.getBuffer(accessoryRenderType);

                float interpolatedYaw = Mth.lerp(partialTick, animatable.yBodyRotO, animatable.yBodyRot);
                poseStack.mulPose(Axis.YP.rotationDegrees(interpolatedYaw + 180f));


                accessoryRenderer.reRender(
                        bakedModel,
                        poseStack,
                        bufferSource,
                        animatable,
                        accessoryRenderType,
                        accessoryBuffer,
                        partialTick,
                        packedLight,
                        packedOverlay,
                        1f, 1f, 1f, 1f
                );

                poseStack.popPose();

                buffer = bufferSource.getBuffer(renderType);
            }
        }

        if (bone.getName().equals(Bones.TAIL_3.string())) {
            if (hasTailVines) {
                var bakedModel = tailVinesModel.getBakedModel(tailVinesModel.getModelResource(animatable));

                poseStack.pushPose();

                float scale = 0.25f;
                poseStack.scale(scale, scale, scale);

                poseStack.mulPose(Axis.XP.rotationDegrees(90f));

                RenderUtils.translateMatrixToBone(poseStack, bone);

                poseStack.translate(0.00D, 1.82D, 0.00D);
                poseStack.mulPose(Axis.YP.rotationDegrees(90f));


                RenderType accessoryRenderType = RenderType.entityCutout(tailVinesModel.getTextureResource(animatable));

                VertexConsumer accessoryBuffer = bufferSource.getBuffer(accessoryRenderType);

                float interpolatedYaw = Mth.lerp(partialTick, animatable.yBodyRotO, animatable.yBodyRot);
                poseStack.mulPose(Axis.YP.rotationDegrees(interpolatedYaw + 180f));


                accessoryRenderer.reRender(
                        bakedModel,
                        poseStack,
                        bufferSource,
                        animatable,
                        accessoryRenderType,
                        accessoryBuffer,
                        partialTick,
                        packedLight,
                        packedOverlay,
                        1f, 1f, 1f, 1f
                );

                poseStack.popPose();

                poseStack.pushPose();

                poseStack.scale(scale, scale, scale);

                poseStack.mulPose(Axis.XP.rotationDegrees(90f));
//                poseStack.mulPose(Axis.XP.rotationDegrees(15f));

                RenderUtils.translateMatrixToBone(poseStack, bone);

                poseStack.translate(0.00D, 1.82D, 1.00D);
                poseStack.mulPose(Axis.YP.rotationDegrees(90f));

                poseStack.mulPose(Axis.YP.rotationDegrees(interpolatedYaw + 180f));


                accessoryRenderer.reRender(
                        bakedModel,
                        poseStack,
                        bufferSource,
                        animatable,
                        accessoryRenderType,
                        accessoryBuffer,
                        partialTick,
                        packedLight,
                        packedOverlay,
                        1f, 1f, 1f, 1f
                );

                poseStack.popPose();

                buffer = bufferSource.getBuffer(renderType);
            }
        }


        ItemStack socksStack = ItemStack.EMPTY;
        ItemStack feetStack = animatable.getItemBySlot(EquipmentSlot.FEET);
        if (feetStack.getItem() instanceof CatSocksArmorItem) {
            socksStack = feetStack;
        }

        if (socksStack.isEmpty()) {
            ItemStack curiosStack = CompatibilitiesClient.getCuriosItem(
                    animatable.getPlayerBoundUuid(),
                    CatSocksArmorItem.class
            );

            if (!curiosStack.isEmpty()) {
                socksStack = curiosStack;
            }
        }

        if (socksStack.getItem() instanceof CatSocksArmorItem socks) {

            float red = 1.0f;
            float green = 1.0f;
            float blue = 1.0f;

            if (socks.equals(ModItems.BLACK_CAT_SOCKS.get())) {
                red = 0.2f;
                green = 0.2f;
                blue = 0.2f;
            } else if (socks.equals(ModItems.WHITE_CAT_SOCKS.get())) {
                red = 1f;
                green = 1f;
                blue = 1f;
            } else if (socks.equals(ModItems.BLUE_CAT_SOCKS.get())) {
                red = 0.6f;
                green = 0.6f;
                blue = 1f;
            } else if (socks.equals(ModItems.GREEN_CAT_SOCKS.get())) {
                red = 0.6f;
                green = 1.0f;
                blue = 0.6f;
            } else if (socks.equals(ModItems.PINK_CAT_SOCKS.get())) {
                red = 0.9f;
                green = 0.6f;
                blue = 1f;
            } else if (socks.equals(ModItems.ORANGE_CAT_SOCKS.get())) {
                red = 1.0f;
                green = 0.6f;
                blue = 0.4f;
            }

            if (bone.getName().equals(Bones.FRONT_LEFT_PAW.string())) {
                var bakedModel = socksModel.getBakedModel(socksModel.getModelResource(animatable));

                poseStack.pushPose();
                RenderUtils.translateMatrixToBone(poseStack, bone);

                float scale = 0.52f;
                poseStack.scale(scale, scale, scale);



                poseStack.translate(-0.13D, 0.00D, -0.48D);
                poseStack.mulPose(Axis.YP.rotationDegrees(90f));


                RenderType accessoryRenderType = RenderType.entityCutout(socksModel.getTextureResource(animatable));

                VertexConsumer accessoryBuffer = bufferSource.getBuffer(accessoryRenderType);

                float interpolatedYaw = Mth.lerp(partialTick, animatable.yBodyRotO, animatable.yBodyRot);
                poseStack.mulPose(Axis.YP.rotationDegrees(interpolatedYaw + 180f));


                accessoryRenderer.reRender(
                        bakedModel,
                        poseStack,
                        bufferSource,
                        animatable,
                        accessoryRenderType,
                        accessoryBuffer,
                        partialTick,
                        packedLight,
                        packedOverlay,
                        red, green, blue, 1f
                );

                buffer = bufferSource.getBuffer(renderType);
                poseStack.popPose();
            }
            if (bone.getName().equals(Bones.FRONT_RIGHT_PAW.string())) {
                var bakedModel = socksModel.getBakedModel(socksModel.getModelResource(animatable));

                poseStack.pushPose();
                RenderUtils.translateMatrixToBone(poseStack, bone);

                float scale = 0.52f;
                poseStack.scale(scale, scale, scale);



                poseStack.translate(0.13D, 0.00D, -0.48D);
                poseStack.mulPose(Axis.YP.rotationDegrees(90f));


                RenderType accessoryRenderType = RenderType.entityCutout(socksModel.getTextureResource(animatable));

                VertexConsumer accessoryBuffer = bufferSource.getBuffer(accessoryRenderType);

                float interpolatedYaw = Mth.lerp(partialTick, animatable.yBodyRotO, animatable.yBodyRot);
                poseStack.mulPose(Axis.YP.rotationDegrees(interpolatedYaw + 180f));


                accessoryRenderer.reRender(
                        bakedModel,
                        poseStack,
                        bufferSource,
                        animatable,
                        accessoryRenderType,
                        accessoryBuffer,
                        partialTick,
                        packedLight,
                        packedOverlay,
                        red, green, blue, 1f
                );

                buffer = bufferSource.getBuffer(renderType);
                poseStack.popPose();
            }
        }

        if (bone.getName().equals(Bones.HEAD.string())){
            if (hasLeaf) {
                var bakedModel = leafShieldModel.getBakedModel(leafShieldModel.getModelResource(animatable));

                poseStack.pushPose();

                poseStack.translate(0.0D, 0.67D, -0.65D);

                float scale = 0.28f;
                poseStack.scale(scale, scale, scale);


                RenderType accessoryRenderType = RenderType.entityCutout(leafShieldModel.getTextureResource(animatable));

                VertexConsumer accessoryBuffer = bufferSource.getBuffer(accessoryRenderType);

                float interpolatedYaw = Mth.lerp(partialTick, animatable.yBodyRotO, animatable.yBodyRot);
                poseStack.mulPose(Axis.YP.rotationDegrees(interpolatedYaw + 180f));


                accessoryRenderer.reRender(
                        bakedModel,
                        poseStack,
                        bufferSource,
                        animatable,
                        accessoryRenderType,
                        accessoryBuffer,
                        partialTick,
                        packedLight,
                        packedOverlay,
                        1f, 1f, 1f, 1f
                );

                poseStack.popPose();
                buffer = bufferSource.getBuffer(renderType);
            }
        }



        if (bone.getName().equals(Bones.TAIL_1.string())) {
            if (hasDrapedTailVines) {
                var bakedModel = tailLichenModel.getBakedModel(tailLichenModel.getModelResource(animatable));

                poseStack.pushPose();

                float scale = 0.51f;
                poseStack.scale(scale, scale, scale);

                poseStack.mulPose(Axis.XP.rotationDegrees(90f));
//                poseStack.mulPose(Axis.XP.rotationDegrees(15f));

                RenderUtils.translateMatrixToBone(poseStack, bone);

                poseStack.translate(0.00D, 0.67D, -0.85D);
//                poseStack.mulPose(Axis.YP.rotationDegrees(90f));


                RenderType accessoryRenderType = RenderType.entityCutout(tailLichenModel.getTextureResource(animatable));

                VertexConsumer accessoryBuffer = bufferSource.getBuffer(accessoryRenderType);

                float interpolatedYaw = Mth.lerp(partialTick, animatable.yBodyRotO, animatable.yBodyRot);
                poseStack.mulPose(Axis.YP.rotationDegrees(interpolatedYaw + 180f));


                accessoryRenderer.reRender(
                        bakedModel,
                        poseStack,
                        bufferSource,
                        animatable,
                        accessoryRenderType,
                        accessoryBuffer,
                        partialTick,
                        packedLight,
                        packedOverlay,
                        1f, 1f, 1f, 1f
                );

                poseStack.popPose();

                buffer = bufferSource.getBuffer(renderType);
            }
        }

        if (bone.getName().equals(Bones.TAIL_2.string())) {
            if (hasDrapedTailVines && !WCGenetics.Bobtail.isBobtail(animatable.getGeneticsModule().getGenetics().bobtail)) {
                var bakedModel = tailLichenModel.getBakedModel(tailLichenModel.getModelResource(animatable));

                poseStack.pushPose();

                float scale = 0.51f;

                poseStack.scale(scale, scale, scale);

                poseStack.mulPose(Axis.XP.rotationDegrees(90f));

                RenderUtils.translateMatrixToBone(poseStack, bone);

                poseStack.translate(0.00D, 0.67D, -0.50D);
                poseStack.mulPose(Axis.YP.rotationDegrees(180f));


                RenderType accessoryRenderType = RenderType.entityCutout(tailLichenModel.getTextureResource(animatable));

                VertexConsumer accessoryBuffer = bufferSource.getBuffer(accessoryRenderType);

                float interpolatedYaw = Mth.lerp(partialTick, animatable.yBodyRotO, animatable.yBodyRot);
                poseStack.mulPose(Axis.YP.rotationDegrees(interpolatedYaw + 180f));


                accessoryRenderer.reRender(
                        bakedModel,
                        poseStack,
                        bufferSource,
                        animatable,
                        accessoryRenderType,
                        accessoryBuffer,
                        partialTick,
                        packedLight,
                        packedOverlay,
                        1f, 1f, 1f, 1f
                );

                poseStack.popPose();

                buffer = bufferSource.getBuffer(renderType);
            }
        }

        if (bone.getName().equals(Bones.TAIL_3.string())) {
            if (hasDrapedTailVines) {
                var bakedModel = tailLichenModel.getBakedModel(tailLichenModel.getModelResource(animatable));

                poseStack.pushPose();

                float scale = 0.51f;

                poseStack.scale(scale, scale, scale);

                poseStack.mulPose(Axis.XP.rotationDegrees(90f));

                RenderUtils.translateMatrixToBone(poseStack, bone);

                poseStack.translate(0.00D, 0.67D, 0.10D);


                RenderType accessoryRenderType = RenderType.entityCutout(tailLichenModel.getTextureResource(animatable));

                VertexConsumer accessoryBuffer = bufferSource.getBuffer(accessoryRenderType);

                float interpolatedYaw = Mth.lerp(partialTick, animatable.yBodyRotO, animatable.yBodyRot);
                poseStack.mulPose(Axis.YP.rotationDegrees(interpolatedYaw + 180f));


                accessoryRenderer.reRender(
                        bakedModel,
                        poseStack,
                        bufferSource,
                        animatable,
                        accessoryRenderType,
                        accessoryBuffer,
                        partialTick,
                        packedLight,
                        packedOverlay,
                        1f, 1f, 1f, 1f
                );

                poseStack.popPose();



                buffer = bufferSource.getBuffer(renderType);
            }
        }


        ItemStack featherStack = ItemStack.EMPTY;
        ItemStack featherChestStack = animatable.getItemBySlot(EquipmentSlot.CHEST);
        if (featherChestStack.getItem() instanceof FeathersArmorItem) {
            featherStack = featherChestStack;
        }

        if (featherStack.isEmpty()) {
            ItemStack curiosStack = CompatibilitiesClient.getCuriosItem(
                    animatable.getPlayerBoundUuid(),
                    FeathersArmorItem.class
            );

            if (!curiosStack.isEmpty()) {
                featherStack = curiosStack;
            }
        }

        if (featherStack.getItem() instanceof FeathersArmorItem featherArmor) {
            if (featherArmor == ModItems.VULTURE_BODY_FEATHERS.get()) {

                if (bone.getName().equals(Bones.UPPER_BODY.string())) {
                    var bakedModel = bodyVultureFeathersModelUp.getBakedModel(bodyVultureFeathersModelUp.getModelResource(animatable));

                    poseStack.pushPose();

                    float scale = 1f;
                    poseStack.translate(0.00D, -0.01D, 0.00D);

                    poseStack.scale(scale, scale, scale);

                    poseStack.mulPose(Axis.XP.rotationDegrees(0f));

                    RenderUtils.translateMatrixToBone(poseStack, bone);

                    RenderType accessoryRenderType = RenderType.entityCutout(bodyVultureFeathersModelUp.getTextureResource(animatable));

                    VertexConsumer accessoryBuffer = bufferSource.getBuffer(accessoryRenderType);

                    float interpolatedYaw = Mth.lerp(partialTick, animatable.yBodyRotO, animatable.yBodyRot);
                    poseStack.mulPose(Axis.YP.rotationDegrees(interpolatedYaw + 180f));


                    accessoryRenderer.reRender(
                            bakedModel,
                            poseStack,
                            bufferSource,
                            animatable,
                            accessoryRenderType,
                            accessoryBuffer,
                            partialTick,
                            packedLight,
                            packedOverlay,
                            1f, 1f, 1f, 1f
                    );

                    poseStack.popPose();

                    buffer = bufferSource.getBuffer(renderType);
                }
                if (bone.getName().equals(Bones.UP.string())) {
                    var bakedModel = bodyVultureFeathersModelMid.getBakedModel(bodyVultureFeathersModelMid.getModelResource(animatable));

                    poseStack.pushPose();

                    float scale = 1f;
                    poseStack.translate(0.00D, -0.01D, 0.00D);

                    poseStack.scale(scale, scale, scale);

                    poseStack.mulPose(Axis.XP.rotationDegrees(0f));

                    RenderUtils.translateMatrixToBone(poseStack, bone);

                    RenderType accessoryRenderType = RenderType.entityCutout(bodyVultureFeathersModelMid.getTextureResource(animatable));

                    VertexConsumer accessoryBuffer = bufferSource.getBuffer(accessoryRenderType);

                    float interpolatedYaw = Mth.lerp(partialTick, animatable.yBodyRotO, animatable.yBodyRot);
                    poseStack.mulPose(Axis.YP.rotationDegrees(interpolatedYaw + 180f));


                    accessoryRenderer.reRender(
                            bakedModel,
                            poseStack,
                            bufferSource,
                            animatable,
                            accessoryRenderType,
                            accessoryBuffer,
                            partialTick,
                            packedLight,
                            packedOverlay,
                            1f, 1f, 1f, 1f
                    );

                    poseStack.popPose();

                    buffer = bufferSource.getBuffer(renderType);
                }

            }

            if (featherArmor != ModItems.VULTURE_BODY_FEATHERS.get()){
                bodyFeathersModelUp.texture = AccessoryModel.FEATHER_TEXTURES[5];
                bodyFeathersModelMid.texture = AccessoryModel.FEATHER_TEXTURES[5];
                bodyFeathersModelDown.texture = AccessoryModel.FEATHER_TEXTURES[5];

                if (featherArmor == ModItems.BLUE_PARROT_BODY_FEATHERS.get()) {
                    bodyFeathersModelUp.texture = AccessoryModel.FEATHER_TEXTURES[3];
                    bodyFeathersModelMid.texture = AccessoryModel.FEATHER_TEXTURES[3];
                    bodyFeathersModelDown.texture = AccessoryModel.FEATHER_TEXTURES[3];
                } else if (featherArmor == ModItems.LIGHTBLUE_PARROT_BODY_FEATHERS.get()) {
                    bodyFeathersModelUp.texture = AccessoryModel.FEATHER_TEXTURES[4];
                    bodyFeathersModelMid.texture = AccessoryModel.FEATHER_TEXTURES[4];
                    bodyFeathersModelDown.texture = AccessoryModel.FEATHER_TEXTURES[4];
                } else if (featherArmor == ModItems.GRAY_PARROT_BODY_FEATHERS.get()) {
                    bodyFeathersModelUp.texture = AccessoryModel.FEATHER_TEXTURES[1];
                    bodyFeathersModelMid.texture = AccessoryModel.FEATHER_TEXTURES[1];
                    bodyFeathersModelDown.texture = AccessoryModel.FEATHER_TEXTURES[1];
                } else if (featherArmor == ModItems.GREEN_PARROT_BODY_FEATHERS.get()) {
                    bodyFeathersModelUp.texture = AccessoryModel.FEATHER_TEXTURES[2];
                    bodyFeathersModelMid.texture = AccessoryModel.FEATHER_TEXTURES[2];
                    bodyFeathersModelDown.texture = AccessoryModel.FEATHER_TEXTURES[2];
                } else if (featherArmor == ModItems.RED_PARROT_BODY_FEATHERS.get()) {
                    bodyFeathersModelUp.texture = AccessoryModel.FEATHER_TEXTURES[0];
                    bodyFeathersModelMid.texture = AccessoryModel.FEATHER_TEXTURES[0];
                    bodyFeathersModelDown.texture = AccessoryModel.FEATHER_TEXTURES[0];
                } else if (featherArmor == ModItems.PIGEON_BODY_FEATHERS.get()) {
                    bodyFeathersModelUp.texture = AccessoryModel.FEATHER_TEXTURES[6];
                    bodyFeathersModelMid.texture = AccessoryModel.FEATHER_TEXTURES[6];
                    bodyFeathersModelDown.texture = AccessoryModel.FEATHER_TEXTURES[6];
                } else if (featherArmor == ModItems.CROW_BODY_FEATHERS.get()) {
                    bodyFeathersModelUp.texture = AccessoryModel.FEATHER_TEXTURES[7];
                    bodyFeathersModelMid.texture = AccessoryModel.FEATHER_TEXTURES[7];
                    bodyFeathersModelDown.texture = AccessoryModel.FEATHER_TEXTURES[7];
                } else if (featherArmor == ModItems.GOLDFINCH_BODY_FEATHERS.get()) {
                    bodyFeathersModelUp.texture = AccessoryModel.FEATHER_TEXTURES[8];
                    bodyFeathersModelMid.texture = AccessoryModel.FEATHER_TEXTURES[8];
                    bodyFeathersModelDown.texture = AccessoryModel.FEATHER_TEXTURES[8];
                } else if (featherArmor == ModItems.CARDINAL_BODY_FEATHERS.get()) {
                    bodyFeathersModelUp.texture = AccessoryModel.FEATHER_TEXTURES[9];
                    bodyFeathersModelMid.texture = AccessoryModel.FEATHER_TEXTURES[9];
                    bodyFeathersModelDown.texture = AccessoryModel.FEATHER_TEXTURES[9];
                }

                if (bone.getName().equals(Bones.UPPER_BODY.string())) {
                    var bakedModel = bodyFeathersModelUp.getBakedModel(bodyFeathersModelUp.getModelResource(animatable));

                    poseStack.pushPose();

                    float scale = 1f;
                    poseStack.translate(0.00D, -0.01D, 0.00D);

                    poseStack.scale(scale, scale, scale);

                    poseStack.mulPose(Axis.XP.rotationDegrees(0f));

                    RenderUtils.translateMatrixToBone(poseStack, bone);

                    RenderType accessoryRenderType = RenderType.entityCutout(bodyFeathersModelUp.getTextureResource(animatable));

                    VertexConsumer accessoryBuffer = bufferSource.getBuffer(accessoryRenderType);

                    float interpolatedYaw = Mth.lerp(partialTick, animatable.yBodyRotO, animatable.yBodyRot);
                    poseStack.mulPose(Axis.YP.rotationDegrees(interpolatedYaw + 180f));


                    accessoryRenderer.reRender(
                            bakedModel,
                            poseStack,
                            bufferSource,
                            animatable,
                            accessoryRenderType,
                            accessoryBuffer,
                            partialTick,
                            packedLight,
                            packedOverlay,
                            1f, 1f, 1f, 1f
                    );

                    poseStack.popPose();

                    buffer = bufferSource.getBuffer(renderType);
                }
                if (bone.getName().equals(Bones.UP.string())) {
                    var bakedModel = bodyFeathersModelMid.getBakedModel(bodyFeathersModelMid.getModelResource(animatable));

                    poseStack.pushPose();

                    float scale = 1f;
                    poseStack.translate(0.00D, -0.01D, 0.00D);

                    poseStack.scale(scale, scale, scale);

                    poseStack.mulPose(Axis.XP.rotationDegrees(0f));

                    RenderUtils.translateMatrixToBone(poseStack, bone);

                    RenderType accessoryRenderType = RenderType.entityCutout(bodyFeathersModelMid.getTextureResource(animatable));

                    VertexConsumer accessoryBuffer = bufferSource.getBuffer(accessoryRenderType);

                    float interpolatedYaw = Mth.lerp(partialTick, animatable.yBodyRotO, animatable.yBodyRot);
                    poseStack.mulPose(Axis.YP.rotationDegrees(interpolatedYaw + 180f));


                    accessoryRenderer.reRender(
                            bakedModel,
                            poseStack,
                            bufferSource,
                            animatable,
                            accessoryRenderType,
                            accessoryBuffer,
                            partialTick,
                            packedLight,
                            packedOverlay,
                            1f, 1f, 1f, 1f
                    );

                    poseStack.popPose();

                    buffer = bufferSource.getBuffer(renderType);
                }
                if (bone.getName().equals(Bones.LOWER_BODY_SUB.string())) {
                    var bakedModel = bodyFeathersModelDown.getBakedModel(bodyFeathersModelDown.getModelResource(animatable));

                    poseStack.pushPose();

                    float scale = 1f;
                    poseStack.translate(0.00D, +0.50D, -0.32D);

                    poseStack.scale(scale, scale, scale);

                    poseStack.mulPose(Axis.XP.rotationDegrees(90f));

                    RenderUtils.translateMatrixToBone(poseStack, bone);

                    RenderType accessoryRenderType = RenderType.entityCutout(bodyFeathersModelDown.getTextureResource(animatable));

                    VertexConsumer accessoryBuffer = bufferSource.getBuffer(accessoryRenderType);

                    float interpolatedYaw = Mth.lerp(partialTick, animatable.yBodyRotO, animatable.yBodyRot);
                    poseStack.mulPose(Axis.YP.rotationDegrees(interpolatedYaw + 180f));


                    accessoryRenderer.reRender(
                            bakedModel,
                            poseStack,
                            bufferSource,
                            animatable,
                            accessoryRenderType,
                            accessoryBuffer,
                            partialTick,
                            packedLight,
                            packedOverlay,
                            1f, 1f, 1f, 1f
                    );

                    poseStack.popPose();

                    buffer = bufferSource.getBuffer(renderType);
                }
            }

        }

        if (bone.getName().equals(Bones.UPPER_BODY.string())) {

            boolean hasCollar = false;
            boolean hasSpikes;
            boolean hasBell;

            int light = packedLight;

            boolean spaceKitty = false;


            ItemStack collarStack = ItemStack.EMPTY;
            ItemStack bodyCollarStack = animatable.getItemBySlot(EquipmentSlot.CHEST);
            if (bodyCollarStack.getItem() instanceof CollarArmorItem) {
                collarStack = bodyCollarStack;
            }

            if (collarStack.isEmpty()) {
                ItemStack curiosStack = CompatibilitiesClient.getCuriosItem(
                        animatable.getPlayerBoundUuid(), CollarArmorItem.class
                );

                if (!curiosStack.isEmpty()) {
                    collarStack = curiosStack;
                }
            }

            if (collarStack.getItem() instanceof CollarArmorItem collar) {

                hasCollar = true;

                if (collar.hasSpikes(collarStack)) {
                    hasSpikes = true;
                } else {
                    hasSpikes = false;
                }

                if (collar.hasBell(collarStack)) {
                    hasBell = true;
                } else {
                    hasBell = false;
                }

                if (collar.hasGlow(collarStack)) {
                    light = 255;
                }

                if (collarStack.hasCustomHoverName()) {
                    if (collarStack.getHoverName().getString().toLowerCase(Locale.ROOT).equals("space kitty")) {
                        spaceKitty = true;
                    }
                }

            } else {
                hasSpikes = false;
                hasBell = false;
            }

            if (hasCollar) {

                var bakedModel = collarModel.getBakedModel(collarModel.getModelResource(animatable));

                collarModel.getBone("bell").ifPresent(bell -> { bell.setHidden(true);});
                collarModel.getBone("spikes").ifPresent(spikes -> { spikes.setHidden(true);});

                var bakedModel2 = collarExtraModel.getBakedModel(collarExtraModel.getModelResource(animatable));
                collarExtraModel.getBone("bell").ifPresent(bell -> { bell.setHidden(!hasBell);});
                collarExtraModel.getBone("spikes").ifPresent(spikes -> { spikes.setHidden(!hasSpikes);});

                poseStack.pushPose();

                poseStack.translate(0.0D, 0.30D, -0.44D);
                poseStack.mulPose(Axis.ZP.rotationDegrees(0f));
                poseStack.mulPose(Axis.YP.rotationDegrees(0f));
                poseStack.mulPose(Axis.XP.rotationDegrees(0f));

                float scale = 1f;
                poseStack.scale(scale, scale, scale);

                RenderType accessoryRenderType = RenderType.entityCutoutNoCull(collarModel.getTextureResource(animatable));

                if (spaceKitty) accessoryRenderType = RenderType.endGateway();

                VertexConsumer accessoryBuffer = bufferSource.getBuffer(accessoryRenderType);

                float interpolatedYaw = Mth.lerp(partialTick, animatable.yBodyRotO, animatable.yBodyRot);
                poseStack.mulPose(Axis.YP.rotationDegrees(interpolatedYaw + 180f));

                float r = 1f;
                float g = 1f;
                float b = 1f;
                if (collarStack.getItem() instanceof DyeableLeatherItem dye) {
                    int i = dye.getColor(collarStack);
                    r = (float)(i >> 16 & 255) / 255.0F;
                    g = (float)(i >> 8 & 255) / 255.0F;
                    b = (float)(i & 255) / 255.0F;
                }

                accessoryRenderer.reRender(
                        bakedModel,
                        poseStack, bufferSource, animatable, accessoryRenderType, accessoryBuffer,
                        partialTick, light, packedOverlay,
                        r, g, b, 1f
                );

                RenderType accessoryRenderType2 = RenderType.entityCutoutNoCull(collarExtraModel.getTextureResource(animatable));
                VertexConsumer accessoryBuffer2 = bufferSource.getBuffer(accessoryRenderType2);

                accessoryRenderer.reRender(
                        bakedModel2,
                        poseStack, bufferSource, animatable, accessoryRenderType2, accessoryBuffer2,
                        partialTick, light, packedOverlay,
                        1f, 1f, 1f, 1f
                );

                poseStack.popPose();
                buffer = bufferSource.getBuffer(renderType);
            }

            if (hasChestBow) {
                catBowModel.texture = AccessoryModel.HEAD_BOW_TEXTURES[2];

                var bakedModel = catBowModel.getBakedModel(catBowModel.getModelResource(animatable));

                poseStack.pushPose();

                poseStack.translate(0.0D, 0.14D, -0.480D);
                poseStack.mulPose(Axis.YP.rotationDegrees(180f));
                float scale = 0.45f;
                poseStack.scale(scale, scale, scale);

                RenderType accessoryRenderType = RenderType.entityCutout(catBowModel.getTextureResource(animatable));

                VertexConsumer accessoryBuffer = bufferSource.getBuffer(accessoryRenderType);

                float interpolatedYaw = Mth.lerp(partialTick, animatable.yBodyRotO, animatable.yBodyRot);
                poseStack.mulPose(Axis.YP.rotationDegrees(interpolatedYaw + 180f));


                accessoryRenderer.reRender(
                        bakedModel,
                        poseStack,
                        bufferSource,
                        animatable,
                        accessoryRenderType,
                        accessoryBuffer,
                        partialTick,
                        packedLight,
                        packedOverlay,
                        1f, 1f, 1f, 1f
                );

                poseStack.popPose();
                buffer = bufferSource.getBuffer(renderType);
            }
        }

        if (bone.getName().equals(Bones.UP.string())) {

            ItemStack elytraStack = ItemStack.EMPTY;
            ItemStack bodyElytraStack = animatable.getItemBySlot(EquipmentSlot.CHEST);
            if (bodyElytraStack.getItem() instanceof ElytraItem) {
                elytraStack = bodyElytraStack;
            }

            if (elytraStack.isEmpty()) {
                ItemStack curiosStack = CompatibilitiesClient.getCuriosItem(
                        animatable.getPlayerBoundUuid(), ElytraItem.class
                );

                if (!curiosStack.isEmpty()) {
                    elytraStack = curiosStack;
                }
            }

            if (elytraStack.getItem() instanceof ElytraItem) {
                var bakedModel = elytraModel.getBakedModel(elytraModel.getModelResource(animatable));

                if (Minecraft.getInstance().level != null) {
                    Player player = Minecraft.getInstance().level.getPlayerByUUID(animatable.getPlayerBoundUuid());
                    elytraModel.setTexture(resolveElytraTexture(player, elytraStack));
                    var processor = elytraModel.getAnimationProcessor();
                    var leftWing = processor.getBone("left_wing");
                    var rightWing = processor.getBone("right_wing");
                    if (leftWing != null && rightWing != null) {
                        if (player != null) {
                            boolean flying = player.isFallFlying();

                            if (flying) {
                                float angle = (float) ((60  + 60*-Mth.abs(player.getXRot()/90))* animatable.getDeltaMovement().length()/1.5f);
                                leftWing.setRotZ((float)Math.toRadians(194 + angle));
                                rightWing.setRotZ(-(float)Math.toRadians(194 + angle));

                            } else {
                                leftWing.setRotZ((float)Math.toRadians(194));
                                rightWing.setRotZ(-(float)Math.toRadians(194));
                            }
                        }
                    }
                }



                poseStack.pushPose();

                float scale = 0.8f;

                poseStack.translate(0.00D, 0.43D, -0.0D);

                poseStack.scale(scale, scale, scale);

                poseStack.mulPose(Axis.XP.rotationDegrees(-5f));
                poseStack.mulPose(Axis.YP.rotationDegrees(180f));

                RenderUtils.translateMatrixToBone(poseStack, bone);

                RenderType accessoryRenderType = RenderType.entityCutoutNoCull(elytraModel.getTextureResource(animatable));

                if (WarriorCatsEvents.Collaborators.isContributor(animatable.getPlayerBoundUuid())) {
                    if (elytraStack.hasCustomHoverName() && elytraStack.getHoverName().getString().equalsIgnoreCase("space kitty")) {
                        accessoryRenderType = RenderType.endGateway();
                    }
                }

                VertexConsumer accessoryBuffer = bufferSource.getBuffer(accessoryRenderType);

                float interpolatedYaw = Mth.lerp(partialTick, animatable.yBodyRotO, animatable.yBodyRot);
                poseStack.mulPose(Axis.YP.rotationDegrees(interpolatedYaw + 180f));

                accessoryRenderer.reRender(
                        bakedModel,
                        poseStack,
                        bufferSource,
                        animatable,
                        accessoryRenderType,
                        accessoryBuffer,
                        partialTick,
                        packedLight,
                        packedOverlay,
                        1f, 1f, 1f, 1f
                );

                poseStack.popPose();

                buffer = bufferSource.getBuffer(renderType);
            }
        }

        if (hasLegWrap) {
            if (bone.getName().equals(Bones.FRONT_RIGHT_PAW.string())) {
                var bakedModel = pawWrapModel0.getBakedModel(pawWrapModel0.getModelResource(animatable));

                poseStack.pushPose();

                poseStack.translate(0.07D, 0.0D, -0.249D);
                poseStack.mulPose(Axis.YP.rotationDegrees(0f));
                float scale = 0.5f;
                poseStack.scale(scale, scale, scale);

                RenderType accessoryRenderType = RenderType.entityCutout(pawWrapModel0.getTextureResource(animatable));

                VertexConsumer accessoryBuffer = bufferSource.getBuffer(accessoryRenderType);

                float interpolatedYaw = Mth.lerp(partialTick, animatable.yBodyRotO, animatable.yBodyRot);
                poseStack.mulPose(Axis.YP.rotationDegrees(interpolatedYaw + 180f));


                accessoryRenderer.reRender(
                        bakedModel,
                        poseStack,
                        bufferSource,
                        animatable,
                        accessoryRenderType,
                        accessoryBuffer,
                        partialTick,
                        packedLight,
                        packedOverlay,
                        1f, 1f, 1f, 1f
                );

                poseStack.popPose();
                buffer = bufferSource.getBuffer(renderType);
            }

            if (bone.getName().equals(Bones.FRONT_RIGHT_LEG.string())) {
                var bakedModel = pawWrapModel1.getBakedModel(pawWrapModel1.getModelResource(animatable));

                poseStack.pushPose();

                poseStack.translate(0.07D, 0.0D, -0.249D);
                poseStack.mulPose(Axis.YP.rotationDegrees(0f));
                float scale = 0.5f;
                poseStack.scale(scale, scale, scale);

                RenderType accessoryRenderType = RenderType.entityCutout(pawWrapModel1.getTextureResource(animatable));

                VertexConsumer accessoryBuffer = bufferSource.getBuffer(accessoryRenderType);

                float interpolatedYaw = Mth.lerp(partialTick, animatable.yBodyRotO, animatable.yBodyRot);
                poseStack.mulPose(Axis.YP.rotationDegrees(interpolatedYaw + 180f));

                accessoryRenderer.reRender(
                        bakedModel,
                        poseStack,
                        bufferSource,
                        animatable,
                        accessoryRenderType,
                        accessoryBuffer,
                        partialTick,
                        packedLight,
                        packedOverlay,
                        1f, 1f, 1f, 1f
                );

                poseStack.popPose();
                buffer = bufferSource.getBuffer(renderType);
            }
        }

        ItemStack butterflyWing = ItemStack.EMPTY;
        ItemStack butterflyArmorWing = animatable.getItemBySlot(EquipmentSlot.HEAD);
        if (butterflyArmorWing.getItem() instanceof ButterflyWingArmorItem) {
            butterflyWing = butterflyArmorWing;
        }

        if (butterflyWing.isEmpty()) {
            ItemStack curiosStack = CompatibilitiesClient.getCuriosItem(
                    animatable.getPlayerBoundUuid(),
                    ButterflyWingArmorItem.class
            );

            if (!curiosStack.isEmpty()) {
                butterflyWing = curiosStack;
            }
        }

        if (!butterflyWing.isEmpty()) {
            if (butterflyWing.is(ModItems.BLUE_MORPHO_WING.get())) {
                butterflyWingModel.texture = AccessoryModel.BUTTERFLYWING_TEXTURES[0];
            } else if (butterflyWing.is(ModItems.GOLIATH_BIRDWING_WING.get())) {
                butterflyWingModel.texture = AccessoryModel.BUTTERFLYWING_TEXTURES[1];
            } else if (butterflyWing.is(ModItems.MONARCH_WING.get())) {
                butterflyWingModel.texture = AccessoryModel.BUTTERFLYWING_TEXTURES[2];
            } else {
                butterflyWingModel.texture = AccessoryModel.BUTTERFLYWING_TEXTURES[3];
            }

            if (bone.getName().equals(Bones.HEAD.string())) {
                var bakedModel = butterflyWingModel.getBakedModel(butterflyWingModel.getModelResource(animatable));

                poseStack.pushPose();

                poseStack.translate(-0.159D, 0.5D, -0.6D);
                poseStack.mulPose(Axis.YP.rotationDegrees(-20f));
                float scale = 0.8f;
                poseStack.scale(scale, scale, scale);


                RenderType accessoryRenderType = RenderType.entityCutout(butterflyWingModel.getTextureResource(animatable));

                VertexConsumer accessoryBuffer = bufferSource.getBuffer(accessoryRenderType);

                float interpolatedYaw = Mth.lerp(partialTick, animatable.yBodyRotO, animatable.yBodyRot);
                poseStack.mulPose(Axis.YP.rotationDegrees(interpolatedYaw + 180f));


                accessoryRenderer.reRender(
                        bakedModel,
                        poseStack,
                        bufferSource,
                        animatable,
                        accessoryRenderType,
                        accessoryBuffer,
                        partialTick,
                        packedLight,
                        packedOverlay,
                        1f, 1f, 1f, 1f
                );

                poseStack.popPose();
                buffer = bufferSource.getBuffer(renderType);
            }
        }

        if (hasFlowerArmor) {
            if (bone.getName().equals(Bones.UPPER_BODY.string())) {
                var bakedModel = flowerArmorModelFront.getBakedModel(flowerArmorModelFront.getModelResource(animatable));

                poseStack.pushPose();
                poseStack.translate(0, -0.01D, -0);

                RenderType accessoryRenderType = RenderType.entityCutoutNoCull(flowerArmorModelFront.getTextureResource(animatable));

                VertexConsumer accessoryBuffer = bufferSource.getBuffer(accessoryRenderType);

                float interpolatedYaw = Mth.lerp(partialTick, animatable.yBodyRotO, animatable.yBodyRot);
                poseStack.mulPose(Axis.YP.rotationDegrees(interpolatedYaw + 180f));

                accessoryRenderer.reRender(
                        bakedModel,
                        poseStack,
                        bufferSource,
                        animatable,
                        accessoryRenderType,
                        accessoryBuffer,
                        partialTick,
                        packedLight,
                        packedOverlay,
                        1f, 1f, 1f, 1f
                );

                poseStack.popPose();
                buffer = bufferSource.getBuffer(renderType);
            }
            if (bone.getName().equals(Bones.UP.string())) {
                var bakedModel = flowerArmorModelMid.getBakedModel(flowerArmorModelMid.getModelResource(animatable));

                poseStack.pushPose();
                poseStack.translate(0, -0.01D, -0);

                RenderType accessoryRenderType = RenderType.entityCutoutNoCull(flowerArmorModelMid.getTextureResource(animatable));

                VertexConsumer accessoryBuffer = bufferSource.getBuffer(accessoryRenderType);

                float interpolatedYaw = Mth.lerp(partialTick, animatable.yBodyRotO, animatable.yBodyRot);
                poseStack.mulPose(Axis.YP.rotationDegrees(interpolatedYaw + 180f));

                accessoryRenderer.reRender(
                        bakedModel,
                        poseStack,
                        bufferSource,
                        animatable,
                        accessoryRenderType,
                        accessoryBuffer,
                        partialTick,
                        packedLight,
                        packedOverlay,
                        1f, 1f, 1f, 1f
                );

                poseStack.popPose();
                buffer = bufferSource.getBuffer(renderType);
            }
            if (bone.getName().equals(Bones.LOWER_BODY.string())) {
                var bakedModel = flowerArmorModelBack.getBakedModel(flowerArmorModelBack.getModelResource(animatable));

                poseStack.pushPose();
                poseStack.translate(0, -0.01D, -0);


                RenderType accessoryRenderType = RenderType.entityCutoutNoCull(flowerArmorModelBack.getTextureResource(animatable));

                VertexConsumer accessoryBuffer = bufferSource.getBuffer(accessoryRenderType);

                float interpolatedYaw = Mth.lerp(partialTick, animatable.yBodyRotO, animatable.yBodyRot);
                poseStack.mulPose(Axis.YP.rotationDegrees(interpolatedYaw + 180f));

                accessoryRenderer.reRender(
                        bakedModel,
                        poseStack,
                        bufferSource,
                        animatable,
                        accessoryRenderType,
                        accessoryBuffer,
                        partialTick,
                        packedLight,
                        packedOverlay,
                        1f, 1f, 1f, 1f
                );

                poseStack.popPose();
                buffer = bufferSource.getBuffer(renderType);
            }

        }

        if (hasTeethClaws) {
            ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
            ItemStack stack = new ItemStack(ModItems.ANIMAL_TOOTH.get());
            float itemScale = 0.08f;

            if (bone.getName().equals(Bones.FRONT_RIGHT_PAW.string())) {
                poseStack.pushPose();

                poseStack.translate(0.030, -0.01D, -0.31);
                poseStack.mulPose(Axis.YP.rotationDegrees(90f));
                poseStack.mulPose(Axis.YP.rotationDegrees(180f));
                poseStack.mulPose(Axis.ZP.rotationDegrees(-80));

                poseStack.scale(itemScale, itemScale, itemScale*3);

                renderClaws(itemRenderer, bufferSource, poseStack, stack, packedLight);

                poseStack.popPose();

                buffer = bufferSource.getBuffer(renderType);
            }

            if (bone.getName().equals(Bones.FRONT_LEFT_PAW.string())) {
                poseStack.pushPose();

                poseStack.translate(-0.105, -0.01D, -0.31);
                poseStack.mulPose(Axis.YP.rotationDegrees(90f));
                poseStack.mulPose(Axis.YP.rotationDegrees(180f));
                poseStack.mulPose(Axis.ZP.rotationDegrees(-80));

                poseStack.scale(itemScale, itemScale, itemScale*3);

                renderClaws(itemRenderer, bufferSource, poseStack, stack, packedLight);

                poseStack.popPose();

                buffer = bufferSource.getBuffer(renderType);
            }

            if (bone.getName().equals(Bones.BACK_RIGHT_LEG.string())) {
                poseStack.pushPose();

                poseStack.translate(0.030, -0.01D, 0.38);
                poseStack.mulPose(Axis.YP.rotationDegrees(90f));
                poseStack.mulPose(Axis.YP.rotationDegrees(180f));
                poseStack.mulPose(Axis.ZP.rotationDegrees(-80));

                poseStack.scale(itemScale, itemScale, itemScale*3);

                renderClaws(itemRenderer, bufferSource, poseStack, stack, packedLight);

                poseStack.popPose();

                buffer = bufferSource.getBuffer(renderType);
            }

            if (bone.getName().equals(Bones.BACK_LEFT_LEG.string())) {
                poseStack.pushPose();

                poseStack.translate(-0.105, -0.01D, 0.38);
                poseStack.mulPose(Axis.YP.rotationDegrees(90f));
                poseStack.mulPose(Axis.YP.rotationDegrees(180f));
                poseStack.mulPose(Axis.ZP.rotationDegrees(-80));

                poseStack.scale(itemScale, itemScale, itemScale*3);

                renderClaws(itemRenderer, bufferSource, poseStack, stack, packedLight);

                poseStack.popPose();

                buffer = bufferSource.getBuffer(renderType);
            }

        }

        if (hasBeeSuit) {
            if (bone.getName().equals(Bones.HEAD.string())) {
                var bakedModel = beeSuitHead.getBakedModel(beeSuitHead.getModelResource(animatable));

                poseStack.pushPose();
                poseStack.translate(0, -0.01D, -0);


                RenderType accessoryRenderType = RenderType.entityCutoutNoCull(beeSuitHead.getTextureResource(animatable));

                VertexConsumer accessoryBuffer = bufferSource.getBuffer(accessoryRenderType);

                float interpolatedYaw = Mth.lerp(partialTick, animatable.yBodyRotO, animatable.yBodyRot);
                poseStack.mulPose(Axis.YP.rotationDegrees(interpolatedYaw + 180f));

                accessoryRenderer.reRender(
                        bakedModel,
                        poseStack,
                        bufferSource,
                        animatable,
                        accessoryRenderType,
                        accessoryBuffer,
                        partialTick,
                        packedLight,
                        packedOverlay,
                        1f, 1f, 1f, 1f
                );

                poseStack.popPose();
                buffer = bufferSource.getBuffer(renderType);
            }

            if (bone.getName().equals(Bones.UPPER_BODY.string())) {
                var bakedModel = beeSuitBody.getBakedModel(beeSuitBody.getModelResource(animatable));

                poseStack.pushPose();
                poseStack.translate(0, -0.01D, -0);

                RenderType accessoryRenderType = RenderType.entityCutoutNoCull(beeSuitBody.getTextureResource(animatable));

                VertexConsumer accessoryBuffer = bufferSource.getBuffer(accessoryRenderType);

                float interpolatedYaw = Mth.lerp(partialTick, animatable.yBodyRotO, animatable.yBodyRot);
                poseStack.mulPose(Axis.YP.rotationDegrees(interpolatedYaw + 180f));

                accessoryRenderer.reRender(
                        bakedModel,
                        poseStack,
                        bufferSource,
                        animatable,
                        accessoryRenderType,
                        accessoryBuffer,
                        partialTick,
                        packedLight,
                        packedOverlay,
                        1f, 1f, 1f, 1f
                );

                poseStack.popPose();

                var bakedModel2 = beeSuitBodyWings.getBakedModel(beeSuitBodyWings.getModelResource(animatable));

                poseStack.pushPose();
                poseStack.translate(0, -0.01D, -0);

                RenderType accessoryRenderType2 = RenderType.entityTranslucent(beeSuitBodyWings.getTextureResource(animatable));

                VertexConsumer accessoryBuffer2 = bufferSource.getBuffer(accessoryRenderType2);

                poseStack.mulPose(Axis.YP.rotationDegrees(interpolatedYaw + 180f));

                accessoryRenderer.reRender(
                        bakedModel2,
                        poseStack,
                        bufferSource,
                        animatable,
                        accessoryRenderType2,
                        accessoryBuffer2,
                        partialTick,
                        packedLight,
                        packedOverlay,
                        1f, 1f, 1f, 1f
                );

                poseStack.popPose();

                buffer = bufferSource.getBuffer(renderType);
            }

            if (bone.getName().equals(Bones.FRONT_RIGHT_LEG.string())) {
                var bakedModel = beeSuitUpperPaw.getBakedModel(beeSuitUpperPaw.getModelResource(animatable));

                poseStack.pushPose();
                poseStack.translate(0, -0.01D, -0);


                RenderType accessoryRenderType = RenderType.entityCutoutNoCull(beeSuitUpperPaw.getTextureResource(animatable));

                VertexConsumer accessoryBuffer = bufferSource.getBuffer(accessoryRenderType);

                float interpolatedYaw = Mth.lerp(partialTick, animatable.yBodyRotO, animatable.yBodyRot);
                poseStack.mulPose(Axis.YP.rotationDegrees(interpolatedYaw + 180f));

                accessoryRenderer.reRender(
                        bakedModel,
                        poseStack,
                        bufferSource,
                        animatable,
                        accessoryRenderType,
                        accessoryBuffer,
                        partialTick,
                        packedLight,
                        packedOverlay,
                        1f, 1f, 1f, 1f
                );

                poseStack.popPose();
                buffer = bufferSource.getBuffer(renderType);
            }
            if (bone.getName().equals(Bones.FRONT_LEFT_LEG.string())) {
                var bakedModel = beeSuitUpperPaw.getBakedModel(beeSuitUpperPaw.getModelResource(animatable));

                poseStack.pushPose();
                poseStack.translate(-0.135, -0.01D, -0);


                RenderType accessoryRenderType = RenderType.entityCutoutNoCull(beeSuitUpperPaw.getTextureResource(animatable));

                VertexConsumer accessoryBuffer = bufferSource.getBuffer(accessoryRenderType);

                float interpolatedYaw = Mth.lerp(partialTick, animatable.yBodyRotO, animatable.yBodyRot);
                poseStack.mulPose(Axis.YP.rotationDegrees(interpolatedYaw + 180f));

                accessoryRenderer.reRender(
                        bakedModel,
                        poseStack,
                        bufferSource,
                        animatable,
                        accessoryRenderType,
                        accessoryBuffer,
                        partialTick,
                        packedLight,
                        packedOverlay,
                        1f, 1f, 1f, 1f
                );

                poseStack.popPose();
                buffer = bufferSource.getBuffer(renderType);
            }

            if (bone.getName().equals(Bones.FRONT_RIGHT_PAW.string())) {
                var bakedModel = beeSuitlowerPaw.getBakedModel(beeSuitlowerPaw.getModelResource(animatable));

                poseStack.pushPose();
                poseStack.translate(0, -0.01D, -0);


                RenderType accessoryRenderType = RenderType.entityCutoutNoCull(beeSuitlowerPaw.getTextureResource(animatable));

                VertexConsumer accessoryBuffer = bufferSource.getBuffer(accessoryRenderType);

                float interpolatedYaw = Mth.lerp(partialTick, animatable.yBodyRotO, animatable.yBodyRot);
                poseStack.mulPose(Axis.YP.rotationDegrees(interpolatedYaw + 180f));

                accessoryRenderer.reRender(
                        bakedModel,
                        poseStack,
                        bufferSource,
                        animatable,
                        accessoryRenderType,
                        accessoryBuffer,
                        partialTick,
                        packedLight,
                        packedOverlay,
                        1f, 1f, 1f, 1f
                );

                poseStack.popPose();
                buffer = bufferSource.getBuffer(renderType);
            }
            if (bone.getName().equals(Bones.FRONT_LEFT_PAW.string())) {
                var bakedModel = beeSuitlowerPaw.getBakedModel(beeSuitlowerPaw.getModelResource(animatable));

                poseStack.pushPose();
                poseStack.translate(-0.135, -0.01D, -0);


                RenderType accessoryRenderType = RenderType.entityCutoutNoCull(beeSuitlowerPaw.getTextureResource(animatable));

                VertexConsumer accessoryBuffer = bufferSource.getBuffer(accessoryRenderType);

                float interpolatedYaw = Mth.lerp(partialTick, animatable.yBodyRotO, animatable.yBodyRot);
                poseStack.mulPose(Axis.YP.rotationDegrees(interpolatedYaw + 180f));

                accessoryRenderer.reRender(
                        bakedModel,
                        poseStack,
                        bufferSource,
                        animatable,
                        accessoryRenderType,
                        accessoryBuffer,
                        partialTick,
                        packedLight,
                        packedOverlay,
                        1f, 1f, 1f, 1f
                );

                poseStack.popPose();
                buffer = bufferSource.getBuffer(renderType);
            }

        }


        if (animatable.embeddedAccessories().hasSunGlasses()) {
            if (bone.getName().equals(Bones.HEAD.string())) {
                var bakedModel = sunglassesModel.getBakedModel(sunglassesModel.getModelResource(animatable));

                poseStack.pushPose();
                poseStack.translate(0, -0.015D, -0);


                RenderType accessoryRenderType = RenderType.entityCutoutNoCull(sunglassesModel.getTextureResource(animatable));

                VertexConsumer accessoryBuffer = bufferSource.getBuffer(accessoryRenderType);

                float interpolatedYaw = Mth.lerp(partialTick, animatable.yBodyRotO, animatable.yBodyRot);
                poseStack.mulPose(Axis.YP.rotationDegrees(interpolatedYaw + 180f));

                accessoryRenderer.reRender(
                        bakedModel,
                        poseStack,
                        bufferSource,
                        animatable,
                        accessoryRenderType,
                        accessoryBuffer,
                        partialTick,
                        packedLight,
                        packedOverlay,
                        1f, 1f, 1f, 1f
                );

                poseStack.popPose();
                buffer = bufferSource.getBuffer(renderType);
            }
        }

        if (animatable.embeddedAccessories().hasMossCoat()) {
            if (bone.getName().equals(Bones.HEAD.string())) {
                var bakedModel = mossCoatHead.getBakedModel(mossCoatHead.getModelResource(animatable));

                poseStack.pushPose();
                poseStack.translate(0, -0.01D, -0);


                RenderType accessoryRenderType = RenderType.entityCutoutNoCull(mossCoatHead.getTextureResource(animatable));

                VertexConsumer accessoryBuffer = bufferSource.getBuffer(accessoryRenderType);

                float interpolatedYaw = Mth.lerp(partialTick, animatable.yBodyRotO, animatable.yBodyRot);
                poseStack.mulPose(Axis.YP.rotationDegrees(interpolatedYaw + 180f));

                accessoryRenderer.reRender(
                        bakedModel,
                        poseStack,
                        bufferSource,
                        animatable,
                        accessoryRenderType,
                        accessoryBuffer,
                        partialTick,
                        packedLight,
                        packedOverlay,
                        1f, 1f, 1f, 1f
                );

                poseStack.popPose();
                buffer = bufferSource.getBuffer(renderType);
            }

            if (bone.getName().equals(Bones.UPPER_BODY.string())) {
                var bakedModel = mossCoatUpperBody.getBakedModel(mossCoatUpperBody.getModelResource(animatable));

                poseStack.pushPose();
                poseStack.translate(0, -0.01D, -0.001);

                RenderType accessoryRenderType = RenderType.entityCutout(mossCoatUpperBody.getTextureResource(animatable));

                VertexConsumer accessoryBuffer = bufferSource.getBuffer(accessoryRenderType);

                float interpolatedYaw = Mth.lerp(partialTick, animatable.yBodyRotO, animatable.yBodyRot);
                poseStack.mulPose(Axis.YP.rotationDegrees(interpolatedYaw + 180f));

                accessoryRenderer.reRender(
                        bakedModel,
                        poseStack,
                        bufferSource,
                        animatable,
                        accessoryRenderType,
                        accessoryBuffer,
                        partialTick,
                        packedLight,
                        packedOverlay,
                        1f, 1f, 1f, 1f
                );

                poseStack.popPose();

                buffer = bufferSource.getBuffer(renderType);
            }

            if (bone.getName().equals(Bones.UP.string())) {
                var bakedModel = mossCoatMidBody.getBakedModel(mossCoatMidBody.getModelResource(animatable));

                poseStack.pushPose();
                poseStack.translate(0, -0.01D, -0);

                RenderType accessoryRenderType = RenderType.entityCutoutNoCull(mossCoatMidBody.getTextureResource(animatable));

                VertexConsumer accessoryBuffer = bufferSource.getBuffer(accessoryRenderType);

                float interpolatedYaw = Mth.lerp(partialTick, animatable.yBodyRotO, animatable.yBodyRot);
                poseStack.mulPose(Axis.YP.rotationDegrees(interpolatedYaw + 180f));

                accessoryRenderer.reRender(
                        bakedModel,
                        poseStack,
                        bufferSource,
                        animatable,
                        accessoryRenderType,
                        accessoryBuffer,
                        partialTick,
                        packedLight,
                        packedOverlay,
                        1f, 1f, 1f, 1f
                );

                poseStack.popPose();

                buffer = bufferSource.getBuffer(renderType);
            }

            if (bone.getName().equals(Bones.LOWER_BODY.string())) {
                var bakedModel = mossCoatLowerBody.getBakedModel(mossCoatLowerBody.getModelResource(animatable));

                poseStack.pushPose();

                poseStack.translate(0, -0.01D, 0.001);

                RenderType accessoryRenderType = RenderType.entityCutout(mossCoatLowerBody.getTextureResource(animatable));

                VertexConsumer accessoryBuffer = bufferSource.getBuffer(accessoryRenderType);

                float interpolatedYaw = Mth.lerp(partialTick, animatable.yBodyRotO, animatable.yBodyRot);
                poseStack.mulPose(Axis.YP.rotationDegrees(interpolatedYaw + 180f));

                accessoryRenderer.reRender(
                        bakedModel,
                        poseStack,
                        bufferSource,
                        animatable,
                        accessoryRenderType,
                        accessoryBuffer,
                        partialTick,
                        packedLight,
                        packedOverlay,
                        1f, 1f, 1f, 1f
                );

                poseStack.popPose();

                buffer = bufferSource.getBuffer(renderType);
            }

            if (bone.getName().equals(Bones.FRONT_RIGHT_LEG.string())) {
                var bakedModel = mossCoatLeg.getBakedModel(mossCoatLeg.getModelResource(animatable));

                poseStack.pushPose();
                poseStack.translate(0, -0.0101D, -0);


                RenderType accessoryRenderType = RenderType.entityCutoutNoCull(mossCoatLeg.getTextureResource(animatable));

                VertexConsumer accessoryBuffer = bufferSource.getBuffer(accessoryRenderType);

                float interpolatedYaw = Mth.lerp(partialTick, animatable.yBodyRotO, animatable.yBodyRot);
                poseStack.mulPose(Axis.YP.rotationDegrees(interpolatedYaw + 180f));

                accessoryRenderer.reRender(
                        bakedModel,
                        poseStack,
                        bufferSource,
                        animatable,
                        accessoryRenderType,
                        accessoryBuffer,
                        partialTick,
                        packedLight,
                        packedOverlay,
                        1f, 1f, 1f, 1f
                );

                poseStack.popPose();
                buffer = bufferSource.getBuffer(renderType);
            }
            if (bone.getName().equals(Bones.FRONT_LEFT_LEG.string())) {
                var bakedModel = mossCoatLeg.getBakedModel(mossCoatLeg.getModelResource(animatable));

                poseStack.pushPose();
                poseStack.translate(-0.135, -0.0101D, -0);


                RenderType accessoryRenderType = RenderType.entityCutoutNoCull(mossCoatLeg.getTextureResource(animatable));

                VertexConsumer accessoryBuffer = bufferSource.getBuffer(accessoryRenderType);

                float interpolatedYaw = Mth.lerp(partialTick, animatable.yBodyRotO, animatable.yBodyRot);
                poseStack.mulPose(Axis.YP.rotationDegrees(interpolatedYaw + 180f));

                accessoryRenderer.reRender(
                        bakedModel,
                        poseStack,
                        bufferSource,
                        animatable,
                        accessoryRenderType,
                        accessoryBuffer,
                        partialTick,
                        packedLight,
                        packedOverlay,
                        1f, 1f, 1f, 1f
                );

                poseStack.popPose();
                buffer = bufferSource.getBuffer(renderType);
            }

            if (bone.getName().equals(Bones.FRONT_RIGHT_PAW.string())) {
                var bakedModel = mossCoatPaw.getBakedModel(mossCoatPaw.getModelResource(animatable));

                poseStack.pushPose();
                poseStack.translate(0, -0.0101D, -0);


                RenderType accessoryRenderType = RenderType.entityCutoutNoCull(mossCoatPaw.getTextureResource(animatable));

                VertexConsumer accessoryBuffer = bufferSource.getBuffer(accessoryRenderType);

                float interpolatedYaw = Mth.lerp(partialTick, animatable.yBodyRotO, animatable.yBodyRot);
                poseStack.mulPose(Axis.YP.rotationDegrees(interpolatedYaw + 180f));

                accessoryRenderer.reRender(
                        bakedModel,
                        poseStack,
                        bufferSource,
                        animatable,
                        accessoryRenderType,
                        accessoryBuffer,
                        partialTick,
                        packedLight,
                        packedOverlay,
                        1f, 1f, 1f, 1f
                );

                poseStack.popPose();
                buffer = bufferSource.getBuffer(renderType);
            }
            if (bone.getName().equals(Bones.FRONT_LEFT_PAW.string())) {
                var bakedModel = mossCoatPaw.getBakedModel(mossCoatPaw.getModelResource(animatable));

                poseStack.pushPose();
                poseStack.translate(-0.135, -0.0101D, -0);


                RenderType accessoryRenderType = RenderType.entityCutoutNoCull(mossCoatPaw.getTextureResource(animatable));

                VertexConsumer accessoryBuffer = bufferSource.getBuffer(accessoryRenderType);

                float interpolatedYaw = Mth.lerp(partialTick, animatable.yBodyRotO, animatable.yBodyRot);
                poseStack.mulPose(Axis.YP.rotationDegrees(interpolatedYaw + 180f));

                accessoryRenderer.reRender(
                        bakedModel,
                        poseStack,
                        bufferSource,
                        animatable,
                        accessoryRenderType,
                        accessoryBuffer,
                        partialTick,
                        packedLight,
                        packedOverlay,
                        1f, 1f, 1f, 1f
                );

                poseStack.popPose();
                buffer = bufferSource.getBuffer(renderType);
            }
        }


        ItemStack dockBagStack = ItemStack.EMPTY;
        ItemStack dockBagChest = animatable.getItemBySlot(EquipmentSlot.CHEST);
        if (dockBagChest.getItem() instanceof DockBackpackItem) {
            dockBagStack = dockBagChest;
        }

        if (dockBagStack.isEmpty()) {
            ItemStack curiosStack = CompatibilitiesClient.getCuriosItem(
                    animatable.getPlayerBoundUuid(), ModItems.DOCK_BACKPACK.get());

            if (!curiosStack.isEmpty()) {
                dockBagStack = curiosStack;
            }
        }

        if (!dockBagStack.isEmpty()) {
            if (bone.getName().equals(Bones.UP.string())) {
                var bakedModel = dockBagModel.getBakedModel(dockBagModel.getModelResource(animatable));

                poseStack.pushPose();

                boolean filled = DockBackpackItem.getOccupiedSlots(dockBagStack) > 2;
                dockBagModel.getBone("flowers").ifPresent(b -> b.setHidden(!filled));

                poseStack.translate(-0.043, 0.303D, -0);

                RenderType accessoryRenderType = RenderType.entityCutout(dockBagModel.getTextureResource(animatable));

                VertexConsumer accessoryBuffer = bufferSource.getBuffer(accessoryRenderType);

                float interpolatedYaw = Mth.lerp(partialTick, animatable.yBodyRotO, animatable.yBodyRot);
                poseStack.mulPose(Axis.YP.rotationDegrees(interpolatedYaw + 180f));

                accessoryRenderer.reRender(
                        bakedModel,
                        poseStack,
                        bufferSource,
                        animatable,
                        accessoryRenderType,
                        accessoryBuffer,
                        partialTick,
                        packedLight,
                        packedOverlay,
                        1f, 1f, 1f, 1f
                );

                poseStack.popPose();
                buffer = bufferSource.getBuffer(renderType);
            }
        }

        {
            boolean hasBracelet = false;
            boolean hasSpikes;
            int light = packedLight;
            boolean spaceKitty = false;


            ItemStack braceletStack = ItemStack.EMPTY;
            ItemStack pawsBraceletStack = animatable.getItemBySlot(EquipmentSlot.FEET);
            if (pawsBraceletStack.getItem() instanceof BraceletArmorItem) {
                braceletStack = pawsBraceletStack;
            }

            if (braceletStack.isEmpty()) {
                ItemStack curiosStack = CompatibilitiesClient.getCuriosItem(
                        animatable.getPlayerBoundUuid(), BraceletArmorItem.class
                );

                if (!curiosStack.isEmpty()) {
                    braceletStack = curiosStack;
                }
            }

            if (braceletStack.getItem() instanceof BraceletArmorItem bracelet) {

                hasBracelet = true;

                if (bracelet.hasSpikes(braceletStack)) {
                    hasSpikes = true;
                } else {
                    hasSpikes = false;
                }

                if (bracelet.hasGlow(braceletStack)) {
                    light = 255;
                }

                if (braceletStack.hasCustomHoverName()) {
                    if (braceletStack.getHoverName().getString().toLowerCase(Locale.ROOT).equals("space kitty")) {
                        spaceKitty = true;
                    }
                }

            } else {
                hasSpikes = false;
            }

            if (hasBracelet) {
                var bakedModel = braceletModel.getBakedModel(braceletModel.getModelResource(animatable));
                var bakedModel2 = braceletExtraModel.getBakedModel(braceletExtraModel.getModelResource(animatable));

                float r = 1f;
                float g = 1f;
                float b = 1f;
                if (braceletStack.getItem() instanceof DyeableLeatherItem dye) {
                    int i = dye.getColor(braceletStack);
                    r = (float)(i >> 16 & 255) / 255.0F;
                    g = (float)(i >> 8 & 255) / 255.0F;
                    b = (float)(i & 255) / 255.0F;
                }

                if (bone.getName().equals(Bones.FRONT_RIGHT_PAW.string())) {

                    poseStack.pushPose();

                    poseStack.translate(-0.001D, -0.05D, -0.0D);

                    float scale = 1.02f;
                    poseStack.scale(scale, scale, scale);

                    RenderType accessoryRenderType = RenderType.entityCutoutNoCull(braceletModel.getTextureResource(animatable));

                    if (spaceKitty) accessoryRenderType = RenderType.endGateway();

                    VertexConsumer accessoryBuffer = bufferSource.getBuffer(accessoryRenderType);

                    float interpolatedYaw = Mth.lerp(partialTick, animatable.yBodyRotO, animatable.yBodyRot);
                    poseStack.mulPose(Axis.YP.rotationDegrees(interpolatedYaw + 180f));

                    accessoryRenderer.reRender(
                            bakedModel,
                            poseStack, bufferSource, animatable, accessoryRenderType, accessoryBuffer,
                            partialTick, light, packedOverlay,
                            r,g,b,1
                    );

                    if (hasSpikes) {
                        RenderType accessoryRenderType2 = RenderType.entityCutoutNoCull(braceletExtraModel.getTextureResource(animatable));
                        VertexConsumer accessoryBuffer2 = bufferSource.getBuffer(accessoryRenderType2);

                        accessoryRenderer.reRender(
                                bakedModel2,
                                poseStack, bufferSource, animatable, accessoryRenderType2, accessoryBuffer2,
                                partialTick, light, packedOverlay,
                                1,1,1,1
                        );
                    }

                    poseStack.popPose();
                    buffer = bufferSource.getBuffer(renderType);
                }

                if (bone.getName().equals(Bones.FRONT_LEFT_PAW.string())) {

                    poseStack.pushPose();

                    poseStack.translate(-0.137D, -0.05D, -0.0D);

                    float scale = 1.02f;
                    poseStack.scale(scale, scale, scale);

                    RenderType accessoryRenderType = RenderType.entityCutoutNoCull(braceletModel.getTextureResource(animatable));

                    if (spaceKitty) accessoryRenderType = RenderType.endGateway();

                    VertexConsumer accessoryBuffer = bufferSource.getBuffer(accessoryRenderType);

                    float interpolatedYaw = Mth.lerp(partialTick, animatable.yBodyRotO, animatable.yBodyRot);
                    poseStack.mulPose(Axis.YP.rotationDegrees(interpolatedYaw + 180f));

                    accessoryRenderer.reRender(
                            bakedModel,
                            poseStack, bufferSource, animatable, accessoryRenderType, accessoryBuffer,
                            partialTick, light, packedOverlay,
                            r,g,b,1
                    );

                    if (hasSpikes) {
                        RenderType accessoryRenderType2 = RenderType.entityCutoutNoCull(braceletExtraModel.getTextureResource(animatable));
                        VertexConsumer accessoryBuffer2 = bufferSource.getBuffer(accessoryRenderType2);

                        accessoryRenderer.reRender(
                                bakedModel2,
                                poseStack, bufferSource, animatable, accessoryRenderType2, accessoryBuffer2,
                                partialTick, light, packedOverlay,
                                1f, 1f, 1f, 1f
                        );
                    }

                    poseStack.popPose();
                    buffer = bufferSource.getBuffer(renderType);
                }
            }
        }








        if (Minecraft.getInstance().level != null && Minecraft.getInstance().level.getPlayerByUUID(animatable.getPlayerBoundUuid()) != null) {
            Player player = Minecraft.getInstance().level.getPlayerByUUID(animatable.getPlayerBoundUuid());

            if (animatable.getFirstPassenger() instanceof LizardEntity lizardEntity) {

                if (bone.getName().equals(Bones.HEAD.string())) {

                    poseStack.pushPose();

                    poseStack.translate(-0.00D, 0.678D, -0.6D);
                    float scale = 1.3f;
                    poseStack.scale(scale, scale, scale);

                    float interpolatedYaw = Mth.lerp(partialTick, animatable.yBodyRotO, animatable.yBodyRot);
                    poseStack.mulPose(Axis.YP.rotationDegrees(interpolatedYaw + 180f));

                    lizRenderer.isAccessory = true;
                    lizRenderer.render(lizardEntity, 0, partialTick,
                            poseStack, bufferSource, packedLight);
                    lizRenderer.isAccessory = false;

                    poseStack.popPose();
                    buffer = bufferSource.getBuffer(renderType);
                }
            } else if ((player != null && player.getFirstPassenger() instanceof Player player1)) {
                LivingEntity shape = PlayerShape.getCurrentShape(player1);
                if (bone.getName().equals(Bones.HEAD.string()) && shape instanceof WCatEntity cat) {
                    pendingCarriedCatMatrix.put(animatable.getId(), new Matrix4f(poseStack.last().pose()));
                    pendingCarriedCat.put(animatable.getId(), cat);
                }
            } else if ((animatable.getFirstPassenger() instanceof WCatEntity cat)) {
                if (bone.getName().equals(Bones.HEAD.string())) {
                    pendingCarriedCatMatrix.put(animatable.getId(), new Matrix4f(poseStack.last().pose()));
                    pendingCarriedCat.put(animatable.getId(), cat);
                }
            }

        }

    }

    private void renderClaws(ItemRenderer itemRenderer, MultiBufferSource bufferSource, PoseStack poseStack, ItemStack stack, int packedLight) {
        itemRenderer.renderStatic(stack, ItemDisplayContext.FIXED, packedLight,
                OverlayTexture.NO_OVERLAY, poseStack,
                bufferSource, Minecraft.getInstance().level, 1);

        poseStack.translate(0.0D, 0.0D, -0.15D);

        itemRenderer.renderStatic(stack, ItemDisplayContext.FIXED, packedLight,
                OverlayTexture.NO_OVERLAY, poseStack,
                bufferSource, Minecraft.getInstance().level, 1);

        poseStack.translate(0.0D, 0.0D, -0.15D);

        itemRenderer.renderStatic(stack, ItemDisplayContext.FIXED, packedLight,
                OverlayTexture.NO_OVERLAY, poseStack,
                bufferSource, Minecraft.getInstance().level, 1);

    }

    private final Map<Integer, Matrix4f> pendingCarriedCatMatrix = new HashMap<>();
    private final Map<Integer, WCatEntity> pendingCarriedCat = new HashMap<>();

    public Matrix4f consumePendingCarriedCatMatrix(int entityId) {
        return pendingCarriedCatMatrix.remove(entityId);
    }

    public WCatEntity consumePendingCarriedCat(int entityId) {
        return pendingCarriedCat.remove(entityId);
    }

    public void renderCarryingCat(PoseStack poseStack, WCatEntity animatable, MultiBufferSource bufferSource, float partialTick, int packedLight, WCatEntity cat) {
        poseStack.pushPose();

        float parentScale = catRenderer.getVisualScale(animatable);
        poseStack.translate(0.0, 0.238F * parentScale, -0.9F);
        float inv = 1.0F / parentScale;
        poseStack.scale(inv, inv, inv);

        float interpolatedYaw = Mth.lerp(partialTick, animatable.yBodyRotO, animatable.yBodyRot);
        poseStack.mulPose(Axis.YP.rotationDegrees(interpolatedYaw + 180f));

        catRenderer.isAccessory = true;
        catRenderer.render(cat, 0, partialTick,
                poseStack, bufferSource, packedLight);
        catRenderer.isAccessory = false;

        poseStack.popPose();
    }


    @Override
    public void render(PoseStack poseStack, WCatEntity animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {

        super.render(poseStack, animatable, bakedModel, renderType, bufferSource, buffer, partialTick, packedLight, packedOverlay);
    }

    public static ResourceLocation resolveElytraTexture(LivingEntity entity, ItemStack stack) {

        if (WarriorCatsEvents.Collaborators.isContributor(entity.getUUID())) {
            if (stack.hasCustomHoverName() && stack.getHoverName().getString().equalsIgnoreCase("wce")) {
                return ElytraModel.DEV_TEXTURE2;
            }
            if (stack.hasCustomHoverName() && stack.getHoverName().getString().equalsIgnoreCase("starclan")) {
                return ElytraModel.DEV_TEXTURE;
            }
        }

        if (entity instanceof AbstractClientPlayer player) {


            if (player.isElytraLoaded() && player.getElytraTextureLocation() != null) {
                return player.getElytraTextureLocation();
            }

            if (player.isCapeLoaded() && player.getCloakTextureLocation() != null && player.isModelPartShown(PlayerModelPart.CAPE)) {
                return player.getCloakTextureLocation();
            }
        }

        return ResourceLocation.withDefaultNamespace("textures/entity/elytra.png");
    }
}
