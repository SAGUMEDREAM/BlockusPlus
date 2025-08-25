package cc.thonly.blockuspatch.block.base;

import eu.pb4.factorytools.api.block.FactoryBlock;
import eu.pb4.polymer.blocks.api.BlockModelType;
import eu.pb4.polymer.blocks.api.PolymerBlockModel;
import eu.pb4.polymer.blocks.api.PolymerBlockResourceUtils;
import eu.pb4.polymer.blocks.api.PolymerTexturedBlock;
import lombok.Getter;
import net.minecraft.block.BlockState;
import net.minecraft.block.PillarBlock;
import net.minecraft.util.Identifier;
import xyz.nucleoid.packettweaker.PacketContext;

@Getter
public class PillarImpl implements PolymerTexturedBlock, FactoryBlock {
    private final Identifier blockId;
    private final BlockState[] model = new BlockState[3];

    public PillarImpl(Identifier blockId) {
        this.blockId = blockId;
        model[0] = PolymerBlockResourceUtils.requestBlock(BlockModelType.FULL_BLOCK, PolymerBlockModel.of(Identifier.of(blockId.getNamespace(), "block/" + blockId.getPath()), 90, 90));
        model[1] = PolymerBlockResourceUtils.requestBlock(BlockModelType.FULL_BLOCK, PolymerBlockModel.of(Identifier.of(blockId.getNamespace(), "block/" + blockId.getPath()), 0, 0));
        model[2] = PolymerBlockResourceUtils.requestBlock(BlockModelType.FULL_BLOCK, PolymerBlockModel.of(Identifier.of(blockId.getNamespace(), "block/" + blockId.getPath()), 90, 0));
    }

    @Override
    public BlockState getPolymerBlockState(BlockState state, PacketContext context) {
        return switch (state.get(PillarBlock.AXIS)) {
            case X -> model[0];
            case Y -> model[1];
            case Z -> model[2];
        };
    }

}
