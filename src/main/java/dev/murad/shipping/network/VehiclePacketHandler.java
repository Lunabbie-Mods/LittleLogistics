package dev.murad.shipping.network;

import dev.murad.shipping.ShippingMod;
import dev.murad.shipping.entity.custom.HeadVehicle;
import dev.murad.shipping.entity.custom.train.locomotive.AbstractLocomotiveEntity;
import dev.murad.shipping.global.PlayerTrainChunkManager;
import me.pepperbell.simplenetworking.SimpleChannel;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;
import java.util.function.Supplier;

public final class VehiclePacketHandler {
    private static final Logger LOGGER = LogManager.getLogger(VehiclePacketHandler.class);
    public static final ResourceLocation LOCATION = new ResourceLocation(ShippingMod.MOD_ID, "locomotive_channel");
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = new SimpleChannel(
            LOCATION
    );

    private static int id = 0;
    public static void register() {
        // int index, Class<MSG> messageType, BiConsumer<MSG, PacketBuffer> encoder, Function<PacketBuffer, MSG> decoder, BiConsumer<MSG, Supplier<NetworkEvent.Context>> messageConsumer
        INSTANCE.registerMessage(id++, SetEnginePacket.class, SetEnginePacket::encode, SetEnginePacket::new, VehiclePacketHandler::handleSetEngine);
        INSTANCE.registerMessage(id++, EnrollVehiclePacket.class, EnrollVehiclePacket::encode, EnrollVehiclePacket::new, VehiclePacketHandler::handleEnrollVehicle);
    }


    public static void handleSetEngine(SetEnginePacket operation, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Optional.of(ctx.get()).map(NetworkEvent.Context::getSender).ifPresent(serverPlayer -> {
                var loco = serverPlayer.level.getEntity(operation.locoId);
                if(loco != null && loco.distanceTo(serverPlayer) < 6 && loco instanceof HeadVehicle l){
                    l.setEngineOn(operation.state);
                }
            });

        });

        ctx.get().setPacketHandled(true);
    }

    public static void handleEnrollVehicle(EnrollVehiclePacket operation, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Optional.of(ctx.get()).map(NetworkEvent.Context::getSender).ifPresent(serverPlayer -> {
                var loco = serverPlayer.level.getEntity(operation.locoId);
                if(loco != null && loco.distanceTo(serverPlayer) < 6 && loco instanceof HeadVehicle l){
                    l.enroll(serverPlayer.getUUID());
                }
            });

        });

        ctx.get().setPacketHandled(true);
    }
}
