package com.takumi.takumimod.event;

import com.takumi.takumimod.TakumiMod;
import com.takumi.takumimod.entity.CardboardBoxEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
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

        MinecraftServer server = serverLevel.getServer();
        for (ServerLevel loadedLevel : server.getAllLevels())
        {
            for (Entity entity : loadedLevel.getEntities().getAll())
            {
                if (entity instanceof CardboardBoxEntity box && box.isOwnedBy(player))
                {
                    box.discard();
                }
            }
        }
    }
}
