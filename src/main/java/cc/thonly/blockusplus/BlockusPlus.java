package cc.thonly.blockusplus;

import cc.thonly.blockusplus.block.BlockEntry;
import cc.thonly.blockusplus.block.CommonBlockStates;
import cc.thonly.blockusplus.block.BlockInit;
import cc.thonly.blockusplus.effect.BlockusEffectInit;
import cc.thonly.blockusplus.item.BlockusCreativeTabs;
import cc.thonly.blockusplus.item.ItemInit;
import com.brand.blockus.registry.content.BlockusBlocks;
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import eu.pb4.polymer.resourcepack.extras.api.ResourcePackExtras;
import net.fabricmc.api.ModInitializer;

import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BlockusPlus implements ModInitializer {
	public static final String MAIN_MOD_ID = "blockus-plus";
	public static final String MOD_ID = "blockus";
	public static final Logger LOGGER = LoggerFactory.getLogger(MAIN_MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Patching Blockus");
		CommonBlockStates.load();
		BlockInit.bootstrap();
		ItemInit.bootstrap();
		BlockusEffectInit.bootstrap();

//		System.out.println(BlockEntry.getBlockMap().get(BlockusBlocks.ACACIA_HEDGE));

		PolymerResourcePackUtils.addModAssets(MAIN_MOD_ID);
		PolymerResourcePackUtils.addModAssets(MOD_ID);
		ResourcePackExtras.forDefault().addBridgedModelsFolder(
				id("block"),
				id("item"),
				id("entity"),
				id("gui"),
				id("mob_effect")
		);
	}

	public static Identifier id(String name) {
		return Identifier.of(MOD_ID, name);
	}
}