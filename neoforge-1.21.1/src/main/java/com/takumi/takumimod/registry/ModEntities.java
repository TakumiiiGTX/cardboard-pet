package com.takumi.takumimod.registry;

import com.takumi.takumimod.TakumiMod;
import com.takumi.takumimod.entity.CardboardBoxEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModEntities
{
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(Registries.ENTITY_TYPE, TakumiMod.MODID);

    public static final DeferredHolder<EntityType<?>, EntityType<CardboardBoxEntity>> CARDBOARD_BOX =
            ENTITY_TYPES.register("cardboard_box", () -> EntityType.Builder.of(CardboardBoxEntity::new, MobCategory.CREATURE)
                    .sized(0.8F, 1.5F)
                    .clientTrackingRange(10)
                    .build("cardboard_box"));

    public static void registerAttributes(EntityAttributeCreationEvent event)
    {
        event.put(CARDBOARD_BOX.get(), CardboardBoxEntity.createAttributes().build());
    }
}
