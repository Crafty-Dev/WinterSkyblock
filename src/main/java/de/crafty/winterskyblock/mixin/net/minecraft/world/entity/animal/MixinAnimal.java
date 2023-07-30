package de.crafty.winterskyblock.mixin.net.minecraft.world.entity.animal;


import de.crafty.winterskyblock.entity.ResourceSheep;
import de.crafty.winterskyblock.item.ResourceWheatItem;
import de.crafty.winterskyblock.registry.TagRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Animal.class)
public abstract class MixinAnimal extends AgeableMob {


    protected MixinAnimal(EntityType<? extends AgeableMob> p_146738_, Level p_146739_) {
        super(p_146738_, p_146739_);
    }


    @Inject(method = "isFood", at = @At("HEAD"), cancellable = true)
    private void addSpecialFood(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(stack.is(Items.WHEAT) || (((Animal) (Object) this) instanceof Sheep && stack.is(TagRegistry.RESSOURCE_WHEAT)));
    }

    @Inject(method = "mobInteract", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/Animal;setInLove(Lnet/minecraft/world/entity/player/Player;)V", shift = At.Shift.AFTER))
    private void addData$0(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        ItemStack stack = player.getItemInHand(hand).copy();
        stack.setCount(1);
        this.getPersistentData().put("lastFood", stack.save(new CompoundTag()));
    }


    @Redirect(method = "spawnChildFromBreeding", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/Animal;getBreedOffspring(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/AgeableMob;)Lnet/minecraft/world/entity/AgeableMob;"))
    private AgeableMob modifySheeps(Animal animal, ServerLevel level, AgeableMob ageableMob) {

        ItemStack lastFood = ItemStack.of(this.getPersistentData().getCompound("lastFood"));

        if (!(animal instanceof Sheep) && !(animal instanceof ResourceSheep))
            return this.getBreedOffspring(level, ageableMob);

        if (!(ageableMob instanceof Sheep) && !(ageableMob instanceof ResourceSheep))
            return this.getBreedOffspring(level, ageableMob);

        if (animal instanceof ResourceSheep && ageableMob instanceof ResourceSheep)
            return this.getBreedOffspring(level, ageableMob);


        if (!lastFood.is(ItemStack.of(ageableMob.getPersistentData().getCompound("lastFood")).getItem()) || !(lastFood.getItem() instanceof ResourceWheatItem resourceWheat))
            return this.getBreedOffspring(level, ageableMob);


        boolean canSpawnMutant = !lastFood.is(TagRegistry.NETHER_WHEAT) || level.dimension() == Level.NETHER;

        if (animal instanceof Sheep && ageableMob instanceof Sheep && level.getRandom().nextFloat() <= resourceWheat.getSpawnChance() && canSpawnMutant)
            return resourceWheat.getSheepType().create(level);

        if ((animal instanceof ResourceSheep || ageableMob instanceof ResourceSheep) && level.getRandom().nextFloat() <= resourceWheat.getSpawnChance() + resourceWheat.getSpawnChance() / 4.0F && canSpawnMutant)
            return resourceWheat.getSheepType().create(level);

        if (animal instanceof ResourceSheep)
            return ageableMob.getBreedOffspring(level, animal);

        return this.getBreedOffspring(level, ageableMob);
    }


    @Inject(method = "canMate", at = @At("HEAD"), cancellable = true)
    private void modifySheepBreeding(Animal animal, CallbackInfoReturnable<Boolean> cir) {

        ItemStack lastFood = ItemStack.of(this.getPersistentData().getCompound("lastFood"));


        if (((Animal) (Object) this) instanceof Sheep && animal instanceof ResourceSheep && ItemStack.of(animal.getPersistentData().getCompound("lastFood")).is(lastFood.getItem()))
            cir.setReturnValue(true);

        if (((Animal) (Object) this) instanceof ResourceSheep && animal instanceof Sheep && ItemStack.of(animal.getPersistentData().getCompound("lastFood")).is(lastFood.getItem()))
            cir.setReturnValue(true);
    }

}
