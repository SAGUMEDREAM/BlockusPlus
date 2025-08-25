package cc.thonly.blockusplus.mixin;

import cc.thonly.blockusplus.block.BlockEntry;
import com.brand.blockus.registry.content.bundles.TimberFrameBundle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = TimberFrameBundle.class, remap = false)
public class TimberFrameBundleMixin {
    @Inject(method = "register", at = @At("TAIL"))
    private static void registerOverlay(CallbackInfoReturnable<TimberFrameBundle> cir) {
        TimberFrameBundle bundle = cir.getReturnValue();
        bundle.woodMap().forEach((woodMaps, timberFrameVariants) -> {
            BlockEntry blockEntry = BlockEntry.of(timberFrameVariants.block());
            blockEntry.registerOverlay(timberFrameVariants.block());
            blockEntry.registerOverlay(timberFrameVariants.cross());
            blockEntry.registerOverlay(timberFrameVariants.diagonal());
            blockEntry.registerOverlay(timberFrameVariants.grate());
            blockEntry.registerOverlay(timberFrameVariants.lattice());
        });
    }
}
