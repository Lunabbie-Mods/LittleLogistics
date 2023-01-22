package dev.murad.shipping.setup;

import dev.murad.shipping.component.EnergyComponent;
import dev.murad.shipping.component.FluidHandlerComponent;
import dev.murad.shipping.component.ItemHandlerComponent;
import dev.murad.shipping.component.StallingComponent;
import dev.murad.shipping.entity.custom.vessel.barge.AbstractBargeEntity;
import dev.murad.shipping.entity.custom.vessel.tug.AbstractTugEntity;
import dev.onyxstudios.cca.api.v3.block.BlockComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.block.BlockComponentInitializer;
import dev.onyxstudios.cca.api.v3.component.Component;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistryV3;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.item.ItemComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.item.ItemComponentInitializer;
import dev.onyxstudios.cca.api.v3.scoreboard.ScoreboardComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.scoreboard.ScoreboardComponentInitializer;
import dev.onyxstudios.cca.api.v3.world.WorldComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.world.WorldComponentInitializer;
import net.minecraft.resources.ResourceLocation;

import static dev.murad.shipping.ShippingMod.MOD_ID;

public class ModComponents implements EntityComponentInitializer, ItemComponentInitializer, WorldComponentInitializer, BlockComponentInitializer, ScoreboardComponentInitializer {
    public static final ComponentKey<StallingComponent> STALLING =
            ComponentRegistryV3.INSTANCE.getOrCreate(new ResourceLocation(MOD_ID, "stalling"), StallingComponent.class);

    public static final ComponentKey<ItemHandlerComponent> ITEM_HANDLER =
            ComponentRegistryV3.INSTANCE.getOrCreate(new ResourceLocation(MOD_ID, "item_handler"), ItemHandlerComponent.class);
    public static final ComponentKey<FluidHandlerComponent> FLUID_HANDLER =
            ComponentRegistryV3.INSTANCE.getOrCreate(new ResourceLocation(MOD_ID, "fluid_handler"), FluidHandlerComponent.class);

    public static final ComponentKey<EnergyComponent> ENERGY =
            ComponentRegistryV3.INSTANCE.getOrCreate(new ResourceLocation(MOD_ID, "energy"), EnergyComponent.class);
    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerFor(AbstractTugEntity.class, STALLING, AbstractTugEntity::createStallingComponent);
        registry.registerFor(AbstractBargeEntity.class, STALLING, AbstractBargeEntity::createStallingComponent);
    }

    @Override
    public void registerItemComponentFactories(ItemComponentFactoryRegistry registry) {

    }

    @Override
    public void registerWorldComponentFactories(WorldComponentFactoryRegistry registry) {

    }

    @Override
    public void registerBlockComponentFactories(BlockComponentFactoryRegistry registry) {

    }

    @Override
    public void registerScoreboardComponentFactories(ScoreboardComponentFactoryRegistry registry) {

    }
}
