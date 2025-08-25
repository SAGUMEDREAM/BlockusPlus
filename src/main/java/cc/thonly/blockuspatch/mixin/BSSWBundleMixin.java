package cc.thonly.blockuspatch.mixin;

import cc.thonly.blockuspatch.block.BlockEntry;
import com.brand.blockus.registry.content.bundles.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = BSSWBundle.Builder.class, remap = false)
public class BSSWBundleMixin {
    @Inject(method = "register", at = @At("RETURN"))
    public void registerOverlay(CallbackInfoReturnable<BSSWBundle> cir) {
        BSSWBundle bundle = cir.getReturnValue();
        BlockEntry blockEntry = BlockEntry.of(bundle.block());
        blockEntry.registerOverlay(bundle.block());
        blockEntry.registerOverlay(bundle.stairs());
        blockEntry.registerOverlay(bundle.slab());
        if (bundle.wall() != null) {
            blockEntry.registerOverlay(bundle.wall());
        }
    }
}
