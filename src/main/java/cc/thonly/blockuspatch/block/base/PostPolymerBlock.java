package cc.thonly.blockuspatch.block.base;

import com.brand.blockus.blocks.base.PostBlock;
import eu.pb4.factorytools.api.block.FactoryBlock;
import eu.pb4.factorytools.api.block.model.generic.ShiftyBlockStateModel;
import eu.pb4.polymer.blocks.api.PolymerTexturedBlock;
import eu.pb4.polymer.virtualentity.api.ElementHolder;
import lombok.Getter;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LightningRodBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;
import xyz.nucleoid.packettweaker.PacketContext;

@Getter
public class PostPolymerBlock implements FactoryBlock, PolymerTexturedBlock {
    private final Identifier blockId;
    private final Identifier model;

    public PostPolymerBlock(Identifier blockId) {
        this.blockId = blockId;
        this.model = Identifier.of(blockId.getNamespace(), "block/" + blockId.getPath());
    }

    @Override
    public BlockState getPolymerBlockState(BlockState blockState, PacketContext packetContext) {
        BlockState defaultState = Blocks.LIGHTNING_ROD.getDefaultState();
        Direction.Axis axis = blockState.get(PostBlock.AXIS);
        if (axis == Direction.Axis.X) {
            defaultState = defaultState.with(LightningRodBlock.FACING, Direction.EAST);
        }
        if (axis == Direction.Axis.Y) {
            defaultState = defaultState.with(LightningRodBlock.FACING, Direction.UP);
        }
        if (axis == Direction.Axis.Z) {
            defaultState = defaultState.with(LightningRodBlock.FACING, Direction.SOUTH);
        }
        return defaultState.with(PostBlock.WATERLOGGED, blockState.get(PostBlock.WATERLOGGED));
    }

    @Override
    public @Nullable ElementHolder createElementHolder(ServerWorld world, BlockPos pos, BlockState initialBlockState) {
        return ShiftyBlockStateModel.longRange(initialBlockState, pos);
    }
}
