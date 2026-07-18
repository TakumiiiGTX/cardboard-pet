package com.takumi.takumimod.client;

import com.takumi.takumimod.TakumiMod;
import com.takumi.takumimod.client.model.CardboardBoxModel;
import com.takumi.takumimod.client.renderer.CardboardBoxRenderer;
import com.takumi.takumimod.registry.ModEntities;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(modid = TakumiMod.MODID, value = Dist.CLIENT)
public class ModClientEvents
{
    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event)
    {
        event.registerLayerDefinition(CardboardBoxModel.LAYER_LOCATION, CardboardBoxModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event)
    {
        event.registerEntityRenderer(ModEntities.CARDBOARD_BOX.get(), CardboardBoxRenderer::new);
    }
}
