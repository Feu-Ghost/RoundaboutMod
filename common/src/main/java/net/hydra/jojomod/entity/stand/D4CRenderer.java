package net.hydra.jojomod.entity.stand;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.entity.client.ModEntityRendererClient;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class D4CRenderer extends StandRenderer<D4CEntity> {

    private static final ResourceLocation MANGA_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/d4c.png");
    public D4CRenderer(EntityRendererProvider.Context context) {
        super(context, new D4CModel<>(context.bakeLayer(ModEntityRendererClient.MAGICIANS_RED_LAYER)), 0.f);
    }

    @Override public ResourceLocation getTextureLocation(D4CEntity entity) { return MANGA_SKIN; }

    @Override
    public void render(D4CEntity mobEntity, float f, float g, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i) {
        float factor = 0.5F + (mobEntity.getSizePercent()/2);
        if (mobEntity.isBaby()) {
            matrixStack.scale(0.5f*factor, 0.5f*factor, 0.5f*factor);
        } else {
            matrixStack.scale(0.87f * factor, 0.87f * factor, 0.87f * factor);
        }
        super.render(mobEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    @Override
    protected @Nullable RenderType getRenderType(D4CEntity entity, boolean shouldRender, boolean translucent, boolean showOutline) {
        return super.getRenderType(entity, shouldRender, translucent, showOutline);
    }
}
