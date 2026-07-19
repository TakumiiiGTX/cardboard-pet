package com.takumi.takumimod.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.takumi.takumimod.TakumiMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

/**
 * Draws the cardboard sword as two flat overlapping quads: an opaque, fully-lit "glow"
 * texture behind, and the normal blade texture (now semi-transparent) in front, so the
 * glow color shows through the blade like it's glassy/energized.
 */
public class CardboardSwordItemRenderer extends BlockEntityWithoutLevelRenderer
{
    private static final ResourceLocation GLOW_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(TakumiMod.MODID, "textures/item/cardboard_sword_glow.png");

    // Every cardboard_sword* variant has a texture file named exactly after its own item id
    // (e.g. cardboard_sword_reinforced_iron -> textures/item/cardboard_sword_reinforced_iron.png),
    // so the base texture can be derived straight from the registry name instead of maintaining
    // a hardcoded constant + branch per tier.
    private static ResourceLocation resolveBaseTexture(ItemStack stack)
    {
        ResourceLocation itemId = BuiltInRegistries.ITEM.getKey(stack.getItem());
        return ResourceLocation.fromNamespaceAndPath(TakumiMod.MODID, "textures/item/" + itemId.getPath() + ".png");
    }

    public CardboardSwordItemRenderer()
    {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
    }

    // Vanilla's flat "generated" item quads sit at z=[7.5,8.5]/16 (i.e. centered on 0.5 in the
    // 0-1 block space), not z=0. Matching that keeps this item's position/depth consistent with
    // every other flat item once ItemRenderer's -0.5,-0.5,-0.5 recenter is applied on top.
    // The glow sits slightly behind the (now translucent) blade so it can show through it.
    private static final float GLOW_Z = 0.501F;
    private static final float BASE_Z = 0.499F;

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack,
                              MultiBufferSource buffer, int light, int overlay)
    {
        poseStack.pushPose();

        VertexConsumer glowConsumer = buffer.getBuffer(RenderType.entityTranslucentEmissive(GLOW_TEXTURE));
        renderFlatQuad(poseStack, glowConsumer, LightTexture.FULL_BRIGHT, overlay, GLOW_Z);

        VertexConsumer baseConsumer = buffer.getBuffer(RenderType.entityTranslucent(resolveBaseTexture(stack)));
        renderFlatQuad(poseStack, baseConsumer, light, overlay, BASE_Z);

        poseStack.popPose();
    }

    // Both entityTranslucent and entityTranslucentEmissive disable back-face culling, so a
    // single face is already visible from either side; no need to draw a second back face.
    private static void renderFlatQuad(PoseStack poseStack, VertexConsumer consumer, int light, int overlay, float z)
    {
        PoseStack.Pose pose = poseStack.last();
        quadFace(consumer, pose, light, overlay, z, false);
    }

    private static void quadFace(VertexConsumer consumer, PoseStack.Pose pose, int light, int overlay, float z, boolean back)
    {
        float nz = back ? 1.0F : -1.0F;
        // v=0 is the top row of the source PNG, so it must map to the top (y=1) of the quad.
        if (!back)
        {
            vertex(consumer, pose, 0.0F, 1.0F, z, 0.0F, 0.0F, overlay, light, nz);
            vertex(consumer, pose, 1.0F, 1.0F, z, 1.0F, 0.0F, overlay, light, nz);
            vertex(consumer, pose, 1.0F, 0.0F, z, 1.0F, 1.0F, overlay, light, nz);
            vertex(consumer, pose, 0.0F, 0.0F, z, 0.0F, 1.0F, overlay, light, nz);
        }
        else
        {
            vertex(consumer, pose, 0.0F, 0.0F, z, 0.0F, 1.0F, overlay, light, nz);
            vertex(consumer, pose, 1.0F, 0.0F, z, 1.0F, 1.0F, overlay, light, nz);
            vertex(consumer, pose, 1.0F, 1.0F, z, 1.0F, 0.0F, overlay, light, nz);
            vertex(consumer, pose, 0.0F, 1.0F, z, 0.0F, 0.0F, overlay, light, nz);
        }
    }

    private static void vertex(VertexConsumer consumer, PoseStack.Pose pose, float x, float y, float z, float u, float v,
                                int overlay, int light, float nz)
    {
        consumer.addVertex(pose.pose(), x, y, z)
                .setColor(1.0F, 1.0F, 1.0F, 1.0F)
                .setUv(u, v)
                .setOverlay(overlay)
                .setUv2(light & 65535, light >> 16 & 65535)
                .setNormal(pose, 0.0F, 0.0F, nz);
    }
}
