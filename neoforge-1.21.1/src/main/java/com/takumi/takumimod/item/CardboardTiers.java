package com.takumi.takumimod.item;

import com.google.common.base.Suppliers;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

/**
 * Custom tool tiers for the cardboard sword's upgrade materials, since vanilla has no
 * built-in tier for deepslate. Deepslate reinforcement trades a bit of attack damage
 * (0.5 less than the stone-reinforced variant) for much higher durability.
 */
public enum CardboardTiers implements Tier
{
    DEEPSLATE_REINFORCED(300, 4.0F, 0.5F, 5, () -> Ingredient.of(Items.COBBLED_DEEPSLATE)),
    GOLD_REINFORCED(32, 4.0F, 5.0F, 22, () -> Ingredient.of(Items.GOLD_INGOT)),
    DIAMOND_REINFORCED(1561, 4.0F, 6.0F, 10, () -> Ingredient.of(Items.DIAMOND)),
    EMERALD_REINFORCED(1800, 4.0F, 8.0F, 12, () -> Ingredient.of(Items.EMERALD)),
    NETHERITE_REINFORCED(2031, 4.0F, 12.0F, 15, () -> Ingredient.of(Items.NETHERITE_INGOT)),
    OBSIDIAN_REINFORCED(500, 4.0F, 1.0F, 8, () -> Ingredient.of(Items.OBSIDIAN)),
    CRYING_OBSIDIAN_REINFORCED(600, 4.0F, 3.0F, 9, () -> Ingredient.of(Items.CRYING_OBSIDIAN)),
    SCULK_REINFORCED(900, 4.0F, 5.0F, 20, () -> Ingredient.of(Items.ECHO_SHARD)),
    END_REINFORCED(1200, 4.0F, 14.0F, 10, () -> Ingredient.of(Items.END_STONE)),
    NETHER_STAR_REINFORCED(3000, 4.0F, 20.0F, 25, () -> Ingredient.of(Items.NETHER_STAR));

    private final int uses;
    private final float speed;
    private final float attackDamageBonus;
    private final int enchantmentValue;
    private final Supplier<Ingredient> repairIngredient;

    CardboardTiers(int uses, float speed, float attackDamageBonus, int enchantmentValue, Supplier<Ingredient> repairIngredient)
    {
        this.uses = uses;
        this.speed = speed;
        this.attackDamageBonus = attackDamageBonus;
        this.enchantmentValue = enchantmentValue;
        this.repairIngredient = Suppliers.memoize(repairIngredient::get);
    }

    @Override
    public int getUses()
    {
        return this.uses;
    }

    @Override
    public float getSpeed()
    {
        return this.speed;
    }

    @Override
    public float getAttackDamageBonus()
    {
        return this.attackDamageBonus;
    }

    @Override
    public TagKey<Block> getIncorrectBlocksForDrops()
    {
        return BlockTags.INCORRECT_FOR_STONE_TOOL;
    }

    @Override
    public int getEnchantmentValue()
    {
        return this.enchantmentValue;
    }

    @Override
    public Ingredient getRepairIngredient()
    {
        return this.repairIngredient.get();
    }
}
