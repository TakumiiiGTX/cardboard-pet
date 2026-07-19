package com.takumi.takumimod;

import com.takumi.takumimod.client.renderer.CardboardSwordItemRenderer;
import com.takumi.takumimod.registry.ModItems;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;

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

    @SubscribeEvent
    static void onRegisterClientExtensions(RegisterClientExtensionsEvent event)
    {
        CardboardSwordItemRenderer renderer = new CardboardSwordItemRenderer();
        IClientItemExtensions extensions = new IClientItemExtensions()
        {
            @Override
            public net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer getCustomRenderer()
            {
                return renderer;
            }
        };
        event.registerItem(extensions,
                ModItems.CARDBOARD_SWORD.get(),
                ModItems.CARDBOARD_SWORD_REINFORCED_STONE.get(),
                ModItems.CARDBOARD_SWORD_REINFORCED_IRON.get(),
                ModItems.CARDBOARD_SWORD_REINFORCED_GOLD.get(),
                ModItems.CARDBOARD_SWORD_REINFORCED_DIAMOND.get(),
                ModItems.CARDBOARD_SWORD_REINFORCED_EMERALD.get(),
                ModItems.CARDBOARD_SWORD_REINFORCED_NETHERITE.get(),
                ModItems.CARDBOARD_SWORD_REINFORCED_DEEPSLATE.get(),
                ModItems.CARDBOARD_SWORD_REINFORCED_OBSIDIAN.get(),
                ModItems.CARDBOARD_SWORD_REINFORCED_CRYING_OBSIDIAN.get(),
                ModItems.CARDBOARD_SWORD_REINFORCED_SCULK.get(),
                ModItems.CARDBOARD_SWORD_REINFORCED_END.get(),
                ModItems.CARDBOARD_SWORD_REINFORCED_NETHER_STAR.get());
    }
}
