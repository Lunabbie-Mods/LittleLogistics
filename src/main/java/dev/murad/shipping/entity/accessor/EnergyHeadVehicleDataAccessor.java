package dev.murad.shipping.entity.accessor;

import dev.murad.shipping.util.EnrollmentHandler;
import net.minecraft.world.inventory.ContainerData;

import java.util.function.BooleanSupplier;
import java.util.function.IntSupplier;
import java.util.function.LongSupplier;
import java.util.function.Supplier;

public class EnergyHeadVehicleDataAccessor extends HeadVehicleDataAccessor {
    private static final long SHORT_MASK = 0xFFFF;
    
    public EnergyHeadVehicleDataAccessor(ContainerData data) {
        super(data);
    }

    /**
     * Lil-endian
     */

    public long getEnergy() {
        long lo = this.data.get(15) & SHORT_MASK;
        long hi = this.data.get(16) & SHORT_MASK;
        return lo | hi << 16;
    }

    public long getCapacity() {
        long lo = this.data.get(17) & SHORT_MASK;
        long hi = this.data.get(18) & SHORT_MASK;
        return lo | hi << 16;
    }

    public static class Builder extends HeadVehicleDataAccessor.Builder {
        public Builder() {
            this.arr = new SupplierIntArray(20);
        }

        public Builder withEnergy(LongSupplier energy) {
            this.arr.setSupplier(15, () -> Math.toIntExact(energy.getAsLong() & SHORT_MASK));
            this.arr.setSupplier(16, () -> Math.toIntExact((energy.getAsLong() >> 16) & SHORT_MASK));
            return this;
        }

        public Builder withCapacity(LongSupplier capacity) {
            this.arr.setSupplier(17, () -> Math.toIntExact(capacity.getAsLong() & SHORT_MASK));
            this.arr.setSupplier(18, () -> Math.toIntExact((capacity.getAsLong() >> 16) & SHORT_MASK));
            return this;
        }

        public EnergyHeadVehicleDataAccessor build() {
            return new EnergyHeadVehicleDataAccessor(this.arr);
        }
    }
}
