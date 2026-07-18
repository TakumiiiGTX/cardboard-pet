package com.takumi.takumimod.item;

import com.takumi.takumimod.entity.CardboardBoxEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class CardboardWhistleItem extends Item
{
    public CardboardWhistleItem(Properties properties)
    {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand)
    {
        ItemStack stack = player.getItemInHand(hand);
        if (level.isClientSide || !(level instanceof ServerLevel serverLevel))
        {
            return InteractionResultHolder.success(stack);
        }

        int called = 0;
        for (Entity entity : serverLevel.getEntities().getAll())
        {
            if (entity instanceof CardboardBoxEntity box && box.isOwnedBy(player))
            {
                Vec3 offset = new Vec3((player.getRandom().nextDouble() - 0.5D) * 3.0D, 0.0D,
                        (player.getRandom().nextDouble() - 0.5D) * 3.0D);
                Vec3 target = player.position().add(offset);
                box.teleportTo(target.x, target.y, target.z);
                box.setDeltaMovement(Vec3.ZERO);
                box.resetFallDistance();
                called++;
            }
        }

        if (called > 0)
        {
            level.playSound(null, player.blockPosition(), SoundEvents.NOTE_BLOCK_PLING.value(), SoundSource.PLAYERS, 1.0F, 1.5F);
        }

        return InteractionResultHolder.success(stack);
    }
}
