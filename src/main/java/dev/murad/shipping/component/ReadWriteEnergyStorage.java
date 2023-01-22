package dev.murad.shipping.component;

import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.nbt.CompoundTag;
import team.reborn.energy.api.EnergyStorage;
import team.reborn.energy.api.base.SimpleEnergyStorage;

/**
 * Re-implementation of EnergyStorage so we can read and write it from/to NBT data
 */
public class ReadWriteEnergyStorage implements EnergyStorage {
    public static final String ENERGY_TAG = "energy";

    private final int maxCapacity, maxReceive, maxExtract;
    private SimpleEnergyStorage proxyStorage;

    public ReadWriteEnergyStorage(int maxCapacity, int maxReceive, int maxExtract)
    {
        this.maxCapacity = maxCapacity;
        this.maxReceive = maxReceive;
        this.maxExtract = maxExtract;
        proxyStorage = null;
    }

    private int clampInclusive(int n, int lo, int hi) {
        return Math.max(lo, Math.min(n, hi));
    }

    // when a TileEntity/Item/Entity is created, call this to set it up
    public void setEnergy(long energy) {
        proxyStorage = new SimpleEnergyStorage(maxCapacity, maxReceive, maxExtract);
        proxyStorage.amount = energy;
    }

    public void readAdditionalSaveData(CompoundTag compound) {
        int energy = compound.contains(ENERGY_TAG) ? compound.getInt(ENERGY_TAG) : 0;
        proxyStorage = new SimpleEnergyStorage(maxCapacity, maxReceive, maxExtract);
        proxyStorage.amount = energy;
    }

    public void addAdditionalSaveData(CompoundTag compound) {
        compound.putFloat(ENERGY_TAG, proxyStorage.getAmount());
    }

    @Override
    public long insert(long maxAmount, TransactionContext transaction) {
        return proxyStorage.insert(maxReceive, transaction);
    }

    @Override
    public long extract(long maxAmount, TransactionContext transaction) {
        return proxyStorage.extract(maxExtract, transaction);
    }

    @Override
    public long getAmount() {
        return proxyStorage.getAmount();
    }

    @Override
    public long getCapacity() {
        return proxyStorage.getCapacity();
    }

    @Override
    public boolean supportsExtraction() {
        return proxyStorage.supportsExtraction();
    }

    @Override
    public boolean supportsInsertion() {
        return proxyStorage.supportsInsertion();
    }
}
