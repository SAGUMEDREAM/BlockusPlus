package cc.thonly.blockuspatch.mixin;

import cc.thonly.blockuspatch.item.BlockusCreativeTabs;
import com.brand.blockus.itemgroups.BlockusItemGroups;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = BlockusItemGroups.class, remap = false)
public class BlockusItemGroupsMixin {
    @Inject(method = "init",at = @At("HEAD"), cancellable = true)
    private static void init(CallbackInfo ci) {
        BlockusCreativeTabs.bootstrap();
        ci.cancel();
    }
}
