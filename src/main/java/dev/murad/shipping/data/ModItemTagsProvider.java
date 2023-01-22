package dev.murad.shipping.data;

import dev.murad.shipping.ShippingMod;
import dev.murad.shipping.setup.ModItems;
import dev.murad.shipping.setup.ModTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.Registry;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;

public class ModItemTagsProvider extends FabricTagProvider<Item> {

    public ModItemTagsProvider(FabricDataGenerator dataGenerator) {
        super(dataGenerator, Registry.ITEM);
    }

    protected void generateTags() {
        tag(ModTags.Items.WRENCHES).add(ModItems.CONDUCTORS_WRENCH.get());
    }
}
