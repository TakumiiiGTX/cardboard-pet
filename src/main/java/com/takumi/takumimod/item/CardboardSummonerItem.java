package com.takumi.takumimod.item;

import com.takumi.takumimod.entity.CardboardBoxEntity;
import com.takumi.takumimod.registry.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;

public class CardboardSummonerItem extends Item
{
    public CardboardSummonerItem(Properties properties)
    {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context)
    {
        Level level = context.getLevel();
        if (level.isClientSide)
        {
            return InteractionResult.SUCCESS;
        }

        BlockPos spawnPos = context.getClickedPos().relative(context.getClickedFace());
        CardboardBoxEntity box = ModEntities.CARDBOARD_BOX.get().create(level);
        if (box == null)
        {
            return InteractionResult.FAIL;
        }

        box.moveTo(spawnPos.getX() + 0.5, spawnPos.getY(), spawnPos.getZ() + 0.5,
                context.getRotation(), 0.0F);
        box.finalizeSpawn((ServerLevel) level, level.getCurrentDifficultyAt(spawnPos),
                MobSpawnType.MOB_SUMMONED, null, null);

        Player player = context.getPlayer();
        if (player != null)
        {
            box.setOwnerUUID(player.getUUID());
        }

        level.addFreshEntity(box);
        level.playSound(null, spawnPos, SoundEvents.WOOL_PLACE, SoundSource.PLAYERS, 1.0F, 1.0F);

        if (player == null || !player.getAbilities().instabuild)
        {
            context.getItemInHand().shrink(1);
        }

        return InteractionResult.CONSUME;
    }
}
