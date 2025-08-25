package cc.thonly.blockuspatch.mixin;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.PillarBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(PillarBlock.class)
public class PillarBlockMixin {
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
