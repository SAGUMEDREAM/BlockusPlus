package cc.thonly.blockuspatch.block;

import cc.thonly.blockuspatch.BlockusPolymerPatch;
import cc.thonly.blockuspatch.block.base.*;
import com.brand.blockus.Blockus;
import com.brand.blockus.blocks.base.*;
import eu.pb4.factorytools.api.block.model.SignModel;
import eu.pb4.factorytools.api.block.model.generic.BlockStateModelManager;
import eu.pb4.polymer.blocks.api.BlockModelType;
import eu.pb4.polymer.common.api.PolymerCommonUtils;
import eu.pb4.polymer.core.api.block.PolymerBlock;
import eu.pb4.polymer.virtualentity.api.BlockWithElementHolder;
import net.minecraft.block.*;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class PolymerBlockHelper {
    public static void registerPolymerBlock(String path, Block block) {
        PolymerBlock polymerBlock = requestPolymerBlock(block);
        PolymerBlock.registerOverlay(block, polymerBlock);
        if (polymerBlock instanceof BlockWithElementHolder blockWithElementHolder) {
            BlockWithElementHolder.registerOverlay(block, blockWithElementHolder);
        }

        if (block instanceof AbstractSignBlock) {
            BlockusPolymerPatch.LATE_INIT.add(() -> SignModel.setModel(block, Blockus.id("block_sign/" + path)));
        }
    }

    public static PolymerBlock requestPolymerBlock(Block block) {
        Identifier id = Registries.BLOCK.getId(block);
        BlockState defaultState = block.getDefaultState();

        BlockusPolymerPatch.LATE_INIT.add(() -> BlockStateModelManager.addBlock(id, block));
        return switch (block) {
            case RedstoneLampBlock ignored -> StatePolymerBlock.of(block, BlockModelType.FULL_BLOCK);
            case StairsBlock ignored -> StateCopyFactoryBlock.STAIR;
            case SlabBlock ignored -> SlabFactoryBlock.INSTANCE;
            case FenceGateBlock ignored -> StateCopyFactoryBlock.FENCE_GATE;
            case FenceBlock ignored -> StateCopyFactoryBlock.FENCE;
            case WallBlock ignored -> StateCopyFactoryBlock.WALL;
            case LeavesBlock ignored -> RealSingleStatePolymerBlock.of(block, BlockModelType.TRANSPARENT_BLOCK);
            case SignBlock ignored -> StateCopyFactoryBlock.SIGN;
            case WallSignBlock ignored -> StateCopyFactoryBlock.WALL_SIGN;
            case HangingSignBlock ignored -> StateCopyFactoryBlock.HANGING_SIGN;
            case WallHangingSignBlock ignored -> StateCopyFactoryBlock.HANGING_WALL_SIGN;
            case DoorBlock ignored -> DoorPolymerBlock.INSTANCE;
            case TrapdoorBlock ignored -> TrapdoorPolymerBlock.INSTANCE;
            case ButtonBlock ignored -> StateCopyFactoryBlock.BUTTON;
            case PressurePlateBlock ignored -> StateCopyFactoryBlock.PRESSURE_PLATE;
            case PlantBlock ignored -> BaseFactoryBlock.SAPLING;
            case FlowerPotBlock ignored -> new PottedPlantPolymerBlock(id);
            case PaneBlock ignored -> StateCopyFactoryBlock.PANE;
            case LanternBlock ignored -> StateCopyFactoryBlock.LANTERN;
            case Barrier ignored -> BaseFactoryBlock.BARRIER;
            case CookieBlock ignored -> CookiePolymerBlock.INSTANCE;
            case SmallHedgeBlock ignored -> BaseFactoryBlock.BARRIER;
            case HorizontalFacingBlock ignored -> BaseFactoryBlock.BARRIER;
            case HorizontalAxisBlock ignored -> BaseFactoryBlock.BARRIER;
            case PaperLampBlock ignored -> BaseFactoryBlock.BARRIER;
            case CarpetBlock ignored -> StateCopyFactoryBlock.CARPET;
            case PostBlock ignored -> new PostPolymerBlock(id);
            case ChainBlock ignored -> StateCopyFactoryBlock.CHAIN;
            case PillarBlock ignored -> BaseFactoryBlock.BARRIER;
            case GrateBlock ignored -> BaseFactoryBlock.BARRIER;
            default -> {
                if (defaultState.isFullCube(PolymerCommonUtils.getFakeWorld(), BlockPos.ORIGIN)) {
                    yield StatePolymerBlock.of(block, BlockModelType.FULL_BLOCK);
                } else {
                    yield BaseFactoryBlock.BARRIER;
                }
            }
        };
    }

}
