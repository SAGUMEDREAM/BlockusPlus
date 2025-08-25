package cc.thonly.blockuspatch.mixin;

import cc.thonly.blockuspatch.block.BlockEntry;
import com.brand.blockus.registry.content.bundles.CopperBundle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = CopperBundle.Builder.class, remap = false)
public class CopperBundleMixin {
    @Inject(method = "register", at = @At("RETURN"))
    public void registerOverlay(CallbackInfoReturnable<CopperBundle> cir) {
        CopperBundle bundle = cir.getReturnValue();
        BlockEntry blockEntry = BlockEntry.of(bundle.block());
        blockEntry.registerOverlay(bundle.block());
        blockEntry.registerOverlay(bundle.stairs());
        blockEntry.registerOverlay(bundle.slab());
        blockEntry.registerOverlay(bundle.wall());
        blockEntry.registerOverlay(bundle.blockWaxed(), blockEntry.getPolymerBlock(bundle.block()));
        blockEntry.registerOverlay(bundle.stairsWaxed(), blockEntry.getPolymerBlock((bundle.stairs())));
        blockEntry.registerOverlay(bundle.slabWaxed(), blockEntry.getPolymerBlock((bundle.slab())));
        blockEntry.registerOverlay(bundle.wallWaxed(), blockEntry.getPolymerBlock((bundle.wall())));
    }
}
