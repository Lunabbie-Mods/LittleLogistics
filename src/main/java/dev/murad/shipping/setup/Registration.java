package dev.murad.shipping.setup;

import dev.murad.shipping.ShippingMod;
import dev.murad.shipping.network.VehiclePacketHandler;
import dev.murad.shipping.network.TugRoutePacketHandler;
import dev.murad.shipping.network.client.VehicleTrackerPacketHandler;
import net.minecraft.core.Registry;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.sounds.SoundEvent;
import io.github.fabricators_of_create.porting_lib.util.LazyRegistrar;

public class Registration  {
    public static final LazyRegistrar<Block> BLOCKS = create(Registry.BLOCK);
    public static final LazyRegistrar<MenuType<?>> CONTAINERS = create(Registry.MENU);
    public static final LazyRegistrar<EntityType<?>> ENTITIES = create(Registry.ENTITY_TYPE);
    public static final LazyRegistrar<Item> ITEMS = create(Registry.ITEM);
    public static final LazyRegistrar<RecipeSerializer<?>> RECIPE_SERIALIZERS = create(Registry.RECIPE_SERIALIZER);
    public static final LazyRegistrar<BlockEntityType<?>> TILE_ENTITIES = create(Registry.BLOCK_ENTITY_TYPE);
    public static final LazyRegistrar<SoundEvent> SOUND_EVENTS = create(Registry.SOUND_EVENT);


    private static<T> LazyRegistrar<T> create(Registry<T> registry) {
        return LazyRegistrar.create(registry, ShippingMod.MOD_ID);
    }

    public static void register () {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        BLOCKS.register(eventBus);
        ITEMS.register(eventBus);
        CONTAINERS.register(eventBus);
        RECIPE_SERIALIZERS.register(eventBus);
        TILE_ENTITIES.register(eventBus);
        ENTITIES.register(eventBus);
        SOUND_EVENTS.register(eventBus);

        ModEntityTypes.register();
        ModItems.register();
        ModBlocks.register();
        ModTileEntitiesTypes.register();
        ModRecipeSerializers.register();
        ModMenuTypes.register();
        TugRoutePacketHandler.register();
        VehicleTrackerPacketHandler.register();
        VehiclePacketHandler.register();
        ModSounds.register();
    }
}
