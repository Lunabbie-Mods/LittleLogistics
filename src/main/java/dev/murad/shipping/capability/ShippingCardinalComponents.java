package dev.murad.shipping.capability;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistryV3;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.item.ItemComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.item.ItemComponentInitializer;
import dev.onyxstudios.cca.api.v3.world.WorldComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.world.WorldComponentInitializer;
import net.minecraft.resources.ResourceLocation;

import static dev.murad.shipping.ShippingMod.MOD_ID;

public class ShippingCardinalComponents implements EntityComponentInitializer, ItemComponentInitializer, WorldComponentInitializer {
    public static final ComponentKey<StallingCapability> STALLING_CAPABILITY_COMPONENT_KEY =
            ComponentRegistryV3.INSTANCE.getOrCreate(new ResourceLocation(MOD_ID, "stalling"), StallingCapability.class);
    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {

    }

    @Override
    public void registerItemComponentFactories(ItemComponentFactoryRegistry registry) {

    }

    @Override
    public void registerWorldComponentFactories(WorldComponentFactoryRegistry registry) {

    }
}
