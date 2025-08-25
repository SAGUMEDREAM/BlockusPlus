package cc.thonly.blockuspatch.mixin;

import cc.thonly.blockuspatch.entity.PolymerBoatEntity;
import cc.thonly.blockuspatch.item.ItemInit;
import com.terraformersmc.terraform.boat.impl.item.TerraformBoatItemHelperImpl;
import eu.pb4.polymer.core.api.entity.PolymerEntityUtils;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.vehicle.AbstractBoatEntity;
import net.minecraft.item.BoatItem;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

@Mixin(TerraformBoatItemHelperImpl.class)
public class TerraformBoatItemHelperImplMixin {
    @Inject(method = "registerBoat", at = @At("RETURN"))
    private static <T extends AbstractBoatEntity> void registerBoat(Identifier id, RegistryKey<Item> itemKey, RegistryKey<EntityType<?>> entityTypeKey, Item.Settings settings, Function<Supplier<Item>, EntityType.EntityFactory<T>> factory, BiConsumer<Identifier, EntityType<T>> registry, CallbackInfoReturnable<BoatItem> cir) {
        BoatItem item = cir.getReturnValue();
        ItemInit.registerOverlay(item);
        EntityType<?> entityType = item.boatEntityType;
        PolymerEntityUtils.registerOverlay(entityType, object -> new PolymerBoatEntity());
    }
}
