package cc.thonly.blockusplus.block.base;

import eu.pb4.factorytools.api.block.FactoryBlock;
import eu.pb4.factorytools.api.virtualentity.BlockModel;
import eu.pb4.factorytools.api.virtualentity.ItemDisplayElementUtil;
import eu.pb4.polymer.blocks.api.PolymerBlockModel;
import eu.pb4.polymer.blocks.api.PolymerTexturedBlock;
import eu.pb4.polymer.virtualentity.api.ElementHolder;
import eu.pb4.polymer.virtualentity.api.attachment.BlockBoundAttachment;
import eu.pb4.polymer.virtualentity.api.attachment.HolderAttachment;
import eu.pb4.polymer.virtualentity.api.elements.ItemDisplayElement;
import lombok.Getter;
import net.minecraft.block.*;
import net.minecraft.block.enums.SlabType;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import xyz.nucleoid.packettweaker.PacketContext;

@Getter
public class SlabImpl implements FactoryBlock, PolymerTexturedBlock {
    private static final BlockState TOP_SLAB = Blocks.MANGROVE_SLAB.getDefaultState()
            .with(SlabBlock.TYPE, SlabType.TOP);
    private static final BlockState TOP_SLAB_WATERLOGGED = Blocks.MANGROVE_SLAB.getDefaultState()
            .with(SlabBlock.TYPE, SlabType.TOP)
            .with(SlabBlock.WATERLOGGED, true);
    private static final BlockState BOTTOM_SLAB = Blocks.MANGROVE_SLAB.getDefaultState()
            .with(SlabBlock.TYPE, SlabType.BOTTOM);
    private static final BlockState BOTTOM_SLAB_WATERLOGGED = Blocks.MANGROVE_SLAB.getDefaultState()
            .with(SlabBlock.TYPE, SlabType.BOTTOM)
            .with(SlabBlock.WATERLOGGED, true);
    private final Identifier baseId;
    private final BlockState baseState;
    private final Identifier blockId;
    private final SlabBlock slabBlock;
    private final PolymerBlockModel topSlabModel;
    private final PolymerBlockModel topSlabWaterLoggedModel;
    private final PolymerBlockModel bottomSlabModel;
    private final PolymerBlockModel bottomSlabWaterLoggedModel;

    public SlabImpl(Identifier blockId, Block base, SlabBlock slabBlock) {
        this.baseId = Registries.BLOCK.getId(base);
        this.baseState = base.getDefaultState();
        this.blockId = blockId;
        this.slabBlock = slabBlock;
        this.topSlabModel = PolymerBlockModel.of(Identifier.of(this.blockId.getNamespace(), "block/" + this.blockId.getPath() + "_top"));
        this.topSlabWaterLoggedModel = PolymerBlockModel.of(Identifier.of(this.blockId.getNamespace(), "block/" + this.blockId.getPath() + "_top"));
        this.bottomSlabModel = PolymerBlockModel.of(Identifier.of(this.blockId.getNamespace(), "block/" + this.blockId.getPath()));
        this.bottomSlabWaterLoggedModel = PolymerBlockModel.of(Identifier.of(this.blockId.getNamespace(), "block/" + this.blockId.getPath()));
    }

    @Override
    public BlockState getPolymerBlockState(BlockState state, PacketContext context) {
        return switch (state.get(SlabBlock.TYPE)) {
            case SlabType.TOP -> state.get(SlabBlock.WATERLOGGED) ? TOP_SLAB_WATERLOGGED : TOP_SLAB;
            case SlabType.BOTTOM -> state.get(SlabBlock.WATERLOGGED) ? BOTTOM_SLAB_WATERLOGGED : BOTTOM_SLAB;
            default -> this.baseState.getBlock().getDefaultState();
        };
    }

    @Override
    public @Nullable ElementHolder createElementHolder(ServerWorld world, BlockPos pos, BlockState initialBlockState) {
        return new Model(this, initialBlockState, this.blockId);
    }

    @Getter
    public static final class Model extends BlockModel {
        private final SlabImpl self;
        private final BlockState state;
        private final Identifier id;
        private final ItemStack topSlabModel;
        private final ItemStack topSlabWaterLoggedModel;
        private final ItemStack bottomSlabModel;
        private final ItemStack bottomSlabWaterLoggedModel;
        public ItemDisplayElement main;

        public Model(SlabImpl slabImpl, BlockState state, Identifier id) {
            this.self = slabImpl;
            this.state = state;
            this.id = id;
            this.topSlabModel = ItemDisplayElementUtil.getModel(slabImpl.getTopSlabModel().model());
            this.topSlabWaterLoggedModel = ItemDisplayElementUtil.getModel(slabImpl.getTopSlabModel().model());
            this.bottomSlabModel = ItemDisplayElementUtil.getModel(slabImpl.getBottomSlabWaterLoggedModel().model());
            this.bottomSlabWaterLoggedModel = ItemDisplayElementUtil.getModel(slabImpl.getBottomSlabWaterLoggedModel().model());
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

            SlabType slabType = state.get(SlabBlock.TYPE);
            boolean shouldShow = slabType == SlabType.TOP || slabType == SlabType.BOTTOM;

            if (!shouldShow) return;

            this.main = (slabType == SlabType.TOP)
                    ? new ItemDisplayElement(this.topSlabModel)
                    : new ItemDisplayElement(this.bottomSlabModel);

            final float uniformScale = 1.004f;
            if (slabType == SlabType.TOP) {
                this.main.setScale(new Vector3f(uniformScale, uniformScale + 0.001f, uniformScale));
                this.main.setOffset(new Vec3d(0, ((uniformScale - 1) / 4) - 0.002, 0));
            }
            if (slabType == SlabType.BOTTOM) {
                this.main.setScale(new Vector3f(uniformScale, uniformScale + 0.001f, uniformScale));
                this.main.setOffset(new Vec3d(0, (uniformScale - 1) / 4, 0));
            }

            this.addElement(this.main);
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
