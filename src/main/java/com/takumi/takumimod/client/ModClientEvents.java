package com.takumi.takumimod.client;

import com.takumi.takumimod.TakumiMod;
import com.takumi.takumimod.client.model.CardboardBoxModel;
import com.takumi.takumimod.client.renderer.CardboardBoxRenderer;
import com.takumi.takumimod.registry.ModEntities;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = TakumiMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
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
