package cc.thonly.blockuspatch.effect;

import com.brand.blockus.registry.effect.BlockusEffects;
import eu.pb4.polymer.core.api.other.PolymerStatusEffect;

public class BlockusEffectInit {
    public static void bootstrap() {
        PolymerStatusEffect.registerOverlay(BlockusEffects.ASPHALT_SPRINT.value());
    }
}
