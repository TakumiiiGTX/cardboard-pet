package com.takumi.takumimod.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import com.takumi.takumimod.entity.goal.AssistOwnerTargetGoal;
import com.takumi.takumimod.entity.goal.DefendOwnerTargetGoal;
import com.takumi.takumimod.entity.goal.FlyAtTargetGoal;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;

import java.util.Optional;
import java.util.UUID;

public class CardboardBoxEntity extends PathfinderMob
{
    private static final EntityDataAccessor<Optional<UUID>> OWNER_UUID =
            SynchedEntityData.defineId(CardboardBoxEntity.class, EntityDataSerializers.OPTIONAL_UUID);
    private static final EntityDataAccessor<Float> SIZE_SCALE =
            SynchedEntityData.defineId(CardboardBoxEntity.class, EntityDataSerializers.FLOAT);

    public static final float GIANT_SCALE = 2.5F;

    public CardboardBoxEntity(EntityType<? extends CardboardBoxEntity> type, Level level)
    {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes()
    {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 40.0D)
                .add(Attributes.ATTACK_DAMAGE, 5.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.3D)
                .add(Attributes.FOLLOW_RANGE, 32.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.3D);
    }

    @Override
    protected void defineSynchedData()
    {
        super.defineSynchedData();
        this.entityData.define(OWNER_UUID, Optional.empty());
        this.entityData.define(SIZE_SCALE, 1.0F);
    }

    @Override
    protected void registerGoals()
    {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new FlyAtTargetGoal(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.2D, true));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, CardboardBoxEntity.class));
        this.targetSelector.addGoal(1, new DefendOwnerTargetGoal(this));
        this.targetSelector.addGoal(2, new AssistOwnerTargetGoal(this));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Mob.class, 10, true, false,
                entity -> entity instanceof Enemy));
    }

    @Override
    public boolean canAttack(LivingEntity target)
    {
        if (isOwnedBy(target) || target instanceof CardboardBoxEntity || target instanceof ArmorStand)
        {
            return false;
        }
        return super.canAttack(target);
    }

    private static final EquipmentSlot[] ARMOR_SLOTS =
            {EquipmentSlot.FEET, EquipmentSlot.LEGS, EquipmentSlot.CHEST, EquipmentSlot.HEAD};

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand)
    {
        if (!player.isShiftKeyDown() || !isOwnedBy(player))
        {
            return InteractionResult.PASS;
        }

        if (this.level().isClientSide)
        {
            return InteractionResult.CONSUME;
        }

        ItemStack heldItem = player.getItemInHand(hand);

        if (!heldItem.isEmpty())
        {
            EquipmentSlot slot = resolveEquipSlot(heldItem, hand);
            ItemStack equippedItem = this.getItemBySlot(slot);
            this.setItemSlot(slot, heldItem.split(1));
            if (!equippedItem.isEmpty() && !player.getInventory().add(equippedItem))
            {
                player.drop(equippedItem, false);
            }
        }
        else
        {
            EquipmentSlot slot = findUnequipSlot(hand);
            if (slot == null)
            {
                return InteractionResult.PASS;
            }
            ItemStack equippedItem = this.getItemBySlot(slot);
            this.setItemSlot(slot, ItemStack.EMPTY);
            player.setItemInHand(hand, equippedItem);
        }

        this.level().playSound(null, this.blockPosition(), SoundEvents.ARMOR_EQUIP_GENERIC, SoundSource.NEUTRAL, 1.0F, 1.0F);
        return InteractionResult.CONSUME;
    }

    /**
     * Armor pieces go to their matching armor slot regardless of which hand they're held in;
     * anything else keeps the previous per-hand behavior (main hand / off hand item slot).
     */
    private static EquipmentSlot resolveEquipSlot(ItemStack stack, InteractionHand hand)
    {
        if (stack.getItem() instanceof ArmorItem armorItem)
        {
            return armorItem.getEquipmentSlot();
        }
        return hand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND;
    }

    /**
     * Empty-hand shift-right-click retrieves the matching hand slot first (existing behavior),
     * then falls back to the first occupied armor slot so armor can be taken back too.
     */
    private EquipmentSlot findUnequipSlot(InteractionHand hand)
    {
        EquipmentSlot handSlot = hand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND;
        if (!this.getItemBySlot(handSlot).isEmpty())
        {
            return handSlot;
        }
        for (EquipmentSlot armorSlot : ARMOR_SLOTS)
        {
            if (!this.getItemBySlot(armorSlot).isEmpty())
            {
                return armorSlot;
            }
        }
        return null;
    }

    /**
     * Returns equipped items to the owner on death instead of dropping them on the ground,
     * falling back to a ground drop if the owner isn't loaded or their inventory is full.
     */
    @Override
    protected void dropEquipment()
    {
        Player owner = getOwnerEntity();
        for (EquipmentSlot slot : EquipmentSlot.values())
        {
            ItemStack stack = this.getItemBySlot(slot);
            if (stack.isEmpty())
            {
                continue;
            }
            this.setItemSlot(slot, ItemStack.EMPTY);
            if (owner == null)
            {
                this.spawnAtLocation(stack);
            }
            else if (!owner.getInventory().add(stack))
            {
                owner.drop(stack, false);
            }
        }
    }

    /**
     * Resolves the owning player entity in this level, if they're currently loaded.
     */
    public Player getOwnerEntity()
    {
        return getOwnerUUID().map(this.level()::getPlayerByUUID).orElse(null);
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer)
    {
        return false;
    }

    @Override
    public EntityDimensions getDimensions(Pose pose)
    {
        return super.getDimensions(pose).scale(getSizeScale());
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key)
    {
        super.onSyncedDataUpdated(key);
        // The client only receives the new SIZE_SCALE value here; unlike vanilla's built-in
        // Attributes.SCALE, nothing refreshes its hitbox for it automatically, so without this
        // the client keeps using the old (unscaled) bounding box for clicking/attacking even
        // though the model renders at the new size.
        if (SIZE_SCALE.equals(key))
        {
            this.refreshDimensions();
        }
    }

    public float getSizeScale()
    {
        return this.entityData.get(SIZE_SCALE);
    }

    public void setSizeScale(float scale)
    {
        this.entityData.set(SIZE_SCALE, scale);
        this.refreshDimensions();
    }

    /**
     * Scales this cardboard box up to giant size, boosting health/attack alongside its hitbox and model.
     * Intended to be called once, right after spawning.
     */
    public void makeGiant()
    {
        setSizeScale(GIANT_SCALE);

        AttributeInstance maxHealth = getAttribute(Attributes.MAX_HEALTH);
        if (maxHealth != null)
        {
            maxHealth.setBaseValue(maxHealth.getBaseValue() * GIANT_SCALE);
        }
        AttributeInstance attackDamage = getAttribute(Attributes.ATTACK_DAMAGE);
        if (attackDamage != null)
        {
            attackDamage.setBaseValue(attackDamage.getBaseValue() * GIANT_SCALE);
        }

        setHealth(getMaxHealth());
    }

    public void setOwnerUUID(UUID uuid)
    {
        this.entityData.set(OWNER_UUID, Optional.ofNullable(uuid));
    }

    public Optional<UUID> getOwnerUUID()
    {
        return this.entityData.get(OWNER_UUID);
    }

    public boolean isOwnedBy(LivingEntity entity)
    {
        return getOwnerUUID().isPresent() && getOwnerUUID().get().equals(entity.getUUID());
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag)
    {
        super.addAdditionalSaveData(tag);
        getOwnerUUID().ifPresent(uuid -> tag.putUUID("Owner", uuid));
        tag.putFloat("SizeScale", getSizeScale());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag)
    {
        super.readAdditionalSaveData(tag);
        if (tag.hasUUID("Owner"))
        {
            setOwnerUUID(tag.getUUID("Owner"));
        }
        if (tag.contains("SizeScale"))
        {
            // Attribute base values (health/attack) are restored separately via LivingEntity's own
            // "Attributes" NBT, so just resync the hitbox/model scale here without reapplying bonuses.
            this.entityData.set(SIZE_SCALE, tag.getFloat("SizeScale"));
            this.refreshDimensions();
        }
    }
}
