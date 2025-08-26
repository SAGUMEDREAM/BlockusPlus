package cc.thonly.blockuspatch.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.vehicle.AbstractBoatEntity;
import net.minecraft.item.BoatItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BoatItem.class)
public interface BoatItemAccessor {
    @Accessor
    EntityType<? extends AbstractBoatEntity> getBoatEntityType();
}
