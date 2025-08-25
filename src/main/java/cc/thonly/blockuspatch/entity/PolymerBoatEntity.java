package cc.thonly.blockuspatch.entity;

import eu.pb4.polymer.core.api.entity.PolymerEntity;
import net.minecraft.entity.EntityType;
import xyz.nucleoid.packettweaker.PacketContext;

public class PolymerBoatEntity implements PolymerEntity {

    @Override
    public EntityType<?> getPolymerEntityType(PacketContext packetContext) {
        return EntityType.OAK_BOAT;
    }
}
