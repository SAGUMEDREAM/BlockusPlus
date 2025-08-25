package cc.thonly.blockuspatch.block.base;

import eu.pb4.polymer.blocks.api.BlockModelType;
import eu.pb4.polymer.blocks.api.PolymerBlockModel;
import eu.pb4.polymer.blocks.api.PolymerBlockResourceUtils;
import eu.pb4.polymer.blocks.api.PolymerTexturedBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.Identifier;
import xyz.nucleoid.packettweaker.PacketContext;

public class CubeAllBlockImpl implements PolymerTexturedBlock {
    private final Identifier blockId;
    private final BlockState polymerBlockState;

    public CubeAllBlockImpl(Identifier blockId) {
        this.blockId = blockId;
        this.polymerBlockState = PolymerBlockResourceUtils.requestBlock(BlockModelType.FULL_BLOCK, PolymerBlockModel.of(Identifier.of(this.blockId.getNamespace(), "block/" + this.blockId.getPath())));
    }

    @Override
    public boolean forceLightUpdates(BlockState blockState) {
        return true;
    }

    @Override
    public BlockState getPolymerBlockState(BlockState blockState, PacketContext packetContext) {
        if (blockState == null) {
            return Blocks.STONE.getDefaultState();
        }
        return this.polymerBlockState;
    }
}
