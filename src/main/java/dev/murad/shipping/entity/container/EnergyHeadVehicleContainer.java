package dev.murad.shipping.entity.container;

import dev.murad.shipping.entity.accessor.EnergyHeadVehicleDataAccessor;
import dev.murad.shipping.entity.custom.HeadVehicle;
import dev.murad.shipping.setup.ModItems;
import dev.murad.shipping.setup.ModMenuTypes;
import dev.murad.shipping.util.EnrollmentHandler;
import io.github.fabricators_of_create.porting_lib.transfer.item.SlotItemHandler;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class EnergyHeadVehicleContainer<T extends Entity & HeadVehicle> extends AbstractHeadVehicleContainer<EnergyHeadVehicleDataAccessor, T> {
    public EnergyHeadVehicleContainer(int windowId, Level world, EnergyHeadVehicleDataAccessor data,
                                      Inventory playerInventory, Player player) {
        super(ModMenuTypes.ENERGY_LOCOMOTIVE_CONTAINER.get(), windowId, world, data, playerInventory, player);

        if(entity != null) {
            entity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
                addSlot(new SlotItemHandler(h, 0, 32, 35)
                        .setBackground(EMPTY_ATLAS_LOC, ModItems.EMPTY_ENERGY));
            });
        }
    }

    public long getEnergy() {
        return this.data.getEnergy();
    }

    public long getCapacity() {
        return this.data.getCapacity();
    }

    public double getEnergyCapacityRatio() {
        if (getCapacity() == 0) {
            return 1.0;
        }

        return (double) getEnergy() / getCapacity();
    }
}
