package com.takumi.takumimod.event;

import com.takumi.takumimod.TakumiMod;
import com.takumi.takumimod.entity.CardboardBoxEntity;
import com.takumi.takumimod.registry.ModItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

@EventBusSubscriber(modid = TakumiMod.MODID)
public class ModEvents
{
    // Stored under Player.PERSISTED_NBT_TAG so it survives the death->respawn entity swap
    // regardless of the keepInventory gamerule (which would otherwise sweep up anything we
    // hand the player directly during LivingDeathEvent).
    private static final String PENDING_REFUNDS_KEY = "PendingCardboardSummoners";

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

                    // Refund a summoner so the owner isn't permanently out one for a box that
                    // only vanished because they died, not because it was defeated.
                    addPendingSummonerRefund(player);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event)
    {
        Player player = event.getEntity();
        if (player.level().isClientSide)
        {
            return;
        }

        CompoundTag persisted = player.getPersistentData().getCompound(Player.PERSISTED_NBT_TAG);
        int pending = persisted.getInt(PENDING_REFUNDS_KEY);
        if (pending <= 0)
        {
            return;
        }

        for (int i = 0; i < pending; i++)
        {
            ItemStack refund = new ItemStack(ModItems.CARDBOARD_SUMMONER.get());
            if (!player.getInventory().add(refund))
            {
                player.drop(refund, false);
            }
        }

        persisted.remove(PENDING_REFUNDS_KEY);
        player.getPersistentData().put(Player.PERSISTED_NBT_TAG, persisted);
    }

    private static void addPendingSummonerRefund(Player player)
    {
        CompoundTag persisted = player.getPersistentData().getCompound(Player.PERSISTED_NBT_TAG);
        persisted.putInt(PENDING_REFUNDS_KEY, persisted.getInt(PENDING_REFUNDS_KEY) + 1);
        player.getPersistentData().put(Player.PERSISTED_NBT_TAG, persisted);
    }
}
