package cc.thonly.blockuspatch.block.base;

import eu.pb4.factorytools.api.block.FactoryBlock;
import eu.pb4.factorytools.api.virtualentity.BlockModel;
import eu.pb4.factorytools.api.virtualentity.ItemDisplayElementUtil;
import eu.pb4.polymer.blocks.api.PolymerTexturedBlock;
import eu.pb4.polymer.virtualentity.api.ElementHolder;
import eu.pb4.polymer.virtualentity.api.elements.ItemDisplayElement;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LanternBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import xyz.nucleoid.packettweaker.PacketContext;

public class LanternImpl implements PolymerTexturedBlock, FactoryBlock {
    private final Identifier blockId;

    public LanternImpl(Identifier blockId) {
        this.blockId = blockId;
    }

    @Override
    public boolean forceLightUpdates(BlockState blockState) {
        return true;
    }

    @Override
    public BlockState getPolymerBlockState(BlockState blockState, PacketContext packetContext) {
        return Blocks.LANTERN.getStateWithProperties(blockState);
    }

    @Override
    public @Nullable ElementHolder createElementHolder(ServerWorld world, BlockPos pos, BlockState initialBlockState) {
        return new Model(initialBlockState, world, pos);
    }

    public final class Model extends BlockModel {
        public final ItemStack STANDING_MODEL = ItemDisplayElementUtil.getModel((Identifier.of(blockId.getNamespace(), "block/%s".formatted(blockId.getPath()))));
        public final ItemStack HANGING_MODEL = ItemDisplayElementUtil.getModel(Identifier.of(blockId.getNamespace(), "block/%s_hanging".formatted(blockId.getPath())));
        public ItemDisplayElement lantern;
        public ServerWorld world;
        public BlockPos pos;

        public Model(BlockState state, ServerWorld world, BlockPos pos) {
            this.world = world;
            this.pos = pos;
            init(state);
        }

        public void init(BlockState state) {
            boolean modelType = state.get(LanternBlock.HANGING);

            ItemStack model = modelType ? HANGING_MODEL : STANDING_MODEL;

            this.lantern = ItemDisplayElementUtil.createSimple(model);
            this.lantern.setScale(new Vector3f(2.01f));

            this.addElement(lantern);
        }
    }
}
