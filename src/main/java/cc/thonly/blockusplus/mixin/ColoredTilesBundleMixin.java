package cc.thonly.blockusplus.mixin;

import cc.thonly.blockusplus.block.BlockEntry;
import com.brand.blockus.registry.content.bundles.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ColoredTilesBundle.Builder.class, remap = false)
public class ColoredTilesBundleMixin {
    @Inject(method = "register", at = @At("RETURN"))
    public void registerOverlay(CallbackInfoReturnable<ColoredTilesBundle> cir) {
        ColoredTilesBundle bundle = cir.getReturnValue();
        BlockEntry blockEntry = BlockEntry.of(bundle.block());
        blockEntry.registerOverlay(bundle.block());
    }
}
