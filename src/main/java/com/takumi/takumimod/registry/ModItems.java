package com.takumi.takumimod.registry;

import com.takumi.takumimod.TakumiMod;
import com.takumi.takumimod.item.CardboardGiantWandItem;
import com.takumi.takumimod.item.CardboardSummonerItem;
import com.takumi.takumimod.item.CardboardSwordItem;
import com.takumi.takumimod.item.CardboardTiers;
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

    public static final RegistryObject<Item> CARDBOARD_SWORD_REINFORCED_STONE =
            TakumiMod.ITEMS.register("cardboard_sword_reinforced_stone",
                    () -> new CardboardSwordItem(Tiers.STONE, 3, -0.4F, new Item.Properties()));

    public static final RegistryObject<Item> CARDBOARD_SWORD_REINFORCED_DEEPSLATE =
            TakumiMod.ITEMS.register("cardboard_sword_reinforced_deepslate",
                    () -> new CardboardSwordItem(CardboardTiers.DEEPSLATE_REINFORCED, 3, -0.4F, new Item.Properties()));

    public static final RegistryObject<Item> CARDBOARD_SWORD_REINFORCED_IRON =
            TakumiMod.ITEMS.register("cardboard_sword_reinforced_iron",
                    () -> new CardboardSwordItem(Tiers.IRON, 3, -0.4F, new Item.Properties()));

    public static final RegistryObject<Item> CARDBOARD_SWORD_REINFORCED_GOLD =
            TakumiMod.ITEMS.register("cardboard_sword_reinforced_gold",
                    () -> new CardboardSwordItem(CardboardTiers.GOLD_REINFORCED, 3, -0.4F, new Item.Properties()));

    public static final RegistryObject<Item> CARDBOARD_SWORD_REINFORCED_DIAMOND =
            TakumiMod.ITEMS.register("cardboard_sword_reinforced_diamond",
                    () -> new CardboardSwordItem(CardboardTiers.DIAMOND_REINFORCED, 3, -0.4F, new Item.Properties()));

    public static final RegistryObject<Item> CARDBOARD_SWORD_REINFORCED_EMERALD =
            TakumiMod.ITEMS.register("cardboard_sword_reinforced_emerald",
                    () -> new CardboardSwordItem(CardboardTiers.EMERALD_REINFORCED, 3, -0.4F, new Item.Properties()));

    public static final RegistryObject<Item> CARDBOARD_SWORD_REINFORCED_NETHERITE =
            TakumiMod.ITEMS.register("cardboard_sword_reinforced_netherite",
                    () -> new CardboardSwordItem(CardboardTiers.NETHERITE_REINFORCED, 3, -0.4F, new Item.Properties()));

    public static final RegistryObject<Item> CARDBOARD_SWORD_REINFORCED_OBSIDIAN =
            TakumiMod.ITEMS.register("cardboard_sword_reinforced_obsidian",
                    () -> new CardboardSwordItem(CardboardTiers.OBSIDIAN_REINFORCED, 3, -0.4F, new Item.Properties()));

    public static final RegistryObject<Item> CARDBOARD_SWORD_REINFORCED_CRYING_OBSIDIAN =
            TakumiMod.ITEMS.register("cardboard_sword_reinforced_crying_obsidian",
                    () -> new CardboardSwordItem(CardboardTiers.CRYING_OBSIDIAN_REINFORCED, 3, -0.4F, new Item.Properties()));

    public static final RegistryObject<Item> CARDBOARD_SWORD_REINFORCED_SCULK =
            TakumiMod.ITEMS.register("cardboard_sword_reinforced_sculk",
                    () -> new CardboardSwordItem(CardboardTiers.SCULK_REINFORCED, 3, -0.4F, new Item.Properties()));

    public static final RegistryObject<Item> CARDBOARD_SWORD_REINFORCED_END =
            TakumiMod.ITEMS.register("cardboard_sword_reinforced_end",
                    () -> new CardboardSwordItem(CardboardTiers.END_REINFORCED, 3, -0.4F, new Item.Properties()));

    public static final RegistryObject<Item> CARDBOARD_SWORD_REINFORCED_NETHER_STAR =
            TakumiMod.ITEMS.register("cardboard_sword_reinforced_nether_star",
                    () -> new CardboardSwordItem(CardboardTiers.NETHER_STAR_REINFORCED, 3, -0.4F, new Item.Properties()));

    public static void init()
    {
        // Forces class loading so the RegistryObject above is registered.
    }
}
