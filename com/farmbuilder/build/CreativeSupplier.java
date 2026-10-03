package com.farmbuilder.build;

import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * Gives the player the block they need straight from the creative menu.
 * Does nothing in survival, so the normal inventory/order logic still applies there.
 *
 * Written with Yarn mappings for Minecraft 1.21.11.
 * If your project uses different mappings, adjust the class/method names.
 */
public final class CreativeSupplier {

    private CreativeSupplier() {}

    /**
     * @return true if the player is in creative and the block's item is now in
     *         the selected hotbar slot; false if the caller should use the
     *         normal survival logic.
     */
    public static boolean supply(MinecraftClient mc, BlockState target) {
        if (mc == null || mc.player == null || mc.interactionManager == null) return false;
        if (!mc.player.isCreative()) return false;

        Item item = target.getBlock().asItem();
        ItemStack stack = new ItemStack(item);
        if (stack.isEmpty()) return false; // blocks with no item (e.g. water, lava, tripwire)

        int slot = mc.player.getInventory().getSelectedSlot();

        // Already holding it? Nothing to do.
        ItemStack current = mc.player.getInventory().getStack(slot);
        if (!current.isEmpty() && current.isOf(item)) return true;

        stack.setCount(stack.getMaxCount());
        // Hotbar slots are 36..44 in the player screen handler
        mc.interactionManager.clickCreativeStack(stack, 36 + slot);
        return true;
    }
}
