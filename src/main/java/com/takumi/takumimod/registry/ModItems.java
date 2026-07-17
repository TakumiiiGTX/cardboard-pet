package com.takumi.takumimod.registry;

import com.takumi.takumimod.TakumiMod;
import com.takumi.takumimod.item.CardboardSummonerItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;

public class ModItems
{
    public static final RegistryObject<Item> CARDBOARD_SUMMONER =
            TakumiMod.ITEMS.register("cardboard_summoner",
                    () -> new CardboardSummonerItem(new Item.Properties().stacksTo(16)));

    public static void init()
    {
        // Forces class loading so the RegistryObject above is registered.
    }
}
