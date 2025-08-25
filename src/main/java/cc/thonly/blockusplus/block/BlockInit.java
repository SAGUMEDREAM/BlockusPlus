package cc.thonly.blockusplus.block;

import com.brand.blockus.registry.content.BlockusBlocks;
import net.minecraft.block.Block;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class BlockInit {
    public static final List<Block> MARK_NULL = new ArrayList<>();
    public static final List<Block> MARK_TRANSPARENT_fACING = new ArrayList<>();

    public static void bootstrap() {
        for (Field declaredField : BlockusBlocks.class.getDeclaredFields()) {
            Class<?> type = declaredField.getType();
            if (!Block.class.isAssignableFrom(type)) {
                continue;
            }
            try {
                Block block = (Block) declaredField.get(null);
                BlockEntry.simple(block);
            } catch (Exception ignored) {

            }
        }
    }
}
