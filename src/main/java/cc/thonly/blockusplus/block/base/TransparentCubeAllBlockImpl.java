package cc.thonly.blockusplus.block.base;

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
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import xyz.nucleoid.packettweaker.PacketContext;

@Getter
public class TransparentCubeAllBlockImpl implements FactoryBlock, PolymerTexturedBlock {
    private final Identifier blockId;

    public TransparentCubeAllBlockImpl(Identifier blockId) {
        this.blockId = blockId;
    }

    @Override
    public boolean forceLightUpdates(BlockState blockState) {
        return true;
    }

    @Override
    public BlockState getPolymerBlockState(BlockState blockState, PacketContext packetContext) {
        return Blocks.BARRIER.getDefaultState();
    }

    @Override
    public @Nullable ElementHolder createElementHolder(ServerWorld world, BlockPos pos, BlockState initialBlockState) {
        return new Model(initialBlockState, this.blockId);
    }

    public static class Model extends BlockModel {
        private ItemDisplayElement main;

        public Model(BlockState initialBlockState, Identifier blockId) {
            this.main = ItemDisplayElementUtil.createSimple(Identifier.of(blockId.getNamespace(), "block/" + blockId.getPath()));
            this.init(initialBlockState);
        }

        public void init(BlockState state) {
            this.update(state);
        }

        public void update(BlockState state) {
            this.removeElement(this.main);
            this.main.setScale(new Vector3f(2));
            this.addElement(this.main);
        }

        @Override
        public void notifyUpdate(HolderAttachment.UpdateType updateType) {
            if (updateType == BlockBoundAttachment.BLOCK_STATE_UPDATE) {
                this.tick();
            }
            super.notifyUpdate(updateType);
        }
    }
}
