package cc.thonly.blockusplus.block;

import cc.thonly.blockusplus.block.base.*;
import cc.thonly.blockusplus.block.base.mod.BarrierImpl;
import cc.thonly.blockusplus.block.base.mod.PostImpl;
import cc.thonly.blockusplus.block.base.TransparentPillarImpl;
import cc.thonly.blockusplus.block.base.mod.SmallHedgeImpl;
import com.brand.blockus.blocks.base.Barrier;
import com.brand.blockus.blocks.base.PaperLampBlock;
import com.brand.blockus.blocks.base.PostBlock;
import com.brand.blockus.blocks.base.SmallHedgeBlock;
import eu.pb4.polymer.core.api.block.PolymerBlock;
import eu.pb4.polymer.rsm.api.RegistrySyncUtils;
import eu.pb4.polymer.virtualentity.api.BlockWithElementHolder;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.minecraft.block.*;
import net.minecraft.registry.Registries;
import net.minecraft.state.StateManager;
import net.minecraft.util.Identifier;
import xyz.nucleoid.packettweaker.PacketContext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Slf4j
@Getter
public class BlockEntry {
    private static final List<BlockEntry> BLOCK_ENTRIES = new ArrayList<>();
    private static final Map<Block, PolymerBlock> blockMap = new Object2ObjectOpenHashMap<>();
    private final Map<Block, PolymerBlock> entryBlockMap = new Object2ObjectOpenHashMap<>();
    private final Identifier id;
    private final Block base;

    public BlockEntry(Block block) {
        this.id = Registries.BLOCK.getId(block);
        this.base = block;
    }

    public PolymerBlock requestPolymerBlock(Block base, Block block) {
        Identifier id = Registries.BLOCK.getId(block);
        PolymerBlock polymerBlock = null;
        if (BlockInit.MARK_NULL.contains(block)) {
            return new NullBlockImpl(id);
        }
        if (BlockInit.MARK_TRANSPARENT_fACING.contains(block)) {

        }
        if (block instanceof RedstoneLampBlock redstoneLampBlock) {
            polymerBlock = new RedstoneLampImpl(id);
        }
        if (block instanceof StairsBlock stairsBlock) {
            polymerBlock = new StairImpl(id, base, stairsBlock);
        }
        if (block instanceof SlabBlock slabBlock) {
            polymerBlock = new SlabImpl(id, base, slabBlock);
        }
        if (block instanceof FenceGateBlock fenceGateBlock) {
            polymerBlock = new FenceGateImpl(id);
        }
        if (block instanceof FenceBlock fenceBlock) {
            polymerBlock = new FenceImpl(id);
        }
        if (block instanceof WallBlock wallBlock) {
            polymerBlock = new WallImpl(id);
        }
        if (block instanceof LeavesBlock leavesBlock) {
            polymerBlock = new TransparentCubeAllBlockImpl(id);
        }
        if (block instanceof SignBlock signBlock) {
            polymerBlock = StateCopyFactoryBlock.SIGN;
        }
        if (block instanceof WallSignBlock wallSignBlock) {
            polymerBlock = StateCopyFactoryBlock.WALL_SIGN;
        }
        if (block instanceof HangingSignBlock hangingSignBlock) {
            polymerBlock = StateCopyFactoryBlock.HANGING_SIGN;
        }
        if (block instanceof WallHangingSignBlock wallHangingSignBlock) {
            polymerBlock = StateCopyFactoryBlock.HANGING_WALL_SIGN;
        }
        if (block instanceof DoorBlock doorBlock) {
            polymerBlock = new DoorImpl(id);
        }
        if (block instanceof TrapdoorBlock trapdoorBlock) {
            polymerBlock = new TrapdoorImpl(id);
        }
        if (block instanceof ButtonBlock buttonBlock) {
            polymerBlock = new ButtonImpl(id);
        }
        if (block instanceof PressurePlateBlock pressurePlateBlock) {
            polymerBlock = new PressurePlateImpl(id);
        }
        if (block instanceof PlantBlock plantBlock) {
            polymerBlock = new PlantImpl(id);
        }
        if (block instanceof FlowerPotBlock flowerPotBlock) {
            polymerBlock = new PottedPlantImpl(id);
        }
        if (block instanceof PaneBlock paneBlock) {
            polymerBlock = new PaneImpl(id);
        }
        if (block instanceof LanternBlock lanternBlock) {
            polymerBlock = new LanternImpl(id);
        }
        if (block instanceof Barrier barrier) {
            polymerBlock = new BarrierImpl(id, barrier);
        }
        if (block instanceof SmallHedgeBlock) {
            polymerBlock = new SmallHedgeImpl(id);
        }
        if (block instanceof HorizontalFacingBlock horizontalFacingBlock) {
            return new TransparentCubeFacingBlockImpl(id);
        }
        if (block instanceof PaperLampBlock paperLampBlock) {
            return new TransparentCubeAllBlockImpl(id);
        }
        if (block instanceof PillarBlock pillarBlock) {
            boolean sp = false;
            if (block instanceof PostBlock postBlock) {
                polymerBlock = new PostImpl(id);
                sp = true;
            }
            if (!sp && block instanceof ChainBlock chainBlock) {
                polymerBlock = new ChainImpl(id);
                sp = true;
            }
            if (!sp) {
                block.getSettings().nonOpaque();
//                StateManager.Builder<Block, BlockState> builder = new StateManager.Builder<>(block);
//                block.appendProperties(builder);
//                block.stateManager = builder.build(Block::getDefaultState, BlockState::new);
//                block.setDefaultState(block.stateManager.getDefaultState());
                polymerBlock = new TransparentPillarImpl(id);
                sp = true;
            }
        }
        if (block instanceof GrateBlock) {
            polymerBlock = new TransparentCubeAllBlockImpl(id);
        }
        if (polymerBlock == null) {
            if (block.getDefaultState().isOpaque()) {
                polymerBlock = new CubeAllBlockImpl(id);
            } else {
                polymerBlock = new TransparentCubeAllBlockImpl(id);
            }
        }
        return polymerBlock;
    }

    public PolymerBlock getPolymerBlock(Block block) {
        return blockMap.get(block);
    }

    public BlockEntry registerOverlay(Block block) {
        if (RegistrySyncUtils.isServerEntry(Registries.BLOCK, block)) {
            return this;
        }
        PolymerBlock polymerBlock = this.requestPolymerBlock(this.base, block);
        if (polymerBlock.getPolymerBlockState(block.getDefaultState(), PacketContext.create()) == null) {
            log.error("{} can't find a overlay", this.id);
            return this;
        }
        PolymerBlock.registerOverlay(block, polymerBlock);
        if (polymerBlock instanceof BlockWithElementHolder blockWithElementHolder) {
            BlockWithElementHolder.registerOverlay(block, blockWithElementHolder);
        }
        this.entryBlockMap.put(block, polymerBlock);
        blockMap.put(block, polymerBlock);
        return this;
    }

    public BlockEntry registerOverlay(Block block, PolymerBlock polymerBlock) {
        PolymerBlock.registerOverlay(block, polymerBlock);
        if (polymerBlock instanceof BlockWithElementHolder blockWithElementHolder) {
            BlockWithElementHolder.registerOverlay(block, blockWithElementHolder);
        }
        this.entryBlockMap.put(block, polymerBlock);
        blockMap.put(block, polymerBlock);
        return this;
    }

    public static BlockEntry of(Block block) {
        var entry = new BlockEntry(block);
        BLOCK_ENTRIES.add(entry);
        return entry;
    }

    public static BlockEntry simple(Block block) {
        BlockEntry blockEntry = of(block);
        blockEntry.registerOverlay(block);
        return blockEntry;
    }

    public static BlockEntry simple(Block block, PolymerBlock polymerBlock) {
        BlockEntry blockEntry = of(block);
        blockEntry.registerOverlay(block, polymerBlock);
        return blockEntry;
    }

    public static Collection<BlockEntry> getList() {
        return BLOCK_ENTRIES;
    }

    public static Map<Block, PolymerBlock> getBlockMap() {
        return Map.copyOf(blockMap);
    }

}
