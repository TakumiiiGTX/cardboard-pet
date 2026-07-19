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
    DEEPSLATE_REINFORCED(300, 4.0F, 0.5F, 5, () -> Ingredient.of(Items.COBBLED_DEEPSLATE));

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
