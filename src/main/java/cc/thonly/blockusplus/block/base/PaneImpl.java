package cc.thonly.blockusplus.block.base;

import eu.pb4.factorytools.api.block.*;
import eu.pb4.factorytools.api.virtualentity.BlockModel;
import eu.pb4.factorytools.api.virtualentity.ItemDisplayElementUtil;
import eu.pb4.polymer.blocks.api.PolymerTexturedBlock;
import eu.pb4.polymer.virtualentity.api.ElementHolder;
import eu.pb4.polymer.virtualentity.api.attachment.BlockBoundAttachment;
import eu.pb4.polymer.virtualentity.api.attachment.HolderAttachment;
import eu.pb4.polymer.virtualentity.api.elements.ItemDisplayElement;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalConnectingBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import xyz.nucleoid.packettweaker.PacketContext;

import java.util.HashMap;
import java.util.Map;

public class PaneImpl implements FactoryBlock, PolymerTexturedBlock {
    private final Identifier blockId;

    public PaneImpl(Identifier blockId) {
        this.blockId = blockId;
    }

    @Override
    public BlockState getPolymerBlockState(BlockState blockState, PacketContext packetContext) {
        return Blocks.GLASS_PANE.getDefaultState()
                .with(HorizontalConnectingBlock.NORTH, blockState.get(HorizontalConnectingBlock.NORTH))
                .with(HorizontalConnectingBlock.EAST, blockState.get(HorizontalConnectingBlock.EAST))
                .with(HorizontalConnectingBlock.SOUTH, blockState.get(HorizontalConnectingBlock.SOUTH))
                .with(HorizontalConnectingBlock.WEST, blockState.get(HorizontalConnectingBlock.WEST))
                .with(HorizontalConnectingBlock.WATERLOGGED, blockState.get(HorizontalConnectingBlock.WATERLOGGED));
//        return Blocks.GLASS_PANE.getDefaultState();
//        return Blocks.BARRIER.getDefaultState();
    }

    @Override
    public @Nullable ElementHolder createElementHolder(ServerWorld world, BlockPos pos, BlockState initialBlockState) {
        return new FenceImpl.Model(initialBlockState, this.blockId);
    }

    public static class Model extends BlockModel {
        public final ItemDisplayElement post;
        public final ItemDisplayElement sideAlt;
        public final Map<Direction, ItemDisplayElement> sides = new HashMap<>();

        public Model(BlockState state, Identifier id) {
            ItemStack MODEL_SIDE_ALT = ItemDisplayElementUtil.getModel(Identifier.of(id.getNamespace(), "block/%s_side_alt".formatted(id.getPath())));
            ItemStack MODEL_NOSIDE = ItemDisplayElementUtil.getModel(Identifier.of(id.getNamespace(), "block/%s_noside".formatted(id.getPath())));
            ItemStack MODEL_POST = ItemDisplayElementUtil.getModel(Identifier.of(id.getNamespace(), "block/%s_post".formatted(id.getPath())));
            ItemStack MODEL_SIDE = ItemDisplayElementUtil.getModel(Identifier.of(id.getNamespace(), "block/%s_side".formatted(id.getPath())));

            post = ItemDisplayElementUtil.createSimple(MODEL_POST);
            sideAlt = ItemDisplayElementUtil.createSimple(MODEL_SIDE_ALT);
            post.setScale(new Vector3f(1.00275f));
            sideAlt.setScale(new Vector3f(1.00275f));
            addElement(post);
            addElement(sideAlt);
            for (Direction side : Direction.Type.HORIZONTAL) {
                var noSide = ItemDisplayElementUtil.createSimple(MODEL_NOSIDE);
                sides.put(side, ItemDisplayElementUtil.createSimple(MODEL_SIDE));
                sides.get(side).setYaw(side.getPositiveHorizontalDegrees());
                sides.get(side).setScale(new Vector3f(1.00275f));
                noSide.setYaw(side.getPositiveHorizontalDegrees());
                addElement(noSide);
                addElement(sides.get(side));
            }
            this.updateItem(state);
        }

        private void updateItem(BlockState state) {
            boolean north = state.get(HorizontalConnectingBlock.NORTH);
            boolean east = state.get(HorizontalConnectingBlock.EAST);
            boolean south = state.get(HorizontalConnectingBlock.SOUTH);
            boolean west = state.get(HorizontalConnectingBlock.WEST);

            setVisibility(sides.get(Direction.NORTH), north);
            setVisibility(sides.get(Direction.EAST), east);
            setVisibility(sides.get(Direction.SOUTH), south);
            setVisibility(sides.get(Direction.WEST), west);
        }

        private void setVisibility(ItemDisplayElement elem, boolean visible) {
            elem.setViewRange(visible ? 0.75f : 0f);
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
