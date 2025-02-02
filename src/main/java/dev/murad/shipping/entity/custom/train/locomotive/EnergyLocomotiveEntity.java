package dev.murad.shipping.entity.custom.train.locomotive;

import dev.murad.shipping.ShippingConfig;
import dev.murad.shipping.component.EnergyComponent;
import dev.murad.shipping.component.ReadWriteEnergyStorage;
import dev.murad.shipping.entity.accessor.EnergyHeadVehicleDataAccessor;
import dev.murad.shipping.entity.container.EnergyHeadVehicleContainer;
import dev.murad.shipping.setup.ModComponents;
import dev.murad.shipping.setup.ModEntityTypes;
import dev.murad.shipping.setup.ModItems;
import dev.murad.shipping.util.InventoryUtils;
import dev.murad.shipping.util.ItemHandlerVanillaContainerWrapper;
import io.github.fabricators_of_create.porting_lib.transfer.item.ItemStackHandler;
import io.github.fabricators_of_create.porting_lib.util.LazyOptional;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import team.reborn.energy.api.EnergyStorage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class EnergyLocomotiveEntity extends AbstractLocomotiveEntity implements ItemHandlerVanillaContainerWrapper, WorldlyContainer {
    private final ItemStackHandler itemHandler = createHandler();
    private final LazyOptional<ItemStackHandler> handler = LazyOptional.of(() -> itemHandler);
    private static final int MAX_ENERGY = ShippingConfig.Server.ENERGY_LOCO_BASE_CAPACITY.get();
    private static final int MAX_TRANSFER = ShippingConfig.Server.ENERGY_LOCO_BASE_MAX_CHARGE_RATE.get();
    private static final int ENERGY_USAGE = ShippingConfig.Server.ENERGY_LOCO_BASE_ENERGY_USAGE.get();

    private final ReadWriteEnergyStorage internalBattery = new ReadWriteEnergyStorage(MAX_ENERGY, MAX_TRANSFER, Integer.MAX_VALUE);
    private final LazyOptional<EnergyStorage> holder = LazyOptional.of(() -> internalBattery);

    public EnergyLocomotiveEntity(EntityType<?> type, Level p_38088_) {
        super(type, p_38088_);
        internalBattery.setEnergy(0);
    }

    public EnergyLocomotiveEntity(Level level, Double aDouble, Double aDouble1, Double aDouble2) {
        super(ModEntityTypes.ENERGY_LOCOMOTIVE.get(), level, aDouble, aDouble1, aDouble2);
        internalBattery.setEnergy(0);
    }

    private ItemStackHandler createHandler() {
        return new ItemStackHandler(1) {
            @Override
            public boolean isItemValid(int slot, ItemVariant resource) {
                return resource.getComponent(ModComponents.ENERGY).isPresent();
            }

            @Nonnull
            @Override
            public long insertSlot(int slot, ItemVariant resource, long maxAmount, TransactionContext transaction) {
                if (!isItemValid(slot, resource)) {
                    return 0;
                }

                return super.insertSlot(slot, resource, maxAmount, transaction);
            }
        };
    }

//    @Nonnull
//    @Override
//    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
//        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
//            return handler.cast();
//        } else if (cap == CapabilityEnergy.ENERGY) {
//            return holder.cast();
//        }
//
//        return super.getCapability(cap, side);
//    }

    @Override
    public void remove(RemovalReason r) {
        if(!this.level.isClientSide){
            Containers.dropContents(this.level, this, this);
        }
        super.remove(r);
    }

    @Override
    protected MenuProvider createContainerProvider() {
        return new MenuProvider() {
            @Override
            public Component getDisplayName() {
                return Component.translatable("entity.littlelogistics.energy_locomotive");
            }

            @Nullable
            @Override
            public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player player) {
                return new EnergyHeadVehicleContainer<EnergyLocomotiveEntity>(i, level, getDataAccessor(), playerInventory, player);
            }
        };
    }

    @Override
    public EnergyHeadVehicleDataAccessor getDataAccessor() {
        return (EnergyHeadVehicleDataAccessor) new EnergyHeadVehicleDataAccessor.Builder()
                .withEnergy(internalBattery::getAmount)
                .withCapacity(internalBattery::getCapacity)
                .withLit(() -> internalBattery.getAmount() > 0) // has energy
                .withId(this.getId())
                .withOn(() -> engineOn)
                .withRouteSize(() -> navigator.getRouteSize())
                .withVisitedSize(() -> navigator.getVisitedSize())
                .withCanMove(enrollmentHandler::mayMove)
                .build();
    }

    @Override
    public void tick() {
        // grab energy from capacitor
        if (!level.isClientSide) {
            EnergyComponent component = InventoryUtils.getEnergyComponentInSlot(0, itemHandler);
            if (component != null) {
                // simulate first
                long toExtract = component.extract(MAX_TRANSFER, Transaction.openOuter());
                toExtract = internalBattery.insert(toExtract, Transaction.openOuter());
                component.extract(toExtract, Transaction.openOuter());
            }
        }

        super.tick();
    }

    @Override
    protected boolean tickFuel() {
        return internalBattery.extract(ENERGY_USAGE, Transaction.openOuter()) > 0;
    }


    @Override
    public ItemStack getPickResult() {
        return new ItemStack(ModItems.ENERGY_LOCOMOTIVE.get());
    }

    @Override
    public ItemStackHandler getRawHandler() {
        return itemHandler;
    }

    @Override
    public int[] getSlotsForFace(Direction p_180463_1_) {
        return new int[]{0};
    }

    @Override
    public boolean canPlaceItemThroughFace(int p_180462_1_, ItemStack p_180462_2_, @Nullable Direction p_180462_3_) {
        return stalling.isDocked();
    }

    @Override
    public boolean canTakeItemThroughFace(int p_180461_1_, ItemStack p_180461_2_, Direction p_180461_3_) {
        return false;
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        itemHandler.deserializeNBT(compound.getCompound("inv"));
        internalBattery.readAdditionalSaveData(compound.getCompound("energy_storage"));
        super.readAdditionalSaveData(compound);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        compound.put("inv", itemHandler.serializeNBT());
        CompoundTag energyNBT = new CompoundTag();
        internalBattery.addAdditionalSaveData(energyNBT);
        compound.put("energy_storage", energyNBT);
        super.addAdditionalSaveData(compound);
    }
}
