package dev.murad.shipping;

import dev.murad.shipping.entity.container.*;
import dev.murad.shipping.entity.custom.train.locomotive.EnergyLocomotiveEntity;
import dev.murad.shipping.entity.custom.train.locomotive.SteamLocomotiveEntity;
import dev.murad.shipping.entity.custom.vessel.tug.EnergyTugEntity;
import dev.murad.shipping.entity.custom.vessel.tug.SteamTugEntity;
import dev.murad.shipping.item.container.TugRouteScreen;
import dev.murad.shipping.setup.ModItemModelProperties;
import dev.murad.shipping.setup.ModMenuTypes;
import dev.murad.shipping.setup.Registration;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.ForgeConfigAPIPort;
import net.minecraftforge.api.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.loading.FMLConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
public class ShippingMod implements ModInitializer
{
    public static final String MOD_ID = "littlelogistics";
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

    public void onInitialize() {
        Registration.register();

        // Register the doClientStuff method for modloading
        //FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        ModLoadingContext.registerConfig(MOD_ID, ModConfig.Type.COMMON, ShippingConfig.Common.SPEC, "littlelogistics-common.toml");
        ModLoadingContext.registerConfig(MOD_ID, ModConfig.Type.CLIENT, ShippingConfig.Client.SPEC, "littlelogistics-client.toml");
        ModLoadingContext.registerConfig(MOD_ID, ModConfig.Type.SERVER, ShippingConfig.Server.SPEC, "littlelogistics-server.toml");

        // Register ourselves for server and other game events we are interested in
        //MinecraftForge.EVENT_BUS.register(this);
    }

}
