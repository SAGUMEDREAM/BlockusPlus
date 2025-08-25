package cc.thonly.blockusplus.mixin;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.HorizontalFacingBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(HorizontalFacingBlock.class)
public class HorizontalFacingBlockMixin {
    @ModifyVariable(
            method = "<init>",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/Block;<init>(Lnet/minecraft/block/AbstractBlock$Settings;)V"
            ),
            argsOnly = true)
    private static AbstractBlock.Settings init(AbstractBlock.Settings value) {
        if (value.registryKey == null) {
            return value;
        }
        if (value.registryKey.getValue().getNamespace().equals("blockus")) {
            return value.nonOpaque();
        }
        return value;
    }
}
