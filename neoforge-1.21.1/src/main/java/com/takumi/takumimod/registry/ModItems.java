package com.takumi.takumimod.registry;

import com.takumi.takumimod.TakumiMod;
import com.takumi.takumimod.item.CardboardGiantWandItem;
import com.takumi.takumimod.item.CardboardSummonerItem;
import com.takumi.takumimod.item.CardboardTiers;
import com.takumi.takumimod.item.CardboardWhistleItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import net.neoforged.neoforge.registries.DeferredItem;

public class ModItems
{
    public static final DeferredItem<Item> CARDBOARD_SUMMONER =
            TakumiMod.ITEMS.register("cardboard_summoner",
                    () -> new CardboardSummonerItem(new Item.Properties().stacksTo(16)));

    public static final DeferredItem<Item> CARDBOARD_WHISTLE =
            TakumiMod.ITEMS.register("cardboard_whistle",
                    () -> new CardboardWhistleItem(new Item.Properties().stacksTo(1)));

    public static final DeferredItem<Item> CARDBOARD_GIANT_WAND =
            TakumiMod.ITEMS.register("cardboard_giant_wand",
                    () -> new CardboardGiantWandItem(new Item.Properties().stacksTo(1)));

    public static final DeferredItem<Item> CARDBOARD_SWORD =
            TakumiMod.ITEMS.register("cardboard_sword",
                    () -> new SwordItem(Tiers.WOOD, new Item.Properties().attributes(SwordItem.createAttributes(Tiers.WOOD, 3, -0.4F))));

    public static final DeferredItem<Item> CARDBOARD_SWORD_REINFORCED_STONE =
            TakumiMod.ITEMS.register("cardboard_sword_reinforced_stone",
                    () -> new SwordItem(Tiers.STONE, new Item.Properties().attributes(SwordItem.createAttributes(Tiers.STONE, 3, -0.4F))));

    public static final DeferredItem<Item> CARDBOARD_SWORD_REINFORCED_DEEPSLATE =
            TakumiMod.ITEMS.register("cardboard_sword_reinforced_deepslate",
                    () -> new SwordItem(CardboardTiers.DEEPSLATE_REINFORCED,
                            new Item.Properties().attributes(SwordItem.createAttributes(CardboardTiers.DEEPSLATE_REINFORCED, 3, -0.4F))));

    public static final DeferredItem<Item> CARDBOARD_SWORD_REINFORCED_IRON =
            TakumiMod.ITEMS.register("cardboard_sword_reinforced_iron",
                    () -> new SwordItem(Tiers.IRON, new Item.Properties().attributes(SwordItem.createAttributes(Tiers.IRON, 3, -0.4F))));

    public static final DeferredItem<Item> CARDBOARD_SWORD_REINFORCED_GOLD =
            TakumiMod.ITEMS.register("cardboard_sword_reinforced_gold",
                    () -> new SwordItem(CardboardTiers.GOLD_REINFORCED,
                            new Item.Properties().attributes(SwordItem.createAttributes(CardboardTiers.GOLD_REINFORCED, 3, -0.4F))));

    public static final DeferredItem<Item> CARDBOARD_SWORD_REINFORCED_DIAMOND =
            TakumiMod.ITEMS.register("cardboard_sword_reinforced_diamond",
                    () -> new SwordItem(CardboardTiers.DIAMOND_REINFORCED,
                            new Item.Properties().attributes(SwordItem.createAttributes(CardboardTiers.DIAMOND_REINFORCED, 3, -0.4F))));

    public static final DeferredItem<Item> CARDBOARD_SWORD_REINFORCED_EMERALD =
            TakumiMod.ITEMS.register("cardboard_sword_reinforced_emerald",
                    () -> new SwordItem(CardboardTiers.EMERALD_REINFORCED,
                            new Item.Properties().attributes(SwordItem.createAttributes(CardboardTiers.EMERALD_REINFORCED, 3, -0.4F))));

    public static final DeferredItem<Item> CARDBOARD_SWORD_REINFORCED_NETHERITE =
            TakumiMod.ITEMS.register("cardboard_sword_reinforced_netherite",
                    () -> new SwordItem(CardboardTiers.NETHERITE_REINFORCED,
                            new Item.Properties().attributes(SwordItem.createAttributes(CardboardTiers.NETHERITE_REINFORCED, 3, -0.4F))));

    public static final DeferredItem<Item> CARDBOARD_SWORD_REINFORCED_OBSIDIAN =
            TakumiMod.ITEMS.register("cardboard_sword_reinforced_obsidian",
                    () -> new SwordItem(CardboardTiers.OBSIDIAN_REINFORCED,
                            new Item.Properties().attributes(SwordItem.createAttributes(CardboardTiers.OBSIDIAN_REINFORCED, 3, -0.4F))));

    public static final DeferredItem<Item> CARDBOARD_SWORD_REINFORCED_CRYING_OBSIDIAN =
            TakumiMod.ITEMS.register("cardboard_sword_reinforced_crying_obsidian",
                    () -> new SwordItem(CardboardTiers.CRYING_OBSIDIAN_REINFORCED,
                            new Item.Properties().attributes(SwordItem.createAttributes(CardboardTiers.CRYING_OBSIDIAN_REINFORCED, 3, -0.4F))));

    public static final DeferredItem<Item> CARDBOARD_SWORD_REINFORCED_SCULK =
            TakumiMod.ITEMS.register("cardboard_sword_reinforced_sculk",
                    () -> new SwordItem(CardboardTiers.SCULK_REINFORCED,
                            new Item.Properties().attributes(SwordItem.createAttributes(CardboardTiers.SCULK_REINFORCED, 3, -0.4F))));

    public static final DeferredItem<Item> CARDBOARD_SWORD_REINFORCED_END =
            TakumiMod.ITEMS.register("cardboard_sword_reinforced_end",
                    () -> new SwordItem(CardboardTiers.END_REINFORCED,
                            new Item.Properties().attributes(SwordItem.createAttributes(CardboardTiers.END_REINFORCED, 3, -0.4F))));

    public static final DeferredItem<Item> CARDBOARD_SWORD_REINFORCED_NETHER_STAR =
            TakumiMod.ITEMS.register("cardboard_sword_reinforced_nether_star",
                    () -> new SwordItem(CardboardTiers.NETHER_STAR_REINFORCED,
                            new Item.Properties().attributes(SwordItem.createAttributes(CardboardTiers.NETHER_STAR_REINFORCED, 3, -0.4F))));

    public static void init()
    {
        // Forces class loading so the DeferredItems above are registered.
    }
}
