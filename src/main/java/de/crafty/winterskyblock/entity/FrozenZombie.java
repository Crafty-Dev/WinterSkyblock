package de.crafty.winterskyblock.entity;

import de.crafty.winterskyblock.registry.EntityRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class FrozenZombie extends Zombie {

    private static final int TOTAL_LIFE_TICKS = 20 * 30;
    private int ticksRemaining = TOTAL_LIFE_TICKS;

    public FrozenZombie(EntityType<? extends Zombie> entityType, Level level) {
        super(entityType, level);
    }

    public FrozenZombie(Level level){
        super(EntityRegistry.FROZEN_ZOMBIE.get(), level);

    }


    @Override
    protected boolean convertsInWater() {
        return false;
    }


    @Override
    public boolean hurt(DamageSource damageSource, float damage) {
        if(!super.hurt(damageSource, damage))
            return false;

        if(damageSource.isProjectile() && damageSource.getDirectEntity() instanceof Snowball)
            this.ticksRemaining = Math.min(TOTAL_LIFE_TICKS, this.ticksRemaining + 20 * 10);

        return true;
    }

    @Override
    public boolean doHurtTarget(Entity entity) {
        boolean canHurt = super.doHurtTarget(entity);

        if(canHurt){
            //float f = this.level.getCurrentDifficultyAt(this.blockPosition()).getEffectiveDifficulty();
            entity.setTicksFrozen(20 * 5);
        }

        return canHurt;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);

        tag.putInt("ticksRemaining", this.ticksRemaining);

    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);

        if(tag.contains("ticksRemaining", 99)){
            this.ticksRemaining = tag.getInt("ticksRemaining");
            this.checkRemainingTime();
        }

    }

    @Override
    protected boolean isSunSensitive() {
        return false;
    }

    @Override
    public void tick() {

        if(!this.level.isClientSide() && this.isAlive() && !this.isNoAi()){
            --this.ticksRemaining;
            this.checkRemainingTime();
        }

        super.tick();

    }

    @Override
    public boolean isUnderWaterConverting() {
        return false;
    }

    private void checkRemainingTime(){
        if(this.ticksRemaining <= 0){

                CompoundTag oldTag = new CompoundTag();
                this.save(oldTag);
                this.discard();

                Zombie zombie = EntityType.ZOMBIE.create(this.level);
                zombie.load(oldTag);
                zombie.copyPosition(this);
                this.level.addFreshEntity(zombie);

        }
    }

    @Override
    public float getSpeed() {
        return this.level.isRaining() && this.level.getBiome(this.blockPosition()).get().coldEnoughToSnow(this.blockPosition()) ? super.getSpeed() * 1.5F : super.getSpeed() * 0.75F;
    }
}
