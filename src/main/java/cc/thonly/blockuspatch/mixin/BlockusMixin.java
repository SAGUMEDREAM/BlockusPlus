package cc.thonly.blockuspatch.mixin;

import cc.thonly.blockuspatch.block.BlockEntry;
import com.brand.blockus.Blockus;
import eu.pb4.polymer.rsm.api.RegistrySyncUtils;
import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(value = Blockus.class, remap = false)
public class BlockusMixin {
    @Inject(method = "onInitialize", at = @At("TAIL"))
    public void onInitialize(CallbackInfo ci) {
        for (Map.Entry<RegistryKey<Block>, Block> mapEntry : Registries.BLOCK.getEntrySet()) {
            RegistryKey<Block> key = mapEntry.getKey();
            Block block = mapEntry.getValue();
            if (!key.getValue().getNamespace().equals("blockus")) {
                continue;
            }
            if (RegistrySyncUtils.isServerEntry(Registries.BLOCK, block)) {
                continue;
            }
            BlockEntry.simple(block);
        }
//        for (Map.Entry<RegistryKey<Item>, Item> mapEntry : Registries.ITEM.getEntrySet()) {
//            RegistryKey<Item> key = mapEntry.getKey();
//            Item item = mapEntry.getValue();
//            if (!key.getValue().getNamespace().equals("blockus")) {
//                continue;
//            }
//            ItemInit.registerOverlay(item);
//        }
    }
}
