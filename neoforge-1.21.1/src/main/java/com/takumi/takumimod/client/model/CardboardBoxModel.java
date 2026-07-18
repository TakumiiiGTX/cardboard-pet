package com.takumi.takumimod.client.model;

import com.takumi.takumimod.TakumiMod;
import com.takumi.takumimod.entity.CardboardBoxEntity;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class CardboardBoxModel extends HierarchicalModel<CardboardBoxEntity>
{
    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(TakumiMod.MODID, "cardboard_box"), "main");

    private final ModelPart root;
    private final ModelPart body;
    private final ModelPart flapLeft;
    private final ModelPart flapRight;
    private final ModelPart rightArm;
    private final ModelPart leftArm;
    private final ModelPart rightLeg;
    private final ModelPart leftLeg;

    public CardboardBoxModel(ModelPart root)
    {
        this.root = root;
        this.body = root.getChild("body");
        this.flapLeft = body.getChild("flap_left");
        this.flapRight = body.getChild("flap_right");
        this.rightArm = root.getChild("right_arm");
        this.leftArm = root.getChild("left_arm");
        this.rightLeg = root.getChild("right_leg");
        this.leftLeg = root.getChild("left_leg");
    }

    public static LayerDefinition createBodyLayer()
    {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition partdefinition = mesh.getRoot();

        PartDefinition body = partdefinition.addOrReplaceChild("body",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-6.0F, -12.0F, -6.0F, 12.0F, 12.0F, 12.0F),
                PartPose.offset(0.0F, 20.0F, 0.0F));

        body.addOrReplaceChild("flap_left",
                CubeListBuilder.create()
                        .texOffs(0, 25)
                        .addBox(-6.0F, -0.5F, 0.0F, 6.0F, 1.0F, 12.0F),
                PartPose.offsetAndRotation(0.0F, -12.0F, -6.0F, 0.0F, 0.0F, -0.5F));

        body.addOrReplaceChild("flap_right",
                CubeListBuilder.create()
                        .texOffs(0, 39)
                        .addBox(0.0F, -0.5F, 0.0F, 6.0F, 1.0F, 12.0F),
                PartPose.offsetAndRotation(0.0F, -12.0F, -6.0F, 0.0F, 0.0F, 0.5F));

        partdefinition.addOrReplaceChild("right_arm",
                CubeListBuilder.create()
                        .texOffs(48, 25)
                        .addBox(-1.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F),
                PartPose.offset(-7.0F, 10.0F, 0.0F));

        partdefinition.addOrReplaceChild("left_arm",
                CubeListBuilder.create()
                        .texOffs(56, 25)
                        .addBox(-1.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F),
                PartPose.offset(7.0F, 10.0F, 0.0F));

        partdefinition.addOrReplaceChild("right_leg",
                CubeListBuilder.create()
                        .texOffs(48, 40)
                        .addBox(-1.5F, 0.0F, -1.5F, 3.0F, 4.0F, 3.0F),
                PartPose.offset(-2.5F, 20.0F, 0.0F));

        partdefinition.addOrReplaceChild("left_leg",
                CubeListBuilder.create()
                        .texOffs(48, 48)
                        .addBox(-1.5F, 0.0F, -1.5F, 3.0F, 4.0F, 3.0F),
                PartPose.offset(2.5F, 20.0F, 0.0F));

        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override
    public void setupAnim(CardboardBoxEntity entity, float limbSwing, float limbSwingAmount,
                           float ageInTicks, float netHeadYaw, float headPitch)
    {
        this.rightLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        this.leftLeg.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
        this.rightArm.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.2F * limbSwingAmount;
        this.leftArm.xRot = Mth.cos(limbSwing * 0.6662F) * 1.2F * limbSwingAmount;

        float attackTime = entity.getAttackAnim(0.0F);
        if (attackTime > 0.0F)
        {
            float swing = Mth.sin(attackTime * (float) Math.PI);
            this.rightArm.xRot = -1.8F * swing;
        }

        this.flapLeft.zRot = -0.5F - Mth.cos(ageInTicks * 0.08F) * 0.05F;
        this.flapRight.zRot = 0.5F + Mth.cos(ageInTicks * 0.08F) * 0.05F;
    }

    @Override
    public ModelPart root()
    {
        return this.root;
    }
}
