package cc.thonly.blockuspatch.block.base;

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
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import xyz.nucleoid.packettweaker.PacketContext;

@Getter
public class TransparentPillarImpl implements FactoryBlock, PolymerTexturedBlock {
    private final Identifier blockId;
    private final Identifier model;

    public TransparentPillarImpl(Identifier blockId) {
        this.blockId = blockId;
        this.model = Identifier.of(blockId.getNamespace(), "block/" + blockId.getPath());
//        model[0] = PolymerBlockModel.of(), 90, 90);
//        model[1] = PolymerBlockModel.of(), 0, 0);
//        model[2] = PolymerBlockModel.of(Identifier.of(blockId.getNamespace(), "block/" + blockId.getPath()), 90, 0);
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
        return new Model(world, pos, initialBlockState);
    }

    public static class Model extends BlockModel {
        private ItemDisplayElement main;
        private World world;
        private BlockPos pos;

        public Model(World world, BlockPos pos, BlockState state) {
            this.world = world;
            this.pos = pos;
            init(state);
        }

        private void init(BlockState state) {
            main = ItemDisplayElementUtil.createSimple(state.getBlock().asItem());
            switch (state.get(Properties.AXIS)) {
                case X: {
                    main.setYaw(90);
                    main.setPitch(90);
                    break;
                }
                case Z: {
                    main.setPitch(90);
                    break;
                }
            }
            main.setScale(new Vector3f(2f));
            addElement(main);
        }

        private void updateItem(BlockState state) {
            this.removeElement(main);
            init(state);
        }

        @Override
        public void notifyUpdate(HolderAttachment.UpdateType updateType) {
            if (updateType == BlockBoundAttachment.BLOCK_STATE_UPDATE) {
                updateItem(this.blockState());
            }
            super.notifyUpdate(updateType);
        }
    }
}
