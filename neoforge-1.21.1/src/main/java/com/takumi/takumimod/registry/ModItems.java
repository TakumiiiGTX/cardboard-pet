package com.takumi.takumimod.registry;

import com.takumi.takumimod.TakumiMod;
import com.takumi.takumimod.item.CardboardSummonerItem;
import com.takumi.takumimod.item.CardboardWhistleItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;

public class ModItems
{
    public static final DeferredItem<Item> CARDBOARD_SUMMONER =
            TakumiMod.ITEMS.register("cardboard_summoner",
                    () -> new CardboardSummonerItem(new Item.Properties().stacksTo(16)));

    public static final DeferredItem<Item> CARDBOARD_WHISTLE =
            TakumiMod.ITEMS.register("cardboard_whistle",
                    () -> new CardboardWhistleItem(new Item.Properties().stacksTo(1)));

    public static void init()
    {
        // Forces class loading so the DeferredItems above are registered.
    }
}
