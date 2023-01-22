package dev.murad.shipping.entity.custom;

import dev.murad.shipping.util.EnrollmentHandler;
import io.github.fabricators_of_create.porting_lib.transfer.item.SlotExposedStorage;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.UUID;

public interface HeadVehicle  {

    void setEngineOn(boolean state);

    SlotExposedStorage getRouteItemHandler();

    boolean isValid(Player pPlayer);

    boolean hasOwner();

    ResourceLocation getRouteIcon();

    void enroll(UUID uuid);

    String owner();
}
