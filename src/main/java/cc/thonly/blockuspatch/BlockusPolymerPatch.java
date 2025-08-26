package cc.thonly.blockuspatch;

import cc.thonly.blockuspatch.effect.BlockusEffectInit;
import cc.thonly.blockuspatch.res.ResourcePackGenerator;
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import eu.pb4.polymer.resourcepack.extras.api.ResourcePackExtras;
import net.fabricmc.api.ModInitializer;

import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class BlockusPolymerPatch implements ModInitializer {
	public static final String MAIN_MOD_ID = "blockus-polymer-patch";
	public static final String MOD_ID = "blockus";
	public static final Logger LOGGER = LoggerFactory.getLogger(MAIN_MOD_ID);
	public static final List<Runnable> LATE_INIT = new ArrayList<>();

	@Override
	public void onInitialize() {
		LOGGER.info("Patching Blockus");
		BlockusEffectInit.bootstrap();
		PolymerResourcePackUtils.addModAssets("blockus-polymer-patch");

		PolymerResourcePackUtils.addModAssets(MOD_ID);
		ResourcePackExtras.forDefault().addBridgedModelsFolder(
				id("block"),
				id("block_sign"),
				id("item"),
				id("entity"),
				id("gui"),
				id("mob_effect")
		);
		ResourcePackGenerator.setup();
	}

	public static Identifier id(String name) {
		return Identifier.of(MOD_ID, name);
	}
}