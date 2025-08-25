package cc.thonly.blockuspatch.block.base.mod;

import com.brand.blockus.blocks.base.PostBlock;
import eu.pb4.factorytools.api.block.FactoryBlock;
import eu.pb4.factorytools.api.virtualentity.BlockModel;
import eu.pb4.factorytools.api.virtualentity.ItemDisplayElementUtil;
import eu.pb4.polymer.blocks.api.PolymerTexturedBlock;
import eu.pb4.polymer.virtualentity.api.ElementHolder;
import eu.pb4.polymer.virtualentity.api.attachment.BlockBoundAttachment;
import eu.pb4.polymer.virtualentity.api.attachment.HolderAttachment;
import eu.pb4.polymer.virtualentity.api.elements.ItemDisplayElement;
import lombok.Getter;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LightningRodBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import xyz.nucleoid.packettweaker.PacketContext;

@Getter
public class PostImpl implements FactoryBlock, PolymerTexturedBlock {
    private final Identifier blockId;
    private final Identifier model;

    public PostImpl(Identifier blockId) {
        this.blockId = blockId;
        this.model = Identifier.of(blockId.getNamespace(), "block/" + blockId.getPath());
//        model[0] = PolymerBlockModel.of(), 90, 90);
//        model[1] = PolymerBlockModel.of(), 0, 0);
//        model[2] = PolymerBlockModel.of(Identifier.of(blockId.getNamespace(), "block/" + blockId.getPath()), 90, 0);
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
        return new Model(initialBlockState);
    }

    public class Model extends BlockModel {
        private ItemDisplayElement main;
        public Model(BlockState state) {
            update(state);
        }

        private void update(BlockState state) {
            if (this.main != null) {
                removeElement(this.main);
            }
            this.main = ItemDisplayElementUtil.createSimple(PostImpl.this.model);
            Direction.Axis axis = state.get(PostBlock.AXIS);
            this.main.setScale(new Vector3f(1.004f));
            if (axis == Direction.Axis.X) {
                this.main.setRotation(90, 90);
            }
            if (axis == Direction.Axis.Y) {
                this.main.setRotation(0, 0);
            }
            if (axis == Direction.Axis.Z) {
                this.main.setRotation(90, 0);
            }
            addElement(this.main);
        }

        @Override
        public void notifyUpdate(HolderAttachment.UpdateType updateType) {
            if (updateType == BlockBoundAttachment.BLOCK_STATE_UPDATE) {
                this.update(this.blockState());
                this.tick();
            }
            super.notifyUpdate(updateType);
        }
    }
}
