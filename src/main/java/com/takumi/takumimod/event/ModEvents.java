package com.takumi.takumimod.event;

import com.takumi.takumimod.TakumiMod;
import com.takumi.takumimod.entity.CardboardBoxEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = TakumiMod.MODID)
public class ModEvents
{
    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event)
    {
        if (!(event.getEntity() instanceof Player player))
        {
            return;
        }

        Level level = player.level();
        if (level.isClientSide || !(level instanceof ServerLevel serverLevel))
        {
            return;
        }

        AABB everywhere = new AABB(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY,
                Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);

        MinecraftServer server = serverLevel.getServer();
        for (ServerLevel loadedLevel : server.getAllLevels())
        {
            for (CardboardBoxEntity box : loadedLevel.getEntitiesOfClass(CardboardBoxEntity.class, everywhere, b -> b.isOwnedBy(player)))
            {
                box.discard();
            }
        }
    }
}
