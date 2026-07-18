package com.takumi.takumimod.item;

import com.takumi.takumimod.client.renderer.CardboardSwordItemRenderer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import java.util.function.Consumer;

public class CardboardSwordItem extends SwordItem
{
    public CardboardSwordItem(Tier tier, int attackDamageModifier, float attackSpeedModifier, Properties properties)
    {
        super(tier, attackDamageModifier, attackSpeedModifier, properties);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer)
    {
        consumer.accept(new IClientItemExtensions()
        {
            private final CardboardSwordItemRenderer renderer = new CardboardSwordItemRenderer();

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer()
            {
                return this.renderer;
            }
        });
    }
}
