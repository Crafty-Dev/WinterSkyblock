package de.crafty.winterskyblock.entity;

import de.crafty.winterskyblock.registry.EntityRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;

public class FrozenZombie extends Zombie {

    private int ticksRemaining = 20 * 30;

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
    protected boolean isSunBurnTick() {
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

                this.level.playLocalSound(this.position().x, this.position().y, this.position().z, SoundEvents.POWDER_SNOW_PLACE, SoundSource.HOSTILE, 1.0F, 1.0F, false);
                this.convertToZombieType(EntityType.ZOMBIE);
        }
    }
}
