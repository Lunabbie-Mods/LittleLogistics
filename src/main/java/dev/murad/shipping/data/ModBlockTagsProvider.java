package dev.murad.shipping.data;

import dev.murad.shipping.ShippingMod;
import dev.murad.shipping.setup.ModBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.Registry;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModBlockTagsProvider extends FabricTagProvider<Block> {
    public ModBlockTagsProvider(FabricDataGenerator gen) {
        super(gen, Registry.BLOCK);
    }

    @Override
    protected void generateTags() {
        tag(BlockTags.RAILS).add(ModBlocks.SWITCH_RAIL.get());
        tag(BlockTags.RAILS).add(ModBlocks.AUTOMATIC_SWITCH_RAIL.get());
        tag(BlockTags.RAILS).add(ModBlocks.TEE_JUNCTION_RAIL.get());
        tag(BlockTags.RAILS).add(ModBlocks.AUTOMATIC_TEE_JUNCTION_RAIL.get());
        tag(BlockTags.RAILS).add(ModBlocks.JUNCTION_RAIL.get());
        tag(BlockTags.RAILS).add(ModBlocks.LOCOMOTIVE_DOCK_RAIL.get());
        tag(BlockTags.RAILS).add(ModBlocks.CAR_DOCK_RAIL.get());
    }
}
