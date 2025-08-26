package cc.thonly.blockuspatch.block.base;

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
import org.joml.Matrix4f;
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
    }

    @Override
    public @Nullable ElementHolder createElementHolder(ServerWorld world, BlockPos pos, BlockState initialBlockState) {
        return new Model(initialBlockState, this.blockId);
    }

    public static class Model extends BlockModel {
        public final Map<Direction, ItemDisplayElement> whenEnabled = new HashMap<>();
        public final Map<Direction, ItemDisplayElement> whenDisabled = new HashMap<>();

        public Model(BlockState state, Identifier id) {
            ItemStack MODEL_POST = ItemDisplayElementUtil.getModel(Identifier.of(id.getNamespace(), "block/%s_post".formatted(id.getPath())));
            ItemStack MODEL_SIDE = ItemDisplayElementUtil.getModel(Identifier.of(id.getNamespace(), "block/%s_side".formatted(id.getPath())));
            ItemStack MODEL_SIDE_ALT = ItemDisplayElementUtil.getModel(Identifier.of(id.getNamespace(), "block/%s_side_alt".formatted(id.getPath())));
            ItemStack MODEL_NOSIDE = ItemDisplayElementUtil.getModel(Identifier.of(id.getNamespace(), "block/%s_noside".formatted(id.getPath())));
            ItemStack MODEL_NOSIDE_ALT = ItemDisplayElementUtil.getModel(Identifier.of(id.getNamespace(), "block/%s_noside_alt".formatted(id.getPath())));

            var post = ItemDisplayElementUtil.createSimple(MODEL_POST);
            post.setScale(new Vector3f(1.00275f));
            addElement(post);

            // Copied from BlockStateModelGenerator.registerGlassAndPane
            whenEnabled.put(Direction.NORTH, createItemDisplay(MODEL_SIDE, 0));
            whenEnabled.put(Direction.EAST, createItemDisplay(MODEL_SIDE, 90));
            whenEnabled.put(Direction.SOUTH, createItemDisplay(MODEL_SIDE_ALT, 0));
            whenEnabled.put(Direction.WEST, createItemDisplay(MODEL_SIDE_ALT, 90));

            whenDisabled.put(Direction.NORTH, createItemDisplay(MODEL_NOSIDE, 0));
            whenDisabled.put(Direction.EAST, createItemDisplay(MODEL_NOSIDE_ALT, 0));
            whenDisabled.put(Direction.SOUTH, createItemDisplay(MODEL_NOSIDE_ALT, 90));
            whenDisabled.put(Direction.WEST, createItemDisplay(MODEL_NOSIDE, 270));

            this.updateItem(state);
        }

        private ItemDisplayElement createItemDisplay(ItemStack model, float yRot) {
            var itemDisplay = ItemDisplayElementUtil.createSimple(model);
            itemDisplay.setTransformation(new Matrix4f().rotateY((float) ((180 - yRot) * Math.PI / 180f)));
            itemDisplay.setScale(new Vector3f(1.00275f));
            return itemDisplay;
        }

        private void updateItem(BlockState state) {
            boolean north = state.get(HorizontalConnectingBlock.NORTH);
            boolean east = state.get(HorizontalConnectingBlock.EAST);
            boolean south = state.get(HorizontalConnectingBlock.SOUTH);
            boolean west = state.get(HorizontalConnectingBlock.WEST);

            setVisibility(Direction.NORTH, north);
            setVisibility(Direction.EAST, east);
            setVisibility(Direction.SOUTH, south);
            setVisibility(Direction.WEST, west);
        }

        private void setVisibility(Direction direction, boolean visible) {
            if (visible) {
                addElement(whenEnabled.get(direction));
                removeElement(whenDisabled.get(direction));
            } else {
                removeElement(whenEnabled.get(direction));
                addElement(whenDisabled.get(direction));
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
