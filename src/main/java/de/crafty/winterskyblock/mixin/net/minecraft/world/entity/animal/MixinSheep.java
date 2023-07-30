package de.crafty.winterskyblock.mixin.net.minecraft.world.entity.animal;

import de.crafty.winterskyblock.entity.ResourceSheep;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Shearable;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Sheep.class)
public abstract class MixinSheep extends Animal implements Shearable, net.minecraftforge.common.IForgeShearable {


    @Shadow public abstract DyeColor getColor();

    protected MixinSheep(EntityType<? extends Animal> p_27557_, Level p_27558_) {
        super(p_27557_, p_27558_);
    }

    @Redirect(method = "registerGoals", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/goal/GoalSelector;addGoal(ILnet/minecraft/world/entity/ai/goal/Goal;)V", ordinal = 2))
    private void modifySheep(GoalSelector goalSelector, int id, Goal goal){
        goalSelector.addGoal(2, new de.crafty.winterskyblock.entity.ai.goals.BreedGoal(this, 1.0D, Sheep.class, ResourceSheep.class));
    }


    @Inject(method = "getBreedOffspring(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/AgeableMob;)Lnet/minecraft/world/entity/animal/Sheep;", at = @At("HEAD"), cancellable = true)
    private void fixBug(ServerLevel level, AgeableMob ageableMob, CallbackInfoReturnable<Sheep> cir){
        if(!(ageableMob instanceof ResourceSheep))
            return;

        Sheep sheep = EntityType.SHEEP.create(level);
        sheep.setColor(this.getColor());
        cir.setReturnValue(sheep);
    }
}
