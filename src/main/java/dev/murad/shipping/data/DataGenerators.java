package dev.murad.shipping.data;

import dev.murad.shipping.data.client.ModBlockStateProvider;
import dev.murad.shipping.data.client.ModItemModelProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;

public final class DataGenerators implements DataGeneratorEntrypoint {
    private DataGenerators () {}

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator gen) {
        ExistingFileHelper existingFileHelper = ExistingFileHelper.standard();

        gen.addProvider(new ModBlockStateProvider(gen, existingFileHelper));
        gen.addProvider(new ModItemModelProvider(gen, existingFileHelper));

        gen.addProvider(ModBlockTagsProvider::new);
        gen.addProvider(ModItemTagsProvider::new);
        gen.addProvider(ModBlockLootTableProvider::new);
        gen.addProvider(ModRecipeProvider::new);

    }
}
