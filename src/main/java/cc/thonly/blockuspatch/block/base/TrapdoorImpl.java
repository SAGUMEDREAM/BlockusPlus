package cc.thonly.blockuspatch.block.base;

import eu.pb4.factorytools.api.block.FactoryBlock;
import eu.pb4.factorytools.api.virtualentity.BlockModel;
import eu.pb4.factorytools.api.virtualentity.ItemDisplayElementUtil;
import eu.pb4.polymer.blocks.api.BlockModelType;
import eu.pb4.polymer.blocks.api.PolymerBlockModel;
import eu.pb4.polymer.blocks.api.PolymerBlockResourceUtils;
import eu.pb4.polymer.blocks.api.PolymerTexturedBlock;
import eu.pb4.polymer.virtualentity.api.ElementHolder;
import eu.pb4.polymer.virtualentity.api.attachment.BlockBoundAttachment;
import eu.pb4.polymer.virtualentity.api.attachment.HolderAttachment;
import eu.pb4.polymer.virtualentity.api.elements.ItemDisplayElement;
import lombok.Getter;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.TrapdoorBlock;
import net.minecraft.block.enums.BlockHalf;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import xyz.nucleoid.packettweaker.PacketContext;

import java.util.Locale;
import java.util.Map;

@Getter
public class TrapdoorImpl implements FactoryBlock, PolymerTexturedBlock {
    public static final Map<Direction, BlockState> STATES_REGULAR = Util.mapEnum(Direction.class, x -> PolymerBlockResourceUtils.requestEmpty(BlockModelType.valueOf(switch (x) {
        case UP -> "BOTTOM";
        case DOWN -> "TOP";
        default -> x.asString().toUpperCase(Locale.ROOT);
    } + "_TRAPDOOR")));
    public static final Map<Direction, BlockState> STATES_WATERLOGGED = Util.mapEnum(Direction.class, x -> PolymerBlockResourceUtils.requestEmpty(BlockModelType.valueOf(switch (x) {
        case UP -> "BOTTOM";
        case DOWN -> "TOP";
        default -> x.asString().toUpperCase(Locale.ROOT);
    } + "_TRAPDOOR_WATERLOGGED")));
    private final Identifier blockId;
    private final PolymerBlockModel openModel;
    private final PolymerBlockModel bottomModel;
    private final PolymerBlockModel topModel;

    public TrapdoorImpl(Identifier blockId) {
        this.blockId = blockId;
        this.openModel = PolymerBlockModel.of(Identifier.of(blockId.getNamespace(), "block/" + blockId.getPath() + "_open"));
        this.bottomModel = PolymerBlockModel.of(Identifier.of(blockId.getNamespace(), "block/" + blockId.getPath() + "_bottom"));
        this.topModel = PolymerBlockModel.of(Identifier.of(blockId.getNamespace(), "block/" + blockId.getPath() + "_top"));
    }

    @Override
    public BlockState getPolymerBlockState(BlockState blockState, PacketContext packetContext) {
        var map = (blockState.get(TrapdoorBlock.WATERLOGGED) ? STATES_WATERLOGGED : STATES_REGULAR);

        if (blockState.get(TrapdoorBlock.OPEN)) {
            return map.get(blockState.get(TrapdoorBlock.FACING));
        }

        return map.get(blockState.get(TrapdoorBlock.HALF) == BlockHalf.BOTTOM ? Direction.UP : Direction.DOWN);
    }

    @Override
    public boolean isIgnoringBlockInteractionPlaySoundExceptedEntity(BlockState state, ServerPlayerEntity player, Hand hand, ItemStack stack, ServerWorld world, BlockHitResult blockHitResult) {
        return true;
    }

    @Override
    public ElementHolder createElementHolder(ServerWorld world, BlockPos pos, BlockState initialBlockState) {
        return new Model(this, initialBlockState, this.blockId);
    }

    @Getter
    public static final class Model extends BlockModel {
        private final TrapdoorImpl trapdoorImpl;
        private final BlockState state;
        private final Identifier id;
        public final ItemStack openModel;
        public final ItemStack bottomModel;
        public final ItemStack topModel;
        public ItemDisplayElement main;

        public Model(TrapdoorImpl trapdoorImpl, BlockState state, Identifier id) {
            this.trapdoorImpl = trapdoorImpl;
            this.state = state;
            this.id = id;
            this.openModel = ItemDisplayElementUtil.getModel(trapdoorImpl.getOpenModel().model());
            this.bottomModel = ItemDisplayElementUtil.getModel(trapdoorImpl.getBottomModel().model());
            this.topModel = ItemDisplayElementUtil.getModel(trapdoorImpl.getTopModel().model());
            this.init();
        }

        public void init() {
            update(this.state);
        }

        public void update(BlockState state) {
            if (this.main != null) {
                this.removeElement(this.main);
                this.main = null;
            }
            ItemDisplayElement element = null;
            boolean open = state.get(TrapdoorBlock.OPEN);
            Direction direction = state.get(HorizontalFacingBlock.FACING);
            BlockHalf blockHalf = state.get(TrapdoorBlock.HALF);
            element = switch (blockHalf) {
                case BlockHalf.TOP -> new ItemDisplayElement(this.topModel);
                case BlockHalf.BOTTOM -> new ItemDisplayElement(this.bottomModel);
                default -> new ItemDisplayElement(this.topModel);
            };
            if (open) {
                element = new ItemDisplayElement(this.openModel);
            }

            element.setYaw(direction.getPositiveHorizontalDegrees());
            this.main = element;
            this.addElement(element);
        }

        @Override
        public void notifyUpdate(HolderAttachment.UpdateType updateType) {
            if (updateType == BlockBoundAttachment.BLOCK_STATE_UPDATE) {
                update(this.blockState());
                this.tick();
            }
            super.notifyUpdate(updateType);
        }
    }
}
