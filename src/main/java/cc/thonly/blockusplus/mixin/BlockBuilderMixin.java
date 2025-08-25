package cc.thonly.blockusplus.mixin;

import cc.thonly.blockusplus.block.BlockEntry;
import com.brand.blockus.utils.helper.BlockBuilder;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Function;

@Mixin(value = BlockBuilder.class, remap = false)
public class BlockBuilderMixin {
//    @Inject(method = "register(Ljava/lang/String;Ljava/util/function/Function;)Lnet/minecraft/block/Block;", at = @At("RETURN"), cancellable = true)
//    public void registerHook(String id, Function<Block, Item> itemFactory, CallbackInfoReturnable<Block> cir) {
//        Block block = cir.getReturnValue();
////        BlockEntry.create(block);
//    }
}
