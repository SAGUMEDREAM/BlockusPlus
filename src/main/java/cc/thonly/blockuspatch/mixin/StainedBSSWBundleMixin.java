package cc.thonly.blockuspatch.mixin;

import cc.thonly.blockuspatch.block.BlockEntry;
import com.brand.blockus.registry.content.bundles.StainedBSSWBundle;
import net.minecraft.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = StainedBSSWBundle.Builder.class, remap = false)
public class StainedBSSWBundleMixin {
    @Inject(method = "register", at = @At("RETURN"))
    public void registerOverlay(CallbackInfoReturnable<StainedBSSWBundle> cir) {
        StainedBSSWBundle bundle = cir.getReturnValue();
        bundle.colorMap().forEach((dyeColor, variants) -> {
            Block block = variants.block();
            Block slab = variants.slab();
            Block stairs = variants.stairs();
            BlockEntry blockEntry = BlockEntry.of(block);
            blockEntry.registerOverlay(block);
            blockEntry.registerOverlay(slab);
            blockEntry.registerOverlay(stairs);
        });
    }
}
