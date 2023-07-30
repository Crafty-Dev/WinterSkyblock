package de.crafty.winterskyblock.block;

import de.crafty.winterskyblock.registry.ItemRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

public class DragonInfusedBedrockBlock extends Block {



    public DragonInfusedBedrockBlock() {
        super(Properties.copy(Blocks.BEDROCK).lightLevel(value -> 10));
    }


    @Override
    public @NotNull InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {

        level.setBlock(pos, Blocks.BEDROCK.defaultBlockState(), 3);
        Block.popResource(level, pos.above(), new ItemStack(ItemRegistry.DRAGON_ARTIFACT.get()));
        level.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.GRINDSTONE_USE, SoundSource.BLOCKS, 1.0F, 0.25F, false);
        return InteractionResult.SUCCESS;

    }
}
