package cc.thonly.blockuspatch.block.base;

import com.brand.blockus.blocks.base.CookieBlock;
import eu.pb4.factorytools.api.block.FactoryBlock;
import eu.pb4.factorytools.api.block.model.generic.BSMMParticleBlock;
import eu.pb4.factorytools.api.block.model.generic.BlockStateModel;
import eu.pb4.polymer.blocks.api.PolymerTexturedBlock;
import eu.pb4.polymer.virtualentity.api.ElementHolder;
import net.minecraft.block.*;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;
import xyz.nucleoid.packettweaker.PacketContext;

public record CookiePolymerBlock() implements FactoryBlock, PolymerTexturedBlock, BSMMParticleBlock {
    public static final CookiePolymerBlock INSTANCE = new CookiePolymerBlock();

    @Override
    public BlockState getPolymerBlockState(BlockState blockState, PacketContext packetContext) {
        Integer bytes = blockState.get(CookieBlock.BITES);
        if (bytes == null || bytes == 0) {
            return Blocks.BARRIER.getDefaultState();
        }
        return Blocks.SNOW.getDefaultState().with(SnowBlock.LAYERS, MathHelper.clamp(8 - bytes, 1, 7));
    }

    @Override
    public @Nullable ElementHolder createElementHolder(ServerWorld world, BlockPos pos, BlockState initialBlockState) {
        return BlockStateModel.longRange(initialBlockState, pos);
    }

    @Override
    public boolean isIgnoringBlockInteractionPlaySoundExceptedEntity(BlockState state, ServerPlayerEntity player, Hand hand, ItemStack stack, ServerWorld world, BlockHitResult blockHitResult) {
        return true;
    }

}
