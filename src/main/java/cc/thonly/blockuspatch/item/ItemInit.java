package cc.thonly.blockuspatch.item;

import eu.pb4.polymer.core.api.item.PolymerItem;
import eu.pb4.polymer.core.api.item.PolymerItemUtils;
import eu.pb4.polymer.rsm.api.RegistrySyncUtils;
import net.minecraft.block.BlockState;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import xyz.nucleoid.packettweaker.PacketContext;

public class ItemInit {
    public static void bootstrap() {

    }

    public static void registerOverlay(Item item) {
        if (RegistrySyncUtils.isServerEntry(Registries.ITEM, item)) {
            return;
        }
        PolymerItemUtils.registerOverlay(item, new PolymerItem() {
            @Override
            public Item getPolymerItem(ItemStack itemStack, PacketContext packetContext) {
                return item instanceof ShieldItem ? Items.SHIELD : Items.TRIAL_KEY;
            }

            @Override
            public boolean isPolymerBlockInteraction(BlockState state, ServerPlayerEntity player, Hand hand, ItemStack stack, ServerWorld world, BlockHitResult blockHitResult, ActionResult actionResult) {
                return actionResult.isAccepted();
            }

            @Override
            public boolean isIgnoringBlockInteractionPlaySoundExceptedEntity(BlockState state, ServerPlayerEntity player, Hand hand, ItemStack stack, ServerWorld world, BlockHitResult blockHitResult) {
                return item instanceof BlockItem;
            }

            @Override
            public void modifyBasePolymerItemStack(ItemStack out, ItemStack stack, PacketContext context) {
                out.set(DataComponentTypes.TOOLTIP_DISPLAY, out.getOrDefault(DataComponentTypes.TOOLTIP_DISPLAY, TooltipDisplayComponent.DEFAULT));
            }
        });
    }
}
