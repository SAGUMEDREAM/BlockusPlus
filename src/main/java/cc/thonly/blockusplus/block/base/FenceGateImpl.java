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
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FenceGateBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RotationAxis;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import xyz.nucleoid.packettweaker.PacketContext;

@Getter
public class FenceGateImpl implements FactoryBlock, PolymerTexturedBlock {
    private static final Block TEMPLATE = Blocks.MANGROVE_FENCE_GATE;
    private final Identifier blockId;

    public FenceGateImpl(Identifier blockId) {
        this.blockId = blockId;
    }

    @Override
    public BlockState getPolymerBlockState(BlockState state, PacketContext packetContext) {
        return TEMPLATE.getDefaultState().with(FenceGateBlock.FACING, state.get(FenceGateBlock.FACING))
                .with(FenceGateBlock.OPEN, state.get(FenceGateBlock.OPEN))
                .with(FenceGateBlock.IN_WALL, state.get(FenceGateBlock.IN_WALL));
    }

    @Override
    public @Nullable ElementHolder createElementHolder(ServerWorld world, BlockPos pos, BlockState initialBlockState) {
        return new Model(initialBlockState, this.blockId);
    }

    public static final class Model extends BlockModel {
        public final ItemStack MODEL_CLOSED;
        public final ItemStack MODEL_OPEN;
        public final ItemDisplayElement[] main = new ItemDisplayElement[2];

        public Model(BlockState state, Identifier id) {
            MODEL_CLOSED = ItemDisplayElementUtil.getModel(Identifier.of(id.getNamespace(), "block/" + id.getPath()));
            MODEL_OPEN = ItemDisplayElementUtil.getModel(Identifier.of(id.getNamespace(), "block/" + id.getPath() + "_open"));

            main[0] = ItemDisplayElementUtil.createSimple();
            main[1] = ItemDisplayElementUtil.createSimple();
            this.updateItem(state);
            addElement(main[0]);
            addElement(main[1]);
        }

        private void updateItem(BlockState state) {
            for (int i = 0; i < 2; i++) {
                ItemDisplayElement elem = main[i];
                elem.setItem(state.get(FenceGateBlock.OPEN) ? MODEL_OPEN : MODEL_CLOSED);
                float scale = 1.0025f;
                elem.setScale(new Vector3f(state.get(FenceGateBlock.OPEN) ? scale : 2 * scale));
                //float scaleOffset = (scale - 1) / 2;
                float offset = i == 0 ? 0.001f : -0.001f;
                elem.setTranslation(new Vector3f(offset, offset + (state.get(FenceGateBlock.IN_WALL) ? -0.1875f : 0), offset));
                elem.setRightRotation(state.get(FenceGateBlock.FACING).getRotationQuaternion().mul(RotationAxis.POSITIVE_X.rotationDegrees(-90)).mul(RotationAxis.POSITIVE_Y.rotationDegrees(180)));
            }
        }

        @Override
        public void notifyUpdate(HolderAttachment.UpdateType updateType) {
            if (updateType == BlockBoundAttachment.BLOCK_STATE_UPDATE) {
                this.updateItem(this.blockState());
                this.tick();
            }
            super.notifyUpdate(updateType);
        }
    }
}
