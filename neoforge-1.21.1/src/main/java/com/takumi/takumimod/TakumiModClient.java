package com.takumi.takumimod;

import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

// This class will not load on dedicated servers. Accessing client side code from here is safe.
@Mod(value = TakumiMod.MODID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = TakumiMod.MODID, value = Dist.CLIENT)
public class TakumiModClient
{
    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event)
    {
        // Some client setup code
        TakumiMod.LOGGER.info("HELLO FROM CLIENT SETUP");
        TakumiMod.LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
    }
}
