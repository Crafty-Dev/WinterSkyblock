package de.crafty.winterskyblock.item;

import de.crafty.winterskyblock.registry.ItemRegistry;
import de.crafty.winterskyblock.registry.TabRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SpawnerBlock;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class MobOrbItem extends Item {


    public MobOrbItem(Item.Properties properties) {
        super(properties);
    }


    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity livingEntity, InteractionHand hand) {

        if (livingEntity.getType().equals(EntityType.ENDER_DRAGON) || livingEntity.getType().equals(EntityType.PLAYER))
            return InteractionResult.PASS;

        EntityType<?> savedEntity = readEntityType(stack.getOrCreateTag());
        if (savedEntity == null) {
            if (!player.getLevel().isClientSide())
                player.setItemInHand(hand, saveEntity(livingEntity));
            else
                player.playSound(SoundEvents.ENDER_EYE_DEATH, 1.0F, 2.0F);
            return InteractionResult.SUCCESS;
        }

        return super.interactLivingEntity(stack, player, livingEntity, hand);
    }

    @Override
    public InteractionResult useOn(UseOnContext ctx) {

        BlockPos pos = ctx.getClickedPos();
        Direction direction = ctx.getClickedFace();
        Player player = ctx.getPlayer();
        Level level = ctx.getLevel();
        BlockState state = level.getBlockState(pos);

        ItemStack stack = ctx.getItemInHand();
        EntityType<?> savedEntity = readEntityType(stack.getOrCreateTag());


        if (savedEntity == null)
            return InteractionResult.PASS;

        if (level instanceof ServerLevel serverLevel) {

            Vec3 p = ctx.getClickLocation().add(direction.getNormal().getX() * 0.5D, 0.0D, direction.getNormal().getZ() * 0.5D);
            player.setItemInHand(ctx.getHand(), loadEntity(pos, stack, serverLevel, p));

        }

        if (level.isClientSide())
            player.playSound(SoundEvents.ENDER_EYE_DEATH, 1.0F, 2.0F);


        return InteractionResult.SUCCESS;
    }

    public static EntityType<? extends Entity> readEntityType(CompoundTag tag) {
        if (tag.contains("savedEntity")){
           Optional<EntityType<?>> optional = EntityType.by(tag.getCompound("savedEntity"));
           return optional.orElse(null);
        }


        return null;
    }

    public static ItemStack saveEntity(Entity entity) {

        CompoundTag entityTag = new CompoundTag();
        entity.saveAsPassenger(entityTag);

        entity.remove(Entity.RemovalReason.DISCARDED);

        ItemStack orbStack = new ItemStack(ItemRegistry.MOB_ORB_ACTIVE.get());
        orbStack.addTagElement("savedEntity", entityTag);

        //System.out.println("Saved: " + orbStack.getTag().getCompound("savedEntity"));

        return orbStack;
    }


    public static ItemStack loadEntity(BlockPos clickedBlockPos, ItemStack stack, ServerLevel level, Vec3 pos) {

        CompoundTag itemTag = stack.getOrCreateTag();
        EntityType<?> entityType = readEntityType(itemTag);
        ItemStack orbStack = new ItemStack(ItemRegistry.MOB_ORB.get());


        if (level.getBlockEntity(new BlockPos(clickedBlockPos)) instanceof SpawnerBlockEntity spawner) {
            spawner.getSpawner().setEntityId(entityType);
            level.sendBlockUpdated(clickedBlockPos, level.getBlockState(clickedBlockPos), level.getBlockState(clickedBlockPos), 2);
            spawner.setChanged();
            return orbStack;
        }

        Entity entity = entityType.create(level);
        entity.load(itemTag.getCompound("savedEntity"));
        entity.setPos(pos);
        level.addFreshEntity(entity);

        return orbStack;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> lore, TooltipFlag flag) {

        EntityType<?> savedEntity = readEntityType(stack.getOrCreateTag());
        if (savedEntity == null) {
            lore.add(Component.translatable(this.getDescriptionId() + ".empty").withStyle(ChatFormatting.GRAY));
            return;
        }

        lore.add(Component.translatable(savedEntity.getDescriptionId()).withStyle(ChatFormatting.DARK_PURPLE));

        if(stack.getOrCreateTag().getCompound("savedEntity").contains("Health"))
            lore.add(Component.literal(stack.getOrCreateTag().getCompound("savedEntity").getFloat("Health") + " Health").withStyle(ChatFormatting.RED));

    }
}
