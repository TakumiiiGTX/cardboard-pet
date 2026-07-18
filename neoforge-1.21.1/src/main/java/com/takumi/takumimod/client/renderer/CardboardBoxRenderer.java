package com.takumi.takumimod.client.renderer;

import com.takumi.takumimod.TakumiMod;
import com.takumi.takumimod.client.model.CardboardBoxModel;
import com.takumi.takumimod.entity.CardboardBoxEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;

public class CardboardBoxRenderer extends MobRenderer<CardboardBoxEntity, CardboardBoxModel>
{
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(TakumiMod.MODID, "textures/entity/cardboard_box.png");

    public CardboardBoxRenderer(EntityRendererProvider.Context context)
    {
        super(context, new CardboardBoxModel(context.bakeLayer(CardboardBoxModel.LAYER_LOCATION)), 0.5F);
        this.addLayer(new ItemInHandLayer<>(this, context.getItemInHandRenderer()));
    }

    @Override
    public ResourceLocation getTextureLocation(CardboardBoxEntity entity)
    {
        return TEXTURE;
    }
}
