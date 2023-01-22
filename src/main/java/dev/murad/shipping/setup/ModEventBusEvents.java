package dev.murad.shipping.setup;

import dev.murad.shipping.ShippingMod;
import dev.murad.shipping.entity.custom.vessel.VesselEntity;
import dev.murad.shipping.entity.custom.vessel.tug.EnergyTugEntity;
import dev.murad.shipping.entity.custom.vessel.tug.SteamTugEntity;

@Mod.EventBusSubscriber(modid = ShippingMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {
    @SubscribeEvent
    public static void addEntityAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntityTypes.STEAM_TUG.get(), SteamTugEntity.setCustomAttributes().build());
        event.put(ModEntityTypes.ENERGY_TUG.get(), EnergyTugEntity.setCustomAttributes().build());
        event.put(ModEntityTypes.FISHING_BARGE.get(), VesselEntity.setCustomAttributes().build());
        event.put(ModEntityTypes.CHUNK_LOADER_BARGE.get(), VesselEntity.setCustomAttributes().build());
        event.put(ModEntityTypes.FLUID_TANK_BARGE.get(), VesselEntity.setCustomAttributes().build());
        event.put(ModEntityTypes.CHEST_BARGE.get(), VesselEntity.setCustomAttributes().build());
        event.put(ModEntityTypes.SEATER_BARGE.get(), VesselEntity.setCustomAttributes().build());
    }

}