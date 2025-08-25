package cc.thonly.blockuspatch.block.base.mod;

import com.brand.blockus.blocks.base.Barrier;
import eu.pb4.factorytools.api.block.FactoryBlock;
import eu.pb4.factorytools.api.virtualentity.BlockModel;
import eu.pb4.factorytools.api.virtualentity.ItemDisplayElementUtil;
import eu.pb4.polymer.blocks.api.PolymerTexturedBlock;
import eu.pb4.polymer.virtualentity.api.ElementHolder;
import eu.pb4.polymer.virtualentity.api.elements.ItemDisplayElement;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import xyz.nucleoid.packettweaker.PacketContext;

import java.util.Random;

public class BarrierImpl implements FactoryBlock, PolymerTexturedBlock {
    private final Identifier blockId;
    private final Barrier barrier;

    public BarrierImpl(Identifier blockId, Barrier barrier) {
        this.blockId = blockId;
        this.barrier = barrier;
    }

    @Override
    public @Nullable ElementHolder createElementHolder(ServerWorld world, BlockPos pos, BlockState initialBlockState) {
        return new Model(world, initialBlockState, pos);
    }

    @Override
    public BlockState getPolymerBlockState(BlockState blockState, PacketContext packetContext) {
        return Blocks.BARRIER.getDefaultState();
    }

    public class Model extends BlockModel {
        private final ServerWorld world;
        private final BlockState initialBlockState;
        private final BlockPos pos;
        private final ItemStack itemStack;
        private final ItemDisplayElement element;

        public Model(ServerWorld world, BlockState initialBlockState, BlockPos pos) {
            this.world = world;
            this.initialBlockState = initialBlockState;
            this.pos = pos;
            this.itemStack = ItemDisplayElementUtil.getModel(BarrierImpl.this.barrier.asItem());
            this.element = ItemDisplayElementUtil.createSimple(this.itemStack);
            this.init();
        }

        public void init() {
            if (this.element != null) {
                this.removeElement(this.element);
            }
            long seed = this.world.getSeed();
            long posNum = pos.asLong();
            long resultSeed = seed + posNum;
            Random random = new Random(resultSeed);
            if (this.element != null) {
                this.element.setScale(new Vector3f(1.5f, 1.5f, 1.5f));
                this.element.setRotation(0, random.nextLong(364));
                this.element.setOffset(new Vec3d(0, -0.1, 0));
            }
            this.addElement(this.element);
        }
    }
}
