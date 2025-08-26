package cc.thonly.blockuspatch.mixin;

import cc.thonly.blockuspatch.BlockusPolymerPatch;
import com.brand.blockus.Blockus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Blockus.class, remap = false)
public class BlockusMixin {
    @Inject(method = "onInitialize", at = @At("TAIL"))
    public void onInitialize(CallbackInfo ci) {
        BlockusPolymerPatch.LATE_INIT.forEach(Runnable::run);
        BlockusPolymerPatch.LATE_INIT.clear();
    }
}
