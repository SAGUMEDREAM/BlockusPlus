package cc.thonly.blockuspatch.mixin;

import com.brand.blockus.utils.helper.BlockBuilder;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = BlockBuilder.class, remap = false)
public class BlockBuilderMixin {
//    @Inject(method = "register(Ljava/lang/String;Ljava/util/function/Function;)Lnet/minecraft/block/Block;", at = @At("RETURN"), cancellable = true)
//    public void registerHook(String id, Function<Block, Item> itemFactory, CallbackInfoReturnable<Block> cir) {
//        Block block = cir.getReturnValue();
////        BlockEntry.create(block);
//    }
}
