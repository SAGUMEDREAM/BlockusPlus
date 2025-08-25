package cc.thonly.blockusplus.mixin;

import cc.thonly.blockusplus.block.BlockEntry;
import com.brand.blockus.registry.content.bundles.*;
import net.minecraft.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = AsphaltBundle.Builder.class, remap = false)
public class AsphaltBundleMixin {
    @Inject(method = "register", at = @At("RETURN"))
    public void registerOverlay(CallbackInfoReturnable<AsphaltBundle> cir) {
        AsphaltBundle bundle = cir.getReturnValue();
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
