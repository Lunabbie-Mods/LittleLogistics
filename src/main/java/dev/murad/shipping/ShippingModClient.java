package dev.murad.shipping;

import dev.murad.shipping.entity.container.EnergyHeadVehicleScreen;
import dev.murad.shipping.entity.container.FishingBargeScreen;
import dev.murad.shipping.entity.container.SteamHeadVehicleScreen;
import dev.murad.shipping.entity.custom.train.locomotive.EnergyLocomotiveEntity;
import dev.murad.shipping.entity.custom.train.locomotive.SteamLocomotiveEntity;
import dev.murad.shipping.entity.custom.vessel.tug.EnergyTugEntity;
import dev.murad.shipping.entity.custom.vessel.tug.SteamTugEntity;
import dev.murad.shipping.item.container.TugRouteScreen;
import dev.murad.shipping.setup.ModItemModelProperties;
import dev.murad.shipping.setup.ModMenuTypes;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screens.MenuScreens;

public class ShippingModClient implements ClientModInitializer {
    public void onInitializeClient() {
        MenuScreens.register(ModMenuTypes.TUG_CONTAINER.get(), SteamHeadVehicleScreen<SteamTugEntity>::new);
        MenuScreens.register(ModMenuTypes.STEAM_LOCOMOTIVE_CONTAINER.get(), SteamHeadVehicleScreen<SteamLocomotiveEntity>::new);
        MenuScreens.register(ModMenuTypes.ENERGY_TUG_CONTAINER.get(),  EnergyHeadVehicleScreen<EnergyTugEntity>::new);
        MenuScreens.register(ModMenuTypes.ENERGY_LOCOMOTIVE_CONTAINER.get(), EnergyHeadVehicleScreen<EnergyLocomotiveEntity>::new);
        MenuScreens.register(ModMenuTypes.FISHING_BARGE_CONTAINER.get(), FishingBargeScreen::new);

        MenuScreens.register(ModMenuTypes.TUG_ROUTE_CONTAINER.get(), TugRouteScreen::new);

        event.enqueueWork(ModItemModelProperties::register);
    }

}
