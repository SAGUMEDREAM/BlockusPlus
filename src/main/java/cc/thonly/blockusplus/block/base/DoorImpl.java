package cc.thonly.blockusplus.block.base;

import eu.pb4.factorytools.api.block.FactoryBlock;
import eu.pb4.factorytools.api.virtualentity.BlockModel;
import eu.pb4.factorytools.api.virtualentity.ItemDisplayElementUtil;
import eu.pb4.polymer.blocks.api.BlockModelType;
import eu.pb4.polymer.blocks.api.PolymerBlockResourceUtils;
import eu.pb4.polymer.blocks.api.PolymerTexturedBlock;
import eu.pb4.polymer.virtualentity.api.ElementHolder;
import eu.pb4.polymer.virtualentity.api.attachment.BlockBoundAttachment;
import eu.pb4.polymer.virtualentity.api.attachment.HolderAttachment;
import eu.pb4.polymer.virtualentity.api.elements.ItemDisplayElement;
import lombok.Getter;
import net.minecraft.block.BlockState;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.enums.DoorHinge;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;
import xyz.nucleoid.packettweaker.PacketContext;

@Getter
public class DoorImpl implements FactoryBlock, PolymerTexturedBlock {
    private static final BlockState NORTH_DOOR;
    private static final BlockState EAST_DOOR;
    private static final BlockState SOUTH_DOOR;
    private static final BlockState WEST_DOOR;
    static {
        NORTH_DOOR = PolymerBlockResourceUtils.requestEmpty(BlockModelType.NORTH_DOOR);
        EAST_DOOR = PolymerBlockResourceUtils.requestEmpty(BlockModelType.EAST_DOOR);
        SOUTH_DOOR = PolymerBlockResourceUtils.requestEmpty(BlockModelType.SOUTH_DOOR);
        WEST_DOOR = PolymerBlockResourceUtils.requestEmpty(BlockModelType.WEST_DOOR);
    }

    private final Identifier blockId;
    protected ItemStack MODEL_TOP_RIGHT;
    protected ItemStack MODEL_TOP_LEFT;
    protected ItemStack MODEL_BOTTOM_RIGHT;
    protected ItemStack MODEL_BOTTOM_LEFT;

    public DoorImpl(Identifier blockId) {
        this.blockId = blockId;
        MODEL_TOP_RIGHT = ItemDisplayElementUtil.getModel(Identifier.of(blockId.getNamespace(), "block/%s_top_left".formatted(blockId.getPath())));
        MODEL_TOP_LEFT = ItemDisplayElementUtil.getModel(Identifier.of(blockId.getNamespace(), "block/%s_top_right".formatted(blockId.getPath())));
        MODEL_BOTTOM_RIGHT = ItemDisplayElementUtil.getModel(Identifier.of(blockId.getNamespace(), "block/%s_bottom_left".formatted(blockId.getPath())));
        MODEL_BOTTOM_LEFT = ItemDisplayElementUtil.getModel(Identifier.of(blockId.getNamespace(), "block/%s_bottom_right".formatted(blockId.getPath())));
        if (blockId.toString().contains("_gate")) {
            MODEL_TOP_RIGHT = ItemDisplayElementUtil.getModel(Identifier.of(blockId.getNamespace(), "block/%s_top".formatted(blockId.getPath())));
            MODEL_TOP_LEFT = ItemDisplayElementUtil.getModel(Identifier.of(blockId.getNamespace(), "block/%s_top_hinge".formatted(blockId.getPath())));
            MODEL_BOTTOM_RIGHT = ItemDisplayElementUtil.getModel(Identifier.of(blockId.getNamespace(), "block/%s_bottom".formatted(blockId.getPath())));
            MODEL_BOTTOM_LEFT = ItemDisplayElementUtil.getModel(Identifier.of(blockId.getNamespace(), "block/%s_bottom_hinge".formatted(blockId.getPath())));
        }
    }

    @Override
    public BlockState getPolymerBlockState(BlockState state, PacketContext context) {
        if (!state.get(DoorBlock.OPEN)) {
            return switch (state.get(DoorBlock.FACING)) {
                case EAST -> EAST_DOOR;
                case WEST -> WEST_DOOR;
                case SOUTH -> SOUTH_DOOR;
                default -> NORTH_DOOR;
            };
        } else {
            if (state.get(DoorBlock.HINGE) == DoorHinge.LEFT) {
                return switch (state.get(DoorBlock.FACING)) {
                    case EAST -> SOUTH_DOOR;
                    case WEST -> NORTH_DOOR;
                    case SOUTH -> WEST_DOOR;
                    default -> EAST_DOOR;
                };
            } else {
                return switch (state.get(DoorBlock.FACING)) {
                    case EAST -> NORTH_DOOR;
                    case WEST -> SOUTH_DOOR;
                    case SOUTH -> EAST_DOOR;
                    default -> WEST_DOOR;
                };
            }
        }
    }

    @Override
    public @Nullable ElementHolder createElementHolder(ServerWorld world, BlockPos pos, BlockState initialBlockState) {
        return new Model(this, initialBlockState);
    }

    public static final class Model extends BlockModel {
        public DoorImpl doorImpl;
        public ItemDisplayElement main;

        public Model(DoorImpl doorImpl, BlockState state) {
            this.doorImpl = doorImpl;
            this.main = ItemDisplayElementUtil.createSimple();
            this.main.setTeleportDuration(0);
            this.main.setInterpolationDuration(0);
            this.updateItem(state);
            updateStatePos(state);
            addElement(this.main);
        }

        private void updateStatePos(BlockState state) {
            var rotation = state.get(DoorBlock.FACING).getPositiveHorizontalDegrees() + 270;
            var open = state.get(DoorBlock.OPEN);
            if (state.get(DoorBlock.HINGE).equals(DoorHinge.LEFT)) {
                rotation += open ? 90 : 0;
            } else {
                rotation += open ? 270 : 0;
            }
            main.setYaw(rotation);
        }

        private void updateItem(BlockState state) {
            boolean useRightModel = state.get(DoorBlock.HINGE).equals(DoorHinge.LEFT) ^ state.get(DoorBlock.OPEN);

            if (state.get(DoorBlock.HALF) == DoubleBlockHalf.UPPER)
                main.setItem(useRightModel ? doorImpl.MODEL_TOP_RIGHT : doorImpl.MODEL_TOP_LEFT);
            else
                main.setItem(useRightModel ? doorImpl.MODEL_BOTTOM_RIGHT : doorImpl.MODEL_BOTTOM_LEFT);
        }

        @Override
        public void notifyUpdate(HolderAttachment.UpdateType updateType) {
            if (updateType == BlockBoundAttachment.BLOCK_STATE_UPDATE) {
                updateStatePos(this.blockState());
                updateItem(this.blockState());
                this.tick();
            }
            super.notifyUpdate(updateType);
        }
    }
}
