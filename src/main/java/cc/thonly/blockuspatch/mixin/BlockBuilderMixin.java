package cc.thonly.blockuspatch.mixin;

import cc.thonly.blockuspatch.block.PolymerBlockHelper;
import cc.thonly.blockuspatch.item.PolyBaseItem;
import com.brand.blockus.utils.helper.BlockBuilder;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import eu.pb4.polymer.core.api.item.PolymerItem;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Function;

@Mixin(BlockBuilder.class)
public abstract class BlockBuilderMixin {
    @Inject(
        method = "register(Ljava/lang/String;Ljava/util/function/Function;)Lnet/minecraft/block/Block;",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/registry/Registry;register(Lnet/minecraft/registry/Registry;Lnet/minecraft/registry/RegistryKey;Ljava/lang/Object;)Ljava/lang/Object;",
            ordinal = 0,
            shift = At.Shift.AFTER
        )
    )
    public void polymerifyBlock(String path, Function<Block, Item> itemFactory, CallbackInfoReturnable<Block> cir, @Local Block block) {
        PolymerBlockHelper.registerPolymerBlock(path, block);
    }

    @WrapOperation(
        method = "register(Ljava/lang/String;Ljava/util/function/Function;)Lnet/minecraft/block/Block;",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/block/AbstractBlock$Settings;registryKey(Lnet/minecraft/registry/RegistryKey;)Lnet/minecraft/block/AbstractBlock$Settings;"
        )
    )
    public AbstractBlock.Settings disableOcclusion(AbstractBlock.Settings instance, RegistryKey<Block> registryKey, Operation<AbstractBlock.Settings> original) {
        return original.call(instance, registryKey).nonOpaque();
    }

    @WrapOperation(
        method = "register(Ljava/lang/String;Ljava/util/function/Function;)Lnet/minecraft/block/Block;",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/registry/Registry;register(Lnet/minecraft/registry/Registry;Lnet/minecraft/registry/RegistryKey;Ljava/lang/Object;)Ljava/lang/Object;",
            ordinal = 1
        )
    )
    private static <V, T extends V> T polymerifyItem(Registry<V> registry, RegistryKey<V> key, T entry, Operation<T> original) {
        Item item = (Item) entry;
        PolymerItem.registerOverlay(item, new PolyBaseItem(item));
        return original.call(registry, key, entry);
    }

}
