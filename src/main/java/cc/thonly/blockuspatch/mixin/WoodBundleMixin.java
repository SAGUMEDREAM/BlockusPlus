package cc.thonly.blockuspatch.mixin;

import cc.thonly.blockuspatch.block.BlockEntry;
import com.brand.blockus.registry.content.bundles.WoodBundle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = WoodBundle.Builder.class, remap = false)
public class WoodBundleMixin {
    @Inject(method = "register", at = @At("RETURN"))
    public void registerOverlay(CallbackInfoReturnable<WoodBundle> cir) {
        WoodBundle bundle = cir.getReturnValue();
        BlockEntry blockEntry = BlockEntry.of(bundle.planks());
        blockEntry.registerOverlay(bundle.planks());
        blockEntry.registerOverlay(bundle.slab());
        blockEntry.registerOverlay(bundle.stairs());
        blockEntry.registerOverlay(bundle.button());
        blockEntry.registerOverlay(bundle.fence());
        blockEntry.registerOverlay(bundle.fenceGate());
        blockEntry.registerOverlay(bundle.pressurePlate());
        blockEntry.registerOverlay(bundle.door());
        blockEntry.registerOverlay(bundle.trapdoor());
        blockEntry.registerOverlay(bundle.standingSign());
        blockEntry.registerOverlay(bundle.wallSign());
        blockEntry.registerOverlay(bundle.ceilingHangingSign());
        blockEntry.registerOverlay(bundle.wallHangingSign());
    }
}
