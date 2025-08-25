package cc.thonly.blockusplus.block.base;

import eu.pb4.polymer.blocks.api.PolymerTexturedBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.Identifier;
import xyz.nucleoid.packettweaker.PacketContext;

public class NullBlockImpl implements PolymerTexturedBlock {
    private final Identifier blockId;

    public NullBlockImpl(Identifier blockId) {
        this.blockId = blockId;
    }

    @Override
    public BlockState getPolymerBlockState(BlockState blockState, PacketContext packetContext) {
        return Blocks.STONE.getDefaultState();
    }
}
