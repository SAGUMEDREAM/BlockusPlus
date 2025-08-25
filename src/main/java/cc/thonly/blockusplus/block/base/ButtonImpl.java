package cc.thonly.blockusplus.block.base;

import eu.pb4.factorytools.api.block.FactoryBlock;
import eu.pb4.factorytools.api.virtualentity.BlockModel;
import eu.pb4.factorytools.api.virtualentity.ItemDisplayElementUtil;
import eu.pb4.polymer.blocks.api.PolymerTexturedBlock;
import eu.pb4.polymer.virtualentity.api.ElementHolder;
import eu.pb4.polymer.virtualentity.api.attachment.BlockBoundAttachment;
import eu.pb4.polymer.virtualentity.api.attachment.HolderAttachment;
import eu.pb4.polymer.virtualentity.api.elements.ItemDisplayElement;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ButtonBlock;
import net.minecraft.block.enums.BlockFace;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RotationAxis;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import xyz.nucleoid.packettweaker.PacketContext;

public class ButtonImpl implements FactoryBlock, PolymerTexturedBlock {
    private static final Block TEMPLATE = Blocks.MANGROVE_BUTTON;
    private final Identifier blockId;

    public ButtonImpl(Identifier blockId) {
        this.blockId = blockId;
    }

    @Override
    public BlockState getPolymerBlockState(BlockState state, PacketContext packetContext) {
        return TEMPLATE.getDefaultState()
                .with(ButtonBlock.FACING, state.get(ButtonBlock.FACING))
                .with(ButtonBlock.FACE, state.get(ButtonBlock.FACE))
                .with(ButtonBlock.POWERED, state.get(ButtonBlock.POWERED));
    }

    @Override
    public @Nullable ElementHolder createElementHolder(ServerWorld world, BlockPos pos, BlockState initialBlockState) {
        return new Model(initialBlockState, this.blockId);
    }

    public static class Model extends BlockModel {
        public final ItemStack MODEL_UNPOWERED;
        public final ItemStack MODEL_POWERED;
        public ItemDisplayElement main;

        public Model(BlockState state, Identifier id) {
            MODEL_UNPOWERED = ItemDisplayElementUtil.getModel(Identifier.of(id.getNamespace(), "block/" + id.getPath()));
            MODEL_POWERED = ItemDisplayElementUtil.getModel(Identifier.of(id.getNamespace(), "block/" + id.getPath() + "_pressed"));

            main = ItemDisplayElementUtil.createSimple();
            this.updateItem(state);
            addElement(main);
        }

        private void updateItem(BlockState state) {
            main.setItem(state.get(ButtonBlock.POWERED) ? MODEL_POWERED : MODEL_UNPOWERED);
            if (state.get(ButtonBlock.FACE) == BlockFace.WALL)
                main.setRightRotation(state.get(ButtonBlock.FACING).getRotationQuaternion());
            else if (state.get(ButtonBlock.FACE) == BlockFace.CEILING)
                main.setRightRotation(RotationAxis.POSITIVE_Z.rotationDegrees(180).mul(RotationAxis.POSITIVE_Y.rotationDegrees(state.get(ButtonBlock.FACING).getPositiveHorizontalDegrees())));
            else
                main.setRightRotation(RotationAxis.POSITIVE_Y.rotationDegrees(state.get(ButtonBlock.FACING).getPositiveHorizontalDegrees()));

            float scale = 1.00125f;
            main.setScale(new Vector3f(scale));
            float scaleOffset = (scale - 1) / 2;
            if (state.get(ButtonBlock.FACE) == BlockFace.WALL)
                main.setTranslation(new Vector3f(scaleOffset, scaleOffset, scaleOffset).mul(state.get(ButtonBlock.FACING).getUnitVector()));
            else
                main.setTranslation(new Vector3f(0, state.get(ButtonBlock.FACE) == BlockFace.FLOOR ? scaleOffset : -scaleOffset, 0));
        }

        @Override
        public void notifyUpdate(HolderAttachment.UpdateType updateType) {
            if (updateType == BlockBoundAttachment.BLOCK_STATE_UPDATE) {
                updateItem(this.blockState());
                this.tick();
            }
            super.notifyUpdate(updateType);
        }
    }
}
