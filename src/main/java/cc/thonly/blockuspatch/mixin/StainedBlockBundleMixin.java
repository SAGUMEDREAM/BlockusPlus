package cc.thonly.blockuspatch.mixin;

import cc.thonly.blockuspatch.block.BlockEntry;
import com.brand.blockus.registry.content.bundles.StainedBlockBundle;
import net.minecraft.block.Block;
import net.minecraft.util.DyeColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

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
