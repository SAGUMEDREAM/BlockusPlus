package cc.thonly.blockuspatch.mixin;

import cc.thonly.blockuspatch.block.PolymerBlockHelper;
import com.terraformersmc.terraform.sign.api.block.TerraformSignBlockHelper;
import net.minecraft.block.AbstractSignBlock;
import net.minecraft.block.Block;
import net.minecraft.registry.RegistryKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TerraformSignBlockHelper.class)
public abstract class TerraformSignBlockHelperMixin {
    @Inject(
        method = "registerSignBlock(Lnet/minecraft/registry/RegistryKey;Lnet/minecraft/block/AbstractSignBlock;)Lnet/minecraft/block/AbstractSignBlock;",
        at = @At("RETURN")
    )
    private static <T extends AbstractSignBlock> void polymerifySign(RegistryKey<Block> key, T block, CallbackInfoReturnable<T> cir) {
        PolymerBlockHelper.registerPolymerBlock(key.getValue().getPath(), block);
    }
}
