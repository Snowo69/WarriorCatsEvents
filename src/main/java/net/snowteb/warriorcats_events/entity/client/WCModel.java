package net.snowteb.warriorcats_events.entity.client;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.snowteb.warriorcats_events.WarriorCatsEvents;
import net.snowteb.warriorcats_events.compat.Compatibilities;
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
            new ResourceLocation("warriorcats_events:textures/entity/wcat/wcskin1.png"),
            new ResourceLocation("warriorcats_events:textures/entity/wcat/wcskin2.png"),
            new ResourceLocation("warriorcats_events:textures/entity/wcat/wcskin3.png"),
            new ResourceLocation("warriorcats_events:textures/entity/wcat/wcskin4.png"),
            new ResourceLocation("warriorcats_events:textures/entity/wcat/wcskin5.png"),
            new ResourceLocation("warriorcats_events:textures/entity/wcat/wcskin6.png"), // 5
            new ResourceLocation("warriorcats_events:textures/entity/wcat/wcskin7.png"),
            new ResourceLocation("warriorcats_events:textures/entity/wcat/wcskin8.png"),
            new ResourceLocation("warriorcats_events:textures/entity/wcat/wcskin9.png"),
            new ResourceLocation("warriorcats_events:textures/entity/wcat/wcskin10.png"),
            new ResourceLocation("warriorcats_events:textures/entity/wcat/wcskin11.png"), //10
            new ResourceLocation("warriorcats_events:textures/entity/wcat/wcskin12.png"),
            new ResourceLocation("warriorcats_events:textures/entity/wcat/chestnutpatch.png"),
            new ResourceLocation("warriorcats_events:textures/entity/wcat/ratstar.png"),
            new ResourceLocation("warriorcats_events:textures/entity/wcat/twitchstream.png"),
            new ResourceLocation("warriorcats_events:textures/entity/wcat/blazepit.png"), // 15
            new ResourceLocation("warriorcats_events:textures/entity/wcat/bengalpelt.png"),
            new ResourceLocation("warriorcats_events:textures/entity/wcat/sparrowstar.png"),
            new ResourceLocation("warriorcats_events:textures/entity/wcat/foxeater.png"),
            new ResourceLocation("warriorcats_events:textures/entity/wcat/willowsong.png"),
            new ResourceLocation("warriorcats_events:textures/entity/wcat/wcskin13.png"), //20
            new ResourceLocation("warriorcats_events:textures/entity/wcat/wcskin14.png"),
            new ResourceLocation("warriorcats_events:textures/entity/wcat/wcskin15.png"),
            new ResourceLocation("warriorcats_events:textures/entity/wcat/wcskin16.png"),
            new ResourceLocation("warriorcats_events:textures/entity/wcat/wcskin17.png"),
            new ResourceLocation("warriorcats_events:textures/entity/wcat/wcskin18.png"), //25
            new ResourceLocation("warriorcats_events:textures/entity/wcat/wcskin19.png"),
            new ResourceLocation("warriorcats_events:textures/entity/wcat/wcskin20.png"),
            new ResourceLocation("warriorcats_events:textures/entity/wcat/wcskin21.png"),
            new ResourceLocation("warriorcats_events:textures/entity/wcat/wcskin22.png"),

            new ResourceLocation("warriorcats_events:textures/entity/wcat/albino.png"), //30
            new ResourceLocation("warriorcats_events:textures/entity/wcat/bengal.png"),
            new ResourceLocation("warriorcats_events:textures/entity/wcat/brindle_tortie.png"),
            new ResourceLocation("warriorcats_events:textures/entity/wcat/brown_cream_calico.png"),
            new ResourceLocation("warriorcats_events:textures/entity/wcat/brown_cream_calico2.png"),
            new ResourceLocation("warriorcats_events:textures/entity/wcat/brown_cream_calico3.png"), //35
            new ResourceLocation("warriorcats_events:textures/entity/wcat/caramel.png"),
            new ResourceLocation("warriorcats_events:textures/entity/wcat/frostdawn.png"),
            new ResourceLocation("warriorcats_events:textures/entity/wcat/gray_white_tabby.png"),
            new ResourceLocation("warriorcats_events:textures/entity/wcat/hailflake.png"),
            new ResourceLocation("warriorcats_events:textures/entity/wcat/karpati.png"), //40
            new ResourceLocation("warriorcats_events:textures/entity/wcat/leafstar.png"),
            new ResourceLocation("warriorcats_events:textures/entity/wcat/longtail.png"),
            new ResourceLocation("warriorcats_events:textures/entity/wcat/mothpaw.png"),
            new ResourceLocation("warriorcats_events:textures/entity/wcat/redtail.png"),
            new ResourceLocation("warriorcats_events:textures/entity/wcat/salem.png"), // 45
            new ResourceLocation("warriorcats_events:textures/entity/wcat/short_hair.png"),
            new ResourceLocation("warriorcats_events:textures/entity/wcat/stoneflare.png"),
            new ResourceLocation("warriorcats_events:textures/entity/wcat/tortie_point.png"),
            new ResourceLocation("warriorcats_events:textures/entity/wcat/turtleheart.png"),
            new ResourceLocation("warriorcats_events:textures/entity/wcat/violetdew.png"), //50
            new ResourceLocation("warriorcats_events:textures/entity/wcat/patch.png"),
            new ResourceLocation("warriorcats_events:textures/entity/wcat/parlee.png"), //52
    };

    @Override
    public ResourceLocation getModelResource(WCatEntity object) {
        return new ResourceLocation(WarriorCatsEvents.MODID, "geo/wcat.geo.json");
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
        return new ResourceLocation(WarriorCatsEvents.MODID, "animations/wcat.animation.json");
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
            if (Compatibilities.hasCuriosItem(animatable.getPlayerBoundUuid(), ModItems.FLOWER_ARMOR.get()))
                hasFlowerArmor = true;


            boolean finalHasFlowerArmor = hasFlowerArmor;
            getBone("flower_upper_armor").ifPresent(bone -> bone.setHidden(!finalHasFlowerArmor));
            getBone("layer_2").ifPresent(bone -> bone.setHidden(!finalHasFlowerArmor));
            getBone("layer_3").ifPresent(bone -> bone.setHidden(!finalHasFlowerArmor));

            {

                boolean hasTeethClaws = animatable.getItemBySlot(EquipmentSlot.FEET).is(ModItems.TEETH_CLAWS.get());
                if (Compatibilities.hasCuriosItem(animatable.getPlayerBoundUuid(), ModItems.TEETH_CLAWS.get()))
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
