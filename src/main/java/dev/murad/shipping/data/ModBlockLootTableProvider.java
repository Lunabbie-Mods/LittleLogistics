package dev.murad.shipping.data;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import dev.murad.shipping.setup.ModBlocks;
import dev.murad.shipping.setup.Registration;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableProvider;
import net.minecraft.core.Registry;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTables;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import io.github.fabricators_of_create.porting_lib.util.RegistryObject;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class ModBlockLootTableProvider extends SimpleFabricLootTableProvider {

    public ModBlockLootTableProvider(FabricDataGenerator dataGenerator) {
        super(dataGenerator, LootContextParamSets.BLOCK);
    }

//    @Override
//    protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> getTables() {
//        return ImmutableList.of(
//                Pair.of(ModBlockLootTables::new, LootContextParamSets.BLOCK)
//        );
//    }
//
//    @Override
//    protected void validate(Map<ResourceLocation, LootTable> map, ValidationContext validationtracker) {
//        map.forEach((p_218436_2_, p_218436_3_) -> LootTables.validate(validationtracker, p_218436_2_, p_218436_3_));
//    }

    private void dropSelf(BiConsumer<ResourceLocation, LootTable.Builder> biConsumer, Block block) {
        biConsumer.accept(Registry.BLOCK.getKey(block), BlockLoot.createSingleItemTable(block));
    }

    @Override
    public void accept(BiConsumer<ResourceLocation, LootTable.Builder> biConsumer) {
        dropSelf(biConsumer, ModBlocks.TUG_DOCK.get());
        dropSelf(biConsumer, ModBlocks.BARGE_DOCK.get());
        dropSelf(biConsumer, ModBlocks.GUIDE_RAIL_CORNER.get());
        dropSelf(biConsumer, ModBlocks.GUIDE_RAIL_TUG.get());
        dropSelf(biConsumer, ModBlocks.FLUID_HOPPER.get());
        dropSelf(biConsumer, ModBlocks.VESSEL_CHARGER.get());
        dropSelf(biConsumer, ModBlocks.VESSEL_DETECTOR.get());
        dropSelf(biConsumer, ModBlocks.SWITCH_RAIL.get());
        dropSelf(biConsumer, ModBlocks.AUTOMATIC_SWITCH_RAIL.get());
        dropSelf(biConsumer, ModBlocks.TEE_JUNCTION_RAIL.get());
        dropSelf(biConsumer, ModBlocks.AUTOMATIC_TEE_JUNCTION_RAIL.get());
        dropSelf(biConsumer, ModBlocks.JUNCTION_RAIL.get());
        dropSelf(biConsumer, ModBlocks.RAPID_HOPPER.get());
        dropSelf(biConsumer, ModBlocks.CAR_DOCK_RAIL.get());
        dropSelf(biConsumer, ModBlocks.LOCOMOTIVE_DOCK_RAIL.get());
    }

//    public static class ModBlockLootTables extends BlockLoot {
//        @Override
//        protected void addTables() {
//        }
//
//        @Override
//        protected Iterable<Block> getKnownBlocks() {
//            return Registration.BLOCKS.getEntries().stream()
//                    .map(RegistryObject::get)
//                    .collect(Collectors.toList());
//        }
//    }
}
