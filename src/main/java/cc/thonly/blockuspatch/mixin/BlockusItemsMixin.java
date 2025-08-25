package cc.thonly.blockuspatch.mixin;

import cc.thonly.blockuspatch.item.ItemInit;
import com.brand.blockus.registry.content.BlockusItems;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Function;

@Mixin(value = BlockusItems.class)
public class BlockusItemsMixin {
    @Inject(method = "register(Ljava/lang/String;Ljava/util/function/Function;Lnet/minecraft/item/Item$Settings;)Lnet/minecraft/item/Item;", at = @At("RETURN"))
    private static void registerItem(String id, Function<Item.Settings, Item> factory, Item.Settings settings, CallbackInfoReturnable<Item> cir) {
        Item item = cir.getReturnValue();
        ItemInit.registerOverlay(item);
    }

    @Inject(method = "register(Lnet/minecraft/registry/RegistryKey;Ljava/util/function/Function;Lnet/minecraft/item/Item$Settings;)Lnet/minecraft/item/Item;", at = @At("RETURN"))
    private static void registerItem(RegistryKey<Item> key, Function<Item.Settings, Item> factory, Item.Settings settings, CallbackInfoReturnable<Item> cir) {
        Item item = cir.getReturnValue();
        ItemInit.registerOverlay(item);
    }
}
