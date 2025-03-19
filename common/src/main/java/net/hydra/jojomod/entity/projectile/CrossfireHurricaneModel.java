package net.hydra.jojomod.entity.projectile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

import javax.swing.text.html.parser.Entity;

public class CrossfireHurricaneModel<T extends CrossfireHurricaneEntity> extends HierarchicalModel<T>  {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(Roundabout.MOD_ID, "crossfire_hurricane"), "main");
    private final ModelPart Fire_Ankh;
    private final ModelPart Square;
    private final ModelPart Root;

    public CrossfireHurricaneModel(ModelPart root) {
        super(RenderType::entityTranslucent);
        this.Root = root;
        this.Fire_Ankh = root.getChild("Fire_Ankh");
        this.Square = this.Fire_Ankh.getChild("Square");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition Fire_Ankh = partdefinition.addOrReplaceChild("Fire_Ankh", CubeListBuilder.create().texOffs(0, 6).addBox(-1.0F, -7.0F, -1.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.001F))
                .texOffs(0, 0).addBox(-5.0F, -8.0F, -1.0F, 9.0F, 1.0F, 1.0F, new CubeDeformation(0.001F)), PartPose.offset(0.5F, 24.0F, 0.5F));

        PartDefinition Square = Fire_Ankh.addOrReplaceChild("Square", CubeListBuilder.create(), PartPose.offset(-0.5F, -10.6F, -0.5F));

        PartDefinition cube_r1 = Square.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 4).addBox(-2.5F, -2.5F, -0.5F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.7854F));

        PartDefinition cube_r2 = Square.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(4, 6).addBox(-2.5F, -1.5F, -0.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(8, 6).addBox(1.5F, -1.5F, -0.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 2).addBox(-2.5F, 1.5F, -0.5F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.7854F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        Fire_Ankh.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public ModelPart root() {
        return Root;
    }

}
