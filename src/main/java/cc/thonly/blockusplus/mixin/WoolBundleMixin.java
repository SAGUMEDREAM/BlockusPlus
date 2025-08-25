package cc.thonly.blockusplus.mixin;

import cc.thonly.blockusplus.block.BlockEntry;
import com.brand.blockus.registry.content.bundles.WoolBundle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(value = WoolBundle.Builder.class, remap = false)
public class WoolBundleMixin {
    @Inject(method = "register", at = @At("RETURN"))
    private static void registerOverlay(CallbackInfoReturnable<WoolBundle> cir) {
        WoolBundle bundle = cir.getReturnValue();
        bundle.colorMap().forEach((dyeColor, variants) -> {
            BlockEntry blockEntry = BlockEntry.of(variants.block());
            blockEntry.registerOverlay(variants.block());
            blockEntry.registerOverlay(variants.stairs());
            blockEntry.registerOverlay(variants.slab());
            blockEntry.registerOverlay(variants.carpet());
        });
    }
}
