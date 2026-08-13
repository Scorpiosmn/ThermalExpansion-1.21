package cofh.thermal.expansion.init.data.providers;

import cofh.lib.init.data.LootTableProviderCoFH;
import cofh.thermal.expansion.init.data.tables.TExpBlockLootTables;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class TExpLootTableProvider extends LootTableProviderCoFH {

    public TExpLootTableProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {

        super(output, List.of(
                new SubProviderEntry(TExpBlockLootTables::new, LootContextParamSets.BLOCK)
        ), registries);
    }

}
