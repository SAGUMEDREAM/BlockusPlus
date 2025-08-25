package cc.thonly.blockusplus.mixin;

import cc.thonly.blockusplus.item.ItemInit;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Registry.class)
public interface RegistryMixin {
    @Inject(method = "register(Lnet/minecraft/registry/Registry;Lnet/minecraft/registry/RegistryKey;Ljava/lang/Object;)Ljava/lang/Object;", at = @At("RETURN"))
    private static<V, T extends V> void onRegisterItem(Registry<V> registry, RegistryKey<V> key, T entry, CallbackInfoReturnable<T> cir) {
        if (!registry.equals(Registries.ITEM)) {
            return;
        }
        if (!key.getValue().getNamespace().equals("blockus")) {
            return;
        }
        Item item = (Item) entry;
        ItemInit.registerOverlay(item);
    }
    @Inject(method = "register(Lnet/minecraft/registry/Registry;Lnet/minecraft/registry/RegistryKey;Ljava/lang/Object;)Ljava/lang/Object;", at = @At("RETURN"))
    private static<V, T extends V> void onRegisterBlock(Registry<V> registry, RegistryKey<V> key, T entry, CallbackInfoReturnable<T> cir) {
        if (!registry.equals(Registries.BLOCK)) {
            return;
        }
        if (!key.getValue().getNamespace().equals("blockus")) {
            return;
        }
        Block block = (Block) entry;
        boolean isOpaque = block.getDefaultState().isOpaque();
        if (!isOpaque) {

        }
    }
}
