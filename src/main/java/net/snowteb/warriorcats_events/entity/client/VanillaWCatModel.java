//package net.snowteb.warriorcats_events.entity.client;// Made with Blockbench 5.0.4
//// Exported for Minecraft version 1.17 or later with Mojang mappings
//// Paste this class into your mod and generate all required imports
//
//
//import com.mojang.blaze3d.vertex.PoseStack;
//import com.mojang.blaze3d.vertex.VertexConsumer;
//import net.minecraft.client.model.EntityModel;
//import net.minecraft.client.model.HierarchicalModel;
//import net.minecraft.client.model.geom.ModelLayerLocation;
//import net.minecraft.client.model.geom.ModelPart;
//import net.minecraft.client.model.geom.PartPose;
//import net.minecraft.client.model.geom.builders.*;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.util.Mth;
//import net.minecraft.world.entity.Entity;
////import net.snowteb.warriorcats_events.entity.animations.VanillaWCatAnimations;
////import net.snowteb.warriorcats_events.entity.custom.VanillaWCatEntity;
//
//public class VanillaWCatModel<T extends Entity> extends HierarchicalModel<T> {
//	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "unknown"), "main");
//	private final ModelPart whole;
//	private final ModelPart up;
//	private final ModelPart moreup;
//	private final ModelPart front_left_leg;
//	private final ModelPart front_left_leg2down;
//	private final ModelPart front_left_leg2upper;
//	public final ModelPart front_right_leg;
//	private final ModelPart front_right_legDOWN;
//	private final ModelPart front_right_legUPPER;
//	private final ModelPart mainHead;
//	private final ModelPart head;
//	private final ModelPart DOWN;
//	private final ModelPart bodydown2;
//	private final ModelPart back_left_leg;
//	private final ModelPart back_right_leg;
//	private final ModelPart tailALL;
//	private final ModelPart tail;
//	private final ModelPart tailsub;
//	private final ModelPart tail2;
//
//	public VanillaWCatModel(ModelPart root) {
//		this.whole = root.getChild("whole");
//		this.up = this.whole.getChild("up");
//		this.moreup = this.up.getChild("moreup");
//		this.front_left_leg = this.moreup.getChild("front_left_leg");
//		this.front_left_leg2down = this.front_left_leg.getChild("front_left_leg2down");
//		this.front_left_leg2upper = this.front_left_leg.getChild("front_left_leg2upper");
//		this.front_right_leg = this.moreup.getChild("front_right_leg");
//		this.front_right_legDOWN = this.front_right_leg.getChild("front_right_legDOWN");
//		this.front_right_legUPPER = this.front_right_leg.getChild("front_right_legUPPER");
//		this.mainHead = this.moreup.getChild("mainHead");
//		this.head = this.mainHead.getChild("head");
//		this.DOWN = this.whole.getChild("DOWN");
//		this.bodydown2 = this.DOWN.getChild("bodydown2");
//		this.back_left_leg = this.DOWN.getChild("back_left_leg");
//		this.back_right_leg = this.DOWN.getChild("back_right_leg");
//		this.tailALL = this.DOWN.getChild("tailALL");
//		this.tail = this.tailALL.getChild("tail");
//		this.tailsub = this.tail.getChild("tailsub");
//		this.tail2 = this.tailsub.getChild("tail2");
//	}
//
//	public static LayerDefinition createBodyLayer() {
//		MeshDefinition meshdefinition = new MeshDefinition();
//		PartDefinition partdefinition = meshdefinition.getRoot();
//
//		PartDefinition whole = partdefinition.addOrReplaceChild("whole", CubeListBuilder.create(), PartPose.offset(0.0F, 17.0F, 1.0F));
//
//		PartDefinition up = whole.addOrReplaceChild("up", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 3.0F));
//
//		PartDefinition up_r1 = up.addOrReplaceChild("up_r1", CubeListBuilder.create().texOffs(20, 0).addBox(-2.0F, -2.0F, -18.0F, 4.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -15.0F, -2.0F, 1.5708F, 0.0F, 0.0F));
//
//		PartDefinition moreup = up.addOrReplaceChild("moreup", CubeListBuilder.create(), PartPose.offset(1.1F, 0.1F, -4.0F));
//
//		PartDefinition moreup_r1 = moreup.addOrReplaceChild("moreup_r1", CubeListBuilder.create().texOffs(20, 0).addBox(-2.0F, -9.0F, -18.0F, 4.0F, 7.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.1F, -15.1F, 2.0F, 1.5708F, 0.0F, 0.0F));
//
//		PartDefinition front_left_leg = moreup.addOrReplaceChild("front_left_leg", CubeListBuilder.create(), PartPose.offset(0.0F, -1.0F, -4.0F));
//
//		PartDefinition front_left_leg2down = front_left_leg.addOrReplaceChild("front_left_leg2down", CubeListBuilder.create().texOffs(40, 0).addBox(-0.9F, 0.1F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.1F, 3.9F, 0.0F));
//
//		PartDefinition front_left_leg2upper = front_left_leg.addOrReplaceChild("front_left_leg2upper", CubeListBuilder.create().texOffs(40, 0).addBox(0.1F, -9.9F, -5.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.1F, 7.9F, 4.0F));
//
//		PartDefinition front_right_leg = moreup.addOrReplaceChild("front_right_leg", CubeListBuilder.create(), PartPose.offset(-2.2F, -1.0F, -4.0F));
//
//		PartDefinition front_right_legDOWN = front_right_leg.addOrReplaceChild("front_right_legDOWN", CubeListBuilder.create().texOffs(40, 0).addBox(-1.1F, 0.1F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.1F, 3.9F, 0.0F));
//
//		PartDefinition front_right_legUPPER = front_right_leg.addOrReplaceChild("front_right_legUPPER", CubeListBuilder.create().texOffs(40, 0).addBox(-2.1F, -9.9F, -5.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(1.1F, 7.9F, 4.0F));
//
//		PartDefinition mainHead = moreup.addOrReplaceChild("mainHead", CubeListBuilder.create(), PartPose.offset(-1.1F, -2.1F, -6.0F));
//
//		PartDefinition head = mainHead.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-2.5F, -2.0F, -6.0F, 5.0F, 4.0F, 5.0F, new CubeDeformation(0.0F))
//		.texOffs(0, 24).addBox(-1.5F, -0.02F, -7.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
//		.texOffs(0, 10).addBox(-2.0F, -3.0F, -3.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
//		.texOffs(6, 10).addBox(1.0F, -3.0F, -3.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
//
//		PartDefinition DOWN = whole.addOrReplaceChild("DOWN", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 1.0F));
//
//		PartDefinition bodydown2 = DOWN.addOrReplaceChild("bodydown2", CubeListBuilder.create().texOffs(20, 0).addBox(-2.0F, 1.0F, -3.0F, 4.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.5708F, 0.0F, 0.0F));
//
//		PartDefinition back_left_leg = DOWN.addOrReplaceChild("back_left_leg", CubeListBuilder.create().texOffs(8, 13).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(1.1F, 1.0F, 5.0F));
//
//		PartDefinition back_right_leg = DOWN.addOrReplaceChild("back_right_leg", CubeListBuilder.create().texOffs(8, 13).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.1F, 1.0F, 5.0F));
//
//		PartDefinition tailALL = DOWN.addOrReplaceChild("tailALL", CubeListBuilder.create(), PartPose.offset(0.0F, -2.0F, 6.0F));
//
//		PartDefinition tail = tailALL.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(0, 15).addBox(-0.5F, -1.0F, 0.0F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.5F, 1.0F, 1.1345F, 0.0F, 0.0F));
//
//		PartDefinition tailsub = tail.addOrReplaceChild("tailsub", CubeListBuilder.create().texOffs(0, 15).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 3.0F, 0.0F));
//
//		PartDefinition tail2 = tailsub.addOrReplaceChild("tail2", CubeListBuilder.create().texOffs(4, 15).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 4.0F, 0.0F, 0.6109F, 0.0F, 0.0F));
//
//		return LayerDefinition.create(meshdefinition, 64, 32);
//	}
//
//	@Override
//	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
////        this.root().getAllParts().forEach(ModelPart::resetPose);
////        this.applyHeadRotation(netHeadYaw, headPitch, ageInTicks);
////
////        VanillaWCatEntity cat = (VanillaWCatEntity) entity;
////
////        this.animateWalk(VanillaWCatAnimations.VANCAT_WALK, limbSwing, limbSwingAmount, 1.2f, 2.5f);
////
////        if(cat.idleAnimationState.isStarted()) {
////            this.animate(cat.idleAnimationState, VanillaWCatAnimations.VANCAT_IDLE, ageInTicks, 1f);
////        }
////        if(cat.fallingAnimationState.isStarted()) {
////            this.animate(cat.fallingAnimationState, VanillaWCatAnimations.VANCAT_FALLING, ageInTicks, 1f);
////        }
//    }
//
//    private void applyHeadRotation(float pNetHeadYaw, float pHeadPitch, float pAgeInTicks) {
//        pNetHeadYaw = Mth.clamp(pNetHeadYaw, -30.0F, 30.0F);
//        pHeadPitch = Mth.clamp(pHeadPitch, -25.0F, 45.0F);
//
//        this.head.yRot = pNetHeadYaw * ((float) Math.PI / 180F);
//        this.head.xRot = pHeadPitch * ((float) Math.PI / 180F);
//    }
//
//	@Override
//	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
//		whole.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
//	}
//
//    @Override
//    public ModelPart root() {
//        return whole;
//    }
//}
//
//