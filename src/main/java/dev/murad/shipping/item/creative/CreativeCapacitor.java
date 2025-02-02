package dev.murad.shipping.item.creative;

import dev.onyxstudios.cca.api.v3.component.ComponentProvider;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.Direction;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.world.item.Item.Properties;
import team.reborn.energy.api.EnergyStorage;
import io.github.fabricators_of_create.porting_lib.util.LazyOptional;
import team.reborn.energy.api.base.SimpleEnergyItem;

public class CreativeCapacitor extends Item implements SimpleEnergyItem {
    public CreativeCapacitor(Properties props) {
        super(props);
    }

    @Override
    public long getEnergyCapacity(ItemStack stack) {
        return Long.MAX_VALUE;
    }

    @Override
    public long getEnergyMaxInput(ItemStack stack) {
        return Long.MAX_VALUE;
    }

    @Override
    public long getEnergyMaxOutput(ItemStack stack) {
        return Long.MAX_VALUE;
    }

    static class CreativeEnergyStorage implements EnergyStorage {
        @Override
        public long insert(long maxReceive, TransactionContext transactionContext) {
            return maxReceive;
        }

        @Override
        public long extract(long maxExtract, TransactionContext transactionContext) {
            return maxExtract;
        }

        @Override
        public long getAmount() {
            return Long.MAX_VALUE;
        }

        @Override
        public long getCapacity() {
            return Long.MAX_VALUE;
        }

        @Override
        public boolean supportsExtraction() {
            return true;
        }

        @Override
        public boolean supportsInsertion() {
            return true;
        }
    }

    // TODO component
//    @Nullable
//    @Override
//    public ComponentProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
//        return new ICapabilityProvider() {
//            @Nonnull
//            @Override
//            public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
//                if (cap == CapabilityEnergy.ENERGY) {
//                    return LazyOptional.of(CreativeEnergyStorage::new).cast();
//                }
//                return LazyOptional.empty();
//            }
//        };
//    }
}
