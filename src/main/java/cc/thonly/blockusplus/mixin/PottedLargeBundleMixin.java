package cc.thonly.blockusplus.mixin;

import cc.thonly.blockusplus.block.BlockEntry;
import com.brand.blockus.registry.content.bundles.CopperBundle;
import com.brand.blockus.registry.content.bundles.PottedLargeBundle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = PottedLargeBundle.Builder.class, remap = false)
public class PottedLargeBundleMixin {
    @Inject(method = "register", at = @At("RETURN"))
    public void registerOverlay(CallbackInfoReturnable<PottedLargeBundle> cir) {
        PottedLargeBundle bundle = cir.getReturnValue();
        BlockEntry blockEntry = BlockEntry.of(bundle.block());
        blockEntry.registerOverlay(bundle.block());
    }
}
