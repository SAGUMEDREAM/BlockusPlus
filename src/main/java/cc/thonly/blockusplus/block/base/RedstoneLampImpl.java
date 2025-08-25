package cc.thonly.blockusplus.block.base;

import eu.pb4.polymer.blocks.api.BlockModelType;
import eu.pb4.polymer.blocks.api.PolymerBlockModel;
import eu.pb4.polymer.blocks.api.PolymerBlockResourceUtils;
import eu.pb4.polymer.blocks.api.PolymerTexturedBlock;
import lombok.Getter;
import net.minecraft.block.BlockState;
import net.minecraft.block.RedstoneLampBlock;
import net.minecraft.util.Identifier;
import xyz.nucleoid.packettweaker.PacketContext;

@Getter
public class RedstoneLampImpl implements PolymerTexturedBlock {
    private final Identifier blockId;
    private final BlockState[] model = new BlockState[2];

    public RedstoneLampImpl(Identifier blockId) {
        this.blockId = blockId;
        model[0] = PolymerBlockResourceUtils.requestBlock(BlockModelType.FULL_BLOCK, PolymerBlockModel.of(Identifier.of(blockId.getNamespace(), "block/" + blockId.getPath() + "_lit")));
        model[1] = PolymerBlockResourceUtils.requestBlock(BlockModelType.FULL_BLOCK, PolymerBlockModel.of(Identifier.of(blockId.getNamespace(), "block/" + blockId.getPath())));
    }

    @Override
    public boolean forceLightUpdates(BlockState blockState) {
        return true;
    }

    @Override
    public BlockState getPolymerBlockState(BlockState blockState, PacketContext packetContext) {
        return blockState.get(RedstoneLampBlock.LIT) ? model[0] : model[1];
    }
}
