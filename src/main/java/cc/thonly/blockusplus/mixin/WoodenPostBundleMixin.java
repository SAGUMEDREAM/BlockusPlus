package cc.thonly.blockusplus.mixin;

import cc.thonly.blockusplus.block.BlockEntry;
import com.brand.blockus.registry.content.bundles.WoodenPostBundle;
import net.minecraft.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = WoodenPostBundle.class, remap = false)
public class WoodenPostBundleMixin {
    @Inject(method = "register", at = @At("RETURN"))
    private static void registerOverlay(CallbackInfoReturnable<WoodenPostBundle> cir) {
        WoodenPostBundle bundle = cir.getReturnValue();
        for (Block block : bundle.all()) {
            BlockEntry blockEntry = BlockEntry.of(block);
            blockEntry.registerOverlay(block);
        }
    }
}
