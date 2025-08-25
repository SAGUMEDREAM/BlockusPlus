package cc.thonly.blockuspatch.block.base.mod;

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

@Getter
public class SmallHedgeImpl implements FactoryBlock, PolymerTexturedBlock {
    private final Identifier blockId;

    public SmallHedgeImpl(Identifier blockId) {
        this.blockId = blockId;
    }

    @Override
    public @Nullable ElementHolder createElementHolder(ServerWorld world, BlockPos pos, BlockState initialBlockState) {
        return new cc.thonly.blockuspatch.block.base.FenceImpl.Model(initialBlockState, this.blockId);
    }

    @Override
    public BlockState getPolymerBlockState(BlockState state, PacketContext packetContext) {
        return Blocks.BARRIER.getDefaultState();
    }

    public static class Model extends BlockModel {
        public final ItemDisplayElement post;
        public final Map<Direction, ItemDisplayElement> sides = new HashMap<>();

        public Model(BlockState state, Identifier id) {
            ItemStack MODEL_POST = ItemDisplayElementUtil.getModel(Identifier.of(id.getNamespace(), "block/%s_post".formatted(id.getPath())));
            ItemStack MODEL_SIDE = ItemDisplayElementUtil.getModel(Identifier.of(id.getNamespace(), "block/%s_side".formatted(id.getPath())));

            post = ItemDisplayElementUtil.createSimple(MODEL_POST);
            post.setScale(new Vector3f(1.00275f));
            addElement(post);
            for (Direction side : Direction.Type.HORIZONTAL) {
                sides.put(side, ItemDisplayElementUtil.createSimple(MODEL_SIDE));
                sides.get(side).setYaw(side.getPositiveHorizontalDegrees());
                sides.get(side).setScale(new Vector3f(1.00275f));
                addElement(sides.get(side));
            }
            this.updateItem(state);
        }

        private void updateItem(BlockState state) {
            setVisibility(sides.get(Direction.NORTH), state.get(HorizontalConnectingBlock.NORTH));
            setVisibility(sides.get(Direction.EAST), state.get(HorizontalConnectingBlock.EAST));
            setVisibility(sides.get(Direction.SOUTH), state.get(HorizontalConnectingBlock.SOUTH));
            setVisibility(sides.get(Direction.WEST), state.get(HorizontalConnectingBlock.WEST));
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
