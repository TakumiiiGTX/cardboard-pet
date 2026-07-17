package com.takumi.takumimod.registry;

import com.takumi.takumimod.TakumiMod;
import com.takumi.takumimod.entity.CardboardBoxEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities
{
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, TakumiMod.MODID);

    public static final RegistryObject<EntityType<CardboardBoxEntity>> CARDBOARD_BOX =
            ENTITY_TYPES.register("cardboard_box", () -> EntityType.Builder.of(CardboardBoxEntity::new, MobCategory.CREATURE)
                    .sized(0.8F, 1.5F)
                    .clientTrackingRange(10)
                    .build("cardboard_box"));

    public static void registerAttributes(EntityAttributeCreationEvent event)
    {
        event.put(CARDBOARD_BOX.get(), CardboardBoxEntity.createAttributes().build());
    }
}
