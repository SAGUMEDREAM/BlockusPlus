package cc.thonly.blockusplus.mixin;

import cc.thonly.blockusplus.block.BlockEntry;
import com.brand.blockus.registry.content.bundles.StainedBSSWBundle;
import com.brand.blockus.registry.content.bundles.StainedBlockBundle;
import net.minecraft.block.Block;
import net.minecraft.util.DyeColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;
import java.util.function.BiConsumer;

@Mixin(value = StainedBlockBundle.class, remap = false)
public class StainedBlockBundleMixin {
    @Inject(method = "<init>", at = @At("RETURN"))
    public void constructor(Map<DyeColor, Block> colorMap, CallbackInfo ci) {
        colorMap.forEach((dyeColor, block) -> {
            BlockEntry blockEntry = BlockEntry.of(block);
            blockEntry.registerOverlay(block);
        });
    }
}
