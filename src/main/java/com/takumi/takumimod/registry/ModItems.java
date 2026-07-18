package com.takumi.takumimod.registry;

import com.takumi.takumimod.TakumiMod;
import com.takumi.takumimod.item.CardboardGiantWandItem;
import com.takumi.takumimod.item.CardboardSummonerItem;
import com.takumi.takumimod.item.CardboardSwordItem;
import com.takumi.takumimod.item.CardboardWhistleItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tiers;
import net.minecraftforge.registries.RegistryObject;

public class ModItems
{
    public static final RegistryObject<Item> CARDBOARD_SUMMONER =
            TakumiMod.ITEMS.register("cardboard_summoner",
                    () -> new CardboardSummonerItem(new Item.Properties().stacksTo(16)));

    public static final RegistryObject<Item> CARDBOARD_WHISTLE =
            TakumiMod.ITEMS.register("cardboard_whistle",
                    () -> new CardboardWhistleItem(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> CARDBOARD_GIANT_WAND =
            TakumiMod.ITEMS.register("cardboard_giant_wand",
                    () -> new CardboardGiantWandItem(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> CARDBOARD_SWORD =
            TakumiMod.ITEMS.register("cardboard_sword",
                    () -> new CardboardSwordItem(Tiers.WOOD, 3, -0.4F, new Item.Properties()));

    public static void init()
    {
        // Forces class loading so the RegistryObject above is registered.
    }
}
