package net.snowteb.warriorcats_events.entity.client;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.snowteb.warriorcats_events.WarriorCatsEvents;
import net.snowteb.warriorcats_events.compat.CompatibilitiesClient;
import net.snowteb.warriorcats_events.entity.custom.WCGenetics;
import net.snowteb.warriorcats_events.entity.custom.WCatEntity;
import net.snowteb.warriorcats_events.item.ModItems;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class WCModel extends GeoModel<WCatEntity> {

    ModelPart rightPaw;

    /**
     * An array list of all the textures available.
     */
    public static final ResourceLocation[] TEXTURES = {
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "textures/entity/wcat/wcskin1.png"),
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "textures/entity/wcat/wcskin2.png"),
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "textures/entity/wcat/wcskin3.png"),
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "textures/entity/wcat/wcskin4.png"),
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "textures/entity/wcat/wcskin5.png"),
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "textures/entity/wcat/wcskin6.png"), // 5
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "textures/entity/wcat/wcskin7.png"),
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "textures/entity/wcat/wcskin8.png"),
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "textures/entity/wcat/wcskin9.png"),
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "textures/entity/wcat/wcskin10.png"),
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "textures/entity/wcat/wcskin11.png"), //10
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "textures/entity/wcat/wcskin12.png"),
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "textures/entity/wcat/chestnutpatch.png"),
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "textures/entity/wcat/ratstar.png"),
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "textures/entity/wcat/twitchstream.png"),
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "textures/entity/wcat/blazepit.png"), // 15
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "textures/entity/wcat/bengalpelt.png"),
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "textures/entity/wcat/sparrowstar.png"),
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "textures/entity/wcat/foxeater.png"),
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "textures/entity/wcat/willowsong.png"),
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "textures/entity/wcat/wcskin13.png"), //20
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "textures/entity/wcat/wcskin14.png"),
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "textures/entity/wcat/wcskin15.png"),
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "textures/entity/wcat/wcskin16.png"),
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "textures/entity/wcat/wcskin17.png"),
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "textures/entity/wcat/wcskin18.png"), //25
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "textures/entity/wcat/wcskin19.png"),
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "textures/entity/wcat/wcskin20.png"),
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "textures/entity/wcat/wcskin21.png"),
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "textures/entity/wcat/wcskin22.png"),

            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "textures/entity/wcat/albino.png"), //30
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "textures/entity/wcat/bengal.png"),
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "textures/entity/wcat/brindle_tortie.png"),
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "textures/entity/wcat/brown_cream_calico.png"),
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "textures/entity/wcat/brown_cream_calico2.png"),
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "textures/entity/wcat/brown_cream_calico3.png"), //35
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "textures/entity/wcat/caramel.png"),
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "textures/entity/wcat/frostdawn.png"),
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "textures/entity/wcat/gray_white_tabby.png"),
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "textures/entity/wcat/hailflake.png"),
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "textures/entity/wcat/karpati.png"), //40
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "textures/entity/wcat/leafstar.png"),
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "textures/entity/wcat/longtail.png"),
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "textures/entity/wcat/mothpaw.png"),
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "textures/entity/wcat/redtail.png"),
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "textures/entity/wcat/salem.png"), // 45
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "textures/entity/wcat/short_hair.png"),
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "textures/entity/wcat/stoneflare.png"),
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "textures/entity/wcat/tortie_point.png"),
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "textures/entity/wcat/turtleheart.png"),
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "textures/entity/wcat/violetdew.png"), //50
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "textures/entity/wcat/patch.png"),
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "textures/entity/wcat/parlee.png"), //52
    };

    @Override
    public ResourceLocation getModelResource(WCatEntity object) {
        return ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "geo/wcat.geo.json");
    }

    /**
     * Depending on the variant, grab a texture from the list.
     */
    @Override
    public ResourceLocation getTextureResource(WCatEntity object) {
        return TEXTURES[object.getVariant()];
    }

    @Override
    public ResourceLocation getAnimationResource(WCatEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "animations/wcat.animation.json");
    }


    @Override
    public void setCustomAnimations(WCatEntity animatable, long instanceId, AnimationState<WCatEntity> animationState) {
        CoreGeoBone head = animatable.isAnImage() ? null : getAnimationProcessor().getBone("mainHead");

//        getBone("crown").ifPresent(bone -> {
//            bone.setHidden(true);
//        });

//        getBone("crown").ifPresent(bone -> {
//            boolean hasCrown = animatable
//                    .getItemBySlot(EquipmentSlot.HEAD)
//                    .is(ModItems.FLOWER_CROWN.get());
//            bone.setHidden(!hasCrown);
//        });

//        getBone("mane").ifPresent(bone -> {
//            boolean hasMane = animatable
//                    .getItemBySlot(EquipmentSlot.HEAD)
//                    .is(ModItems.LEAF_MANE.get());
//            bone.setHidden(true);
//        });

        boolean hasChestFur = (WCGenetics.FurGene.isLongFur(animatable.getGenetics().chestFur));
        boolean hasBellyFur = (WCGenetics.FurGene.isLongFur(animatable.getGenetics().bellyFur));
        boolean hasLegsFur = (WCGenetics.FurGene.isLongFur(animatable.getGenetics().legsFur));
        boolean hasHeadFur = (WCGenetics.FurGene.isLongFur(animatable.getGenetics().headFur));
        boolean hasCheekFur = (WCGenetics.FurGene.isLongFur(animatable.getGenetics().cheekFur));
        boolean hasBackFur = (WCGenetics.FurGene.isLongFur(animatable.getGenetics().backFur));
        boolean hasTailFur = (WCGenetics.FurGene.isLongFur(animatable.getGenetics().tailFur));
        boolean isBobtail = (WCGenetics.Bobtail.isBobtail(animatable.getGenetics().bobtail));

        {
            getBone("chest_fur").ifPresent(bone -> bone.setHidden(!hasChestFur));
            getBone("belly_fur").ifPresent(bone -> bone.setHidden(!hasBellyFur));

            getBone("front_right_fur2").ifPresent(bone -> bone.setHidden(!hasLegsFur));
            getBone("front_right_fur").ifPresent(bone -> bone.setHidden(!hasLegsFur));

            getBone("head_tuft").ifPresent(bone -> bone.setHidden(!hasHeadFur));
            getBone("face_fur").ifPresent(bone -> bone.setHidden(!hasCheekFur));
            getBone("back_fur").ifPresent(bone -> bone.setHidden(!hasBackFur));

            getBone("tailsub").ifPresent(bone -> bone.setHidden(isBobtail));
            getBone("tail2").ifPresent(bone -> bone.setHidden(isBobtail));

            getBone("tail_fur").ifPresent(bone -> bone.setHidden(!hasTailFur));
            getBone("tail_fur2").ifPresent(bone -> bone.setHidden(!hasTailFur));
            getBone("tail_fur3").ifPresent(bone -> bone.setHidden(!hasTailFur));

        }


        {
            boolean hasFlowerArmor = animatable.getItemBySlot(EquipmentSlot.CHEST).is(ModItems.FLOWER_ARMOR.get());
            if (CompatibilitiesClient.hasCuriosItem(animatable.getPlayerBoundUuid(), ModItems.FLOWER_ARMOR.get()))
                hasFlowerArmor = true;


            boolean finalHasFlowerArmor = hasFlowerArmor;
            getBone("flower_upper_armor").ifPresent(bone -> bone.setHidden(!finalHasFlowerArmor));
            getBone("layer_2").ifPresent(bone -> bone.setHidden(!finalHasFlowerArmor));
            getBone("layer_3").ifPresent(bone -> bone.setHidden(!finalHasFlowerArmor));

            {

                boolean hasTeethClaws = animatable.getItemBySlot(EquipmentSlot.FEET).is(ModItems.TEETH_CLAWS.get());
                if (CompatibilitiesClient.hasCuriosItem(animatable.getPlayerBoundUuid(), ModItems.TEETH_CLAWS.get()))
                    hasTeethClaws = true;


                boolean finalHasTeethClaws = hasTeethClaws;
                getBone("teethclaws1").ifPresent(bone -> bone.setHidden(!finalHasTeethClaws));
                getBone("teethclaws2").ifPresent(bone -> bone.setHidden(!finalHasTeethClaws));
                getBone("teethclaws3").ifPresent(bone -> bone.setHidden(!finalHasTeethClaws));
                getBone("teethclaws4").ifPresent(bone -> bone.setHidden(!finalHasTeethClaws));
            }

            if (head != null) {
                EntityModelData entityModelData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
                if (!animatable.isAnImage()) {
                    head.setRotX(entityModelData.headPitch() * Mth.DEG_TO_RAD);
                    head.setRotY(entityModelData.netHeadYaw() * Mth.DEG_TO_RAD);
                }
            }

            if (head != null && animatable.isAnImage()) {
                head.setRotX(0);
                head.setRotY(0);
                head.setRotZ(0);
            }
        }


    }
}
