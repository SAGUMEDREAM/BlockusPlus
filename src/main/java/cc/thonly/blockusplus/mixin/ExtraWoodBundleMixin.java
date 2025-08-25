package cc.thonly.blockusplus.mixin;

import cc.thonly.blockusplus.block.BlockEntry;
import com.brand.blockus.registry.content.bundles.BSSWBundle;
import com.brand.blockus.registry.content.bundles.ExtraWoodBundle;
import net.minecraft.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(value = ExtraWoodBundle.class, remap = false)
public class ExtraWoodBundleMixin<T> {
    @Inject(method = "<init>", at = @At("TAIL"))
    public void constructor(Map<String, T> blocks, CallbackInfo ci) {
        blocks.forEach((string, t) -> {
            if (t instanceof Block block) {
                BlockEntry.simple(block);
            }
            if (t instanceof BSSWBundle bundle) {
                Block block = bundle.block();
                BlockEntry blockEntry = BlockEntry.of(block);
            }
        });
    }
}
