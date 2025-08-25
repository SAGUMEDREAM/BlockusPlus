package cc.thonly.blockusplus.block;

import eu.pb4.factorytools.api.block.FactoryBlock;
import eu.pb4.factorytools.api.block.model.SignModel;
import eu.pb4.factorytools.api.block.model.generic.BSMMParticleBlock;
import eu.pb4.factorytools.api.virtualentity.BlockModel;
import eu.pb4.polymer.blocks.api.PolymerTexturedBlock;
import eu.pb4.polymer.virtualentity.api.ElementHolder;
import net.minecraft.block.*;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;
import xyz.nucleoid.packettweaker.PacketContext;

import java.util.function.BiFunction;

public record StateCopyFactoryBlock(Block clientBlock, BiFunction<BlockState, BlockPos, BlockModel> modelFunction) implements FactoryBlock, PolymerTexturedBlock {
    public static final StateCopyFactoryBlock SIGN = new StateCopyFactoryBlock(Blocks.BIRCH_SIGN, SignModel::new);
    public static final StateCopyFactoryBlock WALL_SIGN = new StateCopyFactoryBlock(Blocks.BIRCH_WALL_SIGN, SignModel::new);
    public static final StateCopyFactoryBlock HANGING_SIGN = new StateCopyFactoryBlock(Blocks.BIRCH_HANGING_SIGN, SignModel::new);
    public static final StateCopyFactoryBlock HANGING_WALL_SIGN = new StateCopyFactoryBlock(Blocks.BIRCH_WALL_HANGING_SIGN, SignModel::new);

    @Override
    public BlockState getPolymerBlockState(BlockState state, PacketContext context) {
        return clientBlock.getStateWithProperties(state);
    }

    @Override
    public @Nullable ElementHolder createElementHolder(ServerWorld world, BlockPos pos, BlockState initialBlockState) {
        return this.modelFunction.apply(initialBlockState, pos);
    }

    @Override
    public boolean isIgnoringBlockInteractionPlaySoundExceptedEntity(BlockState state, ServerPlayerEntity player, Hand hand, ItemStack stack, ServerWorld world, BlockHitResult blockHitResult) {
        return true;
    }
}
