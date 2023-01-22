package dev.murad.shipping.data;

import dev.murad.shipping.data.client.ModModelProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public final class DataGenerators implements DataGeneratorEntrypoint {
    private DataGenerators () {}

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator gen) {
        //DataGenerator gen = gatherDataEvent.getGenerator();
        //ExistingFileHelper existingFileHelper = gen.getExistingFileHelper();

        gen.addProvider(ModModelProvider::new);

        //ModBlockTagsProvider modBlockTagsProvider = new ModBlockTagsProvider(gen);
        gen.addProvider(ModBlockTagsProvider::new);
        gen.addProvider(ModItemTagsProvider::new);
        gen.addProvider(ModLootTableProvider::new);
        gen.addProvider(ModRecipeProvider::new);

    }
}
