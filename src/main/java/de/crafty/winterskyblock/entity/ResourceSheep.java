package de.crafty.winterskyblock.entity;

import de.crafty.winterskyblock.WinterSkyblock;
import de.crafty.winterskyblock.entity.ai.goals.BreedGoal;
import de.crafty.winterskyblock.registry.ItemRegistry;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.IForgeShearable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.List;
import java.util.function.Supplier;

public class ResourceSheep extends Animal implements IForgeShearable {


    private static final EntityDataAccessor<Boolean> DATA_SHEARED = SynchedEntityData.defineId(ResourceSheep.class, EntityDataSerializers.BOOLEAN);

    private final EntityType<? extends ResourceSheep> sheepType;
    private final Type resourceType;
    private int eatAnimationTick;
    private EatBlockGoal eatBlockGoal;

    public ResourceSheep(EntityType<? extends ResourceSheep> entityType, Level level, Type resourceType) {
        super(entityType, level);

        this.sheepType = entityType;
        this.resourceType = resourceType;

        if (!level.isClientSide)
            this.registerGoalsAfterInit();
    }

    public Type getResourceType() {
        return this.resourceType;
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob ageableMob) {
        return this.sheepType.create(level);
    }


    @Override
    public void spawnChildFromBreeding(ServerLevel level, Animal animal) {
        AgeableMob ageablemob = this.getBreedOffspring(level, animal);
        final net.minecraftforge.event.entity.living.BabyEntitySpawnEvent event = new net.minecraftforge.event.entity.living.BabyEntitySpawnEvent(this, animal, ageablemob);
        final boolean cancelled = net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event);
        ageablemob = event.getChild();
        if (cancelled) {
            //Reset the "inLove" state for the animals
            this.setAge((int) (PARENT_AGE_AFTER_BREEDING * this.getResourceType().getStrength()));
            animal.setAge((int) (PARENT_AGE_AFTER_BREEDING * this.getResourceType().getStrength()));
            this.resetLove();
            animal.resetLove();
            return;
        }
        if (ageablemob != null) {
            ServerPlayer serverplayer = this.getLoveCause();
            if (serverplayer == null && animal.getLoveCause() != null) {
                serverplayer = animal.getLoveCause();
            }

            if (serverplayer != null) {
                serverplayer.awardStat(Stats.ANIMALS_BRED);
                CriteriaTriggers.BRED_ANIMALS.trigger(serverplayer, this, animal, ageablemob);
            }

            this.setAge((int) (PARENT_AGE_AFTER_BREEDING * this.getResourceType().getStrength()));
            animal.setAge((int) (PARENT_AGE_AFTER_BREEDING * this.getResourceType().getStrength()));
            this.resetLove();
            animal.resetLove();
            ageablemob.setBaby(true);
            ageablemob.moveTo(this.getX(), this.getY(), this.getZ(), 0.0F, 0.0F);
            level.addFreshEntityWithPassengers(ageablemob);
            level.broadcastEntityEvent(this, (byte) 18);
            if (level.getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT)) {
                level.addFreshEntity(new ExperienceOrb(level, this.getX(), this.getY(), this.getZ(), this.getRandom().nextInt(7) + 1));
            }

        }
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor levelAccessor, DifficultyInstance difficulty, MobSpawnType spawnType, @javax.annotation.Nullable SpawnGroupData spawnGroupData, @javax.annotation.Nullable CompoundTag tag) {
        if (spawnGroupData == null) {
            spawnGroupData = new AgeableMob.AgeableMobGroupData(true);
        }

        AgeableMob.AgeableMobGroupData ageablemob$ageablemobgroupdata = (AgeableMob.AgeableMobGroupData) spawnGroupData;
        if (ageablemob$ageablemobgroupdata.isShouldSpawnBaby() && ageablemob$ageablemobgroupdata.getGroupSize() > 0 && levelAccessor.getRandom().nextFloat() <= ageablemob$ageablemobgroupdata.getBabySpawnChance()) {
            this.setBaby(true);
        }

        ageablemob$ageablemobgroupdata.increaseGroupSizeByOne();
        return super.finalizeSpawn(levelAccessor, difficulty, spawnType, spawnGroupData, tag);
    }

    @Override
    public void setBaby(boolean baby) {
        this.setAge(baby ? (int) (BABY_START_AGE * this.resourceType.getStrength()) : 0);
    }

    @Override
    public void ate() {
        super.ate();
        this.setSheared(false);
        if (this.isBaby()) {
            this.ageUp(60);
        }
    }

    @Override
    protected void registerGoals() {
        //Called later after type has been set
    }

    private void registerGoalsAfterInit() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.25D));
        this.goalSelector.addGoal(2, new BreedGoal(this, 1.0D, Sheep.class, ResourceSheep.class));
        this.goalSelector.addGoal(3, new TemptGoal(this, 1.1D, Ingredient.of(this.resourceType.getBait()), false));
        this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.1D));
        this.goalSelector.addGoal(5, this.eatBlockGoal = new EatBlockGoal(this));
        this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
    }

    @Override
    public boolean canMate(Animal animal) {
        if (animal == this)
            return false;

        if (animal.getClass() != this.getClass() && !(animal instanceof Sheep)) {
            return false;
        }
        return this.isInLove() && animal.isInLove() && !(animal instanceof ResourceSheep resourceSheep && resourceSheep.getResourceType() != this.getResourceType());

    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.is(this.resourceType.getBait());
    }


    @Override
    protected void customServerAiStep() {
        this.eatAnimationTick = this.eatBlockGoal.getEatAnimationTick();
        super.customServerAiStep();
    }

    @Override
    public void aiStep() {
        if (this.level.isClientSide) {
            this.eatAnimationTick = Math.max(0, this.eatAnimationTick - 1);
        }

        super.aiStep();
    }

    @Override
    public void handleEntityEvent(byte b) {
        if (b == 10) {
            this.eatAnimationTick = 40;
        } else {
            super.handleEntityEvent(b);
        }
    }


    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_SHEARED, false);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("sheared", this.isSheared());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.setSheared(tag.getBoolean("sheared"));
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.SHEEP_AMBIENT;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.SHEEP_HURT;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.SHEEP_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.SHEEP_STEP, 0.15F, 1.0F);
    }


    public void setSheared(boolean sheared) {
        this.entityData.set(DATA_SHEARED, sheared);
    }

    public boolean isSheared() {
        return this.entityData.get(DATA_SHEARED);
    }

    @Override
    public boolean isShearable(@NotNull ItemStack item, Level level, BlockPos pos) {
        return this.isAlive() && !this.isSheared() && !this.isBaby() && item.getItem() instanceof ShearsItem;
    }

    @Override
    public @NotNull List<ItemStack> onSheared(@Nullable Player player, @NotNull ItemStack item, Level level, BlockPos pos, int fortune) {
        this.level.playSound(null, this, SoundEvents.SHEEP_SHEAR, this.getSoundSource(), 0.5F, 1.0F);
        this.level.playSound(null, this, this.resourceType.getSound(), this.getSoundSource(), 0.5F, 1.0F);

        this.setSheared(true);
        return List.of(new ItemStack(this.getResourceType().getResource(), this.getResourceType().isFortuneEffective() ? 1 + level.getRandom().nextInt(1 + fortune) : 1));
    }

    public static AttributeSupplier.Builder createAttributes(Type type) {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, type.getStrength() * 8.0D).add(Attributes.MOVEMENT_SPEED, 0.23F);
    }


    public float getHeadEatPositionScale(float p_29881_) {
        if (this.eatAnimationTick <= 0) {
            return 0.0F;
        } else if (this.eatAnimationTick >= 4 && this.eatAnimationTick <= 36) {
            return 1.0F;
        } else {
            return this.eatAnimationTick < 4 ? ((float) this.eatAnimationTick - p_29881_) / 4.0F : -((float) (this.eatAnimationTick - 40) - p_29881_) / 4.0F;
        }
    }

    public float getHeadEatAngleScale(float p_29883_) {
        if (this.eatAnimationTick > 4 && this.eatAnimationTick <= 36) {
            float f = ((float) (this.eatAnimationTick - 4) - p_29883_) / 32.0F;
            return ((float) Math.PI / 5F) + 0.21991149F * Mth.sin(f * 28.7F);
        } else {
            return this.eatAnimationTick > 0 ? ((float) Math.PI / 5F) : this.getXRot() * ((float) Math.PI / 180F);
        }
    }


    public enum Type {

        COAL(ItemRegistry.COAL_ORE_DUST, ItemRegistry.COAL_ENRICHED_WHEAT, 1.0F, "ore_sheep", SoundEvents.STONE_BREAK, true),
        IRON(ItemRegistry.IRON_ORE_DUST, ItemRegistry.IRON_ENRICHED_WHEAT, 1.0F, "ore_sheep", SoundEvents.STONE_BREAK, true),
        COPPER(ItemRegistry.COPPER_ORE_DUST, ItemRegistry.COPPER_ENRICHED_WHEAT, 1.0F, "ore_sheep", SoundEvents.STONE_BREAK, true),
        GOLD(ItemRegistry.GOLD_ORE_DUST, ItemRegistry.GOLD_ENRICHED_WHEAT, 1.75F, "ore_sheep", SoundEvents.STONE_BREAK, true),
        LAPIS(ItemRegistry.LAPIS_ORE_DUST, ItemRegistry.LAPIS_ENRICHED_WHEAT, 1.75F, "ore_sheep", SoundEvents.STONE_BREAK, true),
        REDSTONE(ItemRegistry.REDSTONE_ORE_DUST, ItemRegistry.REDSTONE_ENRICHED_WHEAT, 1.75F, "ore_sheep", SoundEvents.STONE_BREAK, true),
        DIAMOND(ItemRegistry.DIAMOND_ORE_DUST, ItemRegistry.DIAMOND_ENRICHED_WHEAT, 2.5F, "ore_sheep", SoundEvents.STONE_BREAK, true),
        EMERALD(ItemRegistry.EMERALD_ORE_DUST, ItemRegistry.EMERALD_ENRICHED_WHEAT, 2.5F, "ore_sheep", SoundEvents.STONE_BREAK, true),
        QUARTZ(ItemRegistry.QUARTZ_ORE_DUST, ItemRegistry.QUARTZ_ENRICHED_WHEAT, 1.5F, "nether_ore_sheep", SoundEvents.NETHERRACK_BREAK, true),
        NETHERITE(ItemRegistry.NETHERITE_ORE_DUST, ItemRegistry.NETHERITE_ENRICHED_WHEAT, 5.0F, "nether_ore_sheep", SoundEvents.ANCIENT_DEBRIS_BREAK, true),
        GLOWSTONE(ItemRegistry.GLOWSTONE_ORE_DUST, ItemRegistry.GLOWSTONE_ENRICHED_WHEAT, 1.25F, "nether_ore_sheep", SoundEvents.GLASS_BREAK, true),
        NETHERRACK(() -> Items.NETHERRACK, ItemRegistry.NETHERRACK_ENRICHED_WHEAT, 1.0F, "nether_ore_sheep", SoundEvents.NETHERRACK_BREAK, false),
        COBBLESTONE(() -> Items.COBBLESTONE, ItemRegistry.COBBLESTONE_ENRICHED_WHEAT, 1.0F, "ore_sheep", SoundEvents.STONE_BREAK, false),
        DIRT(() -> Items.DIRT, ItemRegistry.DIRT_ENRICHED_WHEAT, 1.0F, "overworld_sheep", SoundEvents.GRASS_BREAK, false);


        private final Supplier<? extends Item> resource;
        private final Supplier<? extends Item> bait;
        private final float strength;
        private final ResourceLocation texture;
        private final ResourceLocation fur_texture;
        private final SoundEvent sound;
        private final boolean isFortuneEffective;

        Type(Supplier<? extends Item> resource, Supplier<? extends Item> bait, float strength, String sheepTexture, SoundEvent sound, boolean isFortuneEffective) {
            this.resource = resource;
            this.bait = bait;
            this.strength = strength;

            this.texture = new ResourceLocation(WinterSkyblock.MODID, "textures/entity/resourcesheep/" + sheepTexture + ".png");
            this.fur_texture = new ResourceLocation(WinterSkyblock.MODID, "textures/entity/resourcesheep/fur/" + this.name().toLowerCase() + ".png");

            this.sound = sound;
            this.isFortuneEffective = isFortuneEffective;
        }

        public Item getResource() {
            return this.resource.get();
        }

        public Item getBait() {
            return this.bait.get();
        }

        public float getStrength() {
            return this.strength;
        }

        public ResourceLocation getTexture() {
            return this.texture;
        }

        public ResourceLocation getFurTexture() {
            return this.fur_texture;
        }

        public SoundEvent getSound() {
            return this.sound;
        }

        public boolean isFortuneEffective() {
            return this.isFortuneEffective;
        }
    }

}
