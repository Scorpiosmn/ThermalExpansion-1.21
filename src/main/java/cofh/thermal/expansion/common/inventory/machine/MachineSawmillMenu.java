package cofh.thermal.expansion.common.inventory.machine;

import cofh.core.common.inventory.BlockEntityCoFHMenu;
import cofh.lib.common.inventory.SlotCoFH;
import cofh.lib.common.inventory.SlotRemoveOnly;
import cofh.lib.common.inventory.wrapper.InvWrapperCoFH;
import cofh.thermal.lib.common.block.entity.Reconfigurable4WayBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.level.Level;

import static cofh.thermal.expansion.init.registries.TExpContainers.MACHINE_SAWMILL_CONTAINER;

public class MachineSawmillMenu extends BlockEntityCoFHMenu {

    public final Reconfigurable4WayBlockEntity tile;

    public MachineSawmillMenu(int windowId, Level level, BlockPos pos, Inventory inventory, Player player) {

        super(MACHINE_SAWMILL_CONTAINER.get(), windowId, level, pos, inventory, player);
        this.tile = (Reconfigurable4WayBlockEntity) level.getBlockEntity(pos);
        InvWrapperCoFH tileInv = new InvWrapperCoFH(this.tile.getItemInv());

        // Explicit slot limit: ItemStorageCoFH#getSlotLimit falls back to ItemStack.EMPTY.getMaxStackSize(),
        // which is 1 on 1.21.1 (it was 64 on 1.20.4), so empty slots would only accept a single item per click.
        addSlot(new SlotCoFH(tileInv, 0, 44, 26, () -> 64));

        addSlot(new SlotRemoveOnly(tileInv, 1, 107, 26));
        addSlot(new SlotRemoveOnly(tileInv, 2, 125, 26));
        addSlot(new SlotRemoveOnly(tileInv, 3, 107, 44));
        addSlot(new SlotRemoveOnly(tileInv, 4, 125, 44));

        addSlot(new SlotCoFH(tileInv, 5, 8, 53, () -> 64));

        bindAugmentSlots(tileInv, 6, this.tile.augSize());
        bindPlayerInventory(inventory);
    }

    @Override
    public void clicked(int index, int dragType, ClickType clickTypeIn, Player player) {

        super.clicked(index, dragType, clickTypeIn, player);
        // 1.21.1 syncs container slots per-slot after a click instead of re-sending the full contents as 1.20.4 did.
        // Force an authoritative full re-sync so the client's menu slots cannot drift from the server state.
        if (player instanceof ServerPlayer) {
            broadcastFullState();
        }
    }

}
