package dev.murad.shipping;

import dev.murad.shipping.data.ShippingRegistrate;
import dev.murad.shipping.setup.Registration;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraftforge.api.ModLoadingContext;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
public class ShippingMod implements ModInitializer
{
    public static final String MOD_ID = "littlelogistics";
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

    public static final ShippingRegistrate REGISTRATE = ShippingRegistrate.create(MOD_ID);

    public static void gatherData(FabricDataGenerator fabricDataGenerator, ExistingFileHelper helper) {

    }

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
