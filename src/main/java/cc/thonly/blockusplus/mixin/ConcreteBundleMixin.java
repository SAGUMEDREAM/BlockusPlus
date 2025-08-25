package cc.thonly.blockusplus.mixin;

import cc.thonly.blockusplus.block.BlockEntry;
import com.brand.blockus.registry.content.bundles.ConcreteBundle;
import net.minecraft.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ConcreteBundle.Builder.class, remap = false)
public class ConcreteBundleMixin {
    @Inject(method = "register", at = @At("RETURN"))
    public void registerOverlay(CallbackInfoReturnable<ConcreteBundle> cir) {
        ConcreteBundle bundle = cir.getReturnValue();
        bundle.colorMap().forEach((dyeColor, variants) -> {
            Block block = variants.block();
            Block slab = variants.slab();
            Block stairs = variants.stairs();
            Block wall = variants.wall();
            Block chiseled = variants.chiseled();
            Block pillar = variants.pillar();
            BlockEntry blockEntry = BlockEntry.of(block);
            blockEntry.registerOverlay(block);
            blockEntry.registerOverlay(slab);
            blockEntry.registerOverlay(stairs);
            blockEntry.registerOverlay(wall);
            blockEntry.registerOverlay(chiseled);
            blockEntry.registerOverlay(pillar);
        });
    }
}
