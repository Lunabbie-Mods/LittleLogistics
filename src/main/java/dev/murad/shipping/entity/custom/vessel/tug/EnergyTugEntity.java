package dev.murad.shipping.entity.custom.vessel.tug;

import dev.murad.shipping.ShippingConfig;
import dev.murad.shipping.component.EnergyComponent;
import dev.murad.shipping.component.ReadWriteEnergyStorage;
import dev.murad.shipping.entity.accessor.EnergyHeadVehicleDataAccessor;
import dev.murad.shipping.entity.container.EnergyHeadVehicleContainer;
import dev.murad.shipping.setup.ModEntityTypes;
import dev.murad.shipping.setup.ModItems;
import dev.murad.shipping.util.InventoryUtils;
import io.github.fabricators_of_create.porting_lib.transfer.item.ItemStackHandler;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;

import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import team.reborn.energy.api.EnergyStorage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class EnergyTugEntity extends AbstractTugEntity {
    private final ItemStackHandler itemHandler = createHandler();
    //private final LazyOptional<IItemHandler> handler = LazyOptional.of(() -> itemHandler);
    private static final int MAX_ENERGY = ShippingConfig.Server.ENERGY_TUG_BASE_CAPACITY.get();
    private static final int MAX_TRANSFER = ShippingConfig.Server.ENERGY_TUG_BASE_MAX_CHARGE_RATE.get();
    private static final int ENERGY_USAGE = ShippingConfig.Server.ENERGY_TUG_BASE_ENERGY_USAGE.get();

    private final ReadWriteEnergyStorage internalBattery = new ReadWriteEnergyStorage(MAX_ENERGY, MAX_TRANSFER, Integer.MAX_VALUE);
    //private final LazyOptional<IEnergyStorage> holder = LazyOptional.of(() -> internalBattery);

    public EnergyTugEntity(EntityType<? extends WaterAnimal> type, Level world) {
        super(type, world);
        internalBattery.setEnergy(0);
    }

    public EnergyTugEntity(Level worldIn, double x, double y, double z) {
        super(ModEntityTypes.ENERGY_TUG.get(), worldIn, x, y, z);
        internalBattery.setEnergy(0);
    }

    // todo: Store contents?
    @Override
    public Item getDropItem() {
        return ModItems.ENERGY_TUG.get();
    }

    @Override
    protected MenuProvider createContainerProvider() {
        return new MenuProvider() {
            @Override
            public Component getDisplayName() {
                return Component.translatable("screen.littlelogistics.energy_tug");
            }

            @Nullable
            @Override
            public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player player) {
                return new EnergyHeadVehicleContainer<EnergyTugEntity>(i, level, getDataAccessor(), playerInventory, player);
            }
        };
    }

    private ItemStackHandler createHandler() {
        return new ItemStackHandler(1) {
            @Override
            public boolean isItemValid(int slot, ItemVariant resource) {
                // TODO
                //return resource.getComponent(ModComponents.ENERGY).isPresent();
                return true;
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

    @Override
    protected void makeSmoke(){

    }

    // Energy tug can be loaded at all times since there is no concern
    // with mix-ups like with fluids and items
    @Override
    public boolean allowDockInterface(){
        return true;
    }

    @Override
    public EnergyHeadVehicleDataAccessor getDataAccessor() {
        return (EnergyHeadVehicleDataAccessor) new EnergyHeadVehicleDataAccessor.Builder()
                .withEnergy(internalBattery::getAmount)
                .withCapacity(internalBattery::getCapacity)
                .withLit(() -> internalBattery.getAmount() > 0) // has energy
                .withId(this.getId())
                .withVisitedSize(() -> nextStop)
                .withOn(() -> engineOn)
                .withCanMove(enrollmentHandler::mayMove)
                .withRouteSize(() -> path != null ? path.size() : 0)
                .build();
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        internalBattery.readAdditionalSaveData(compound.getCompound("energy_storage"));
        if(compound.contains("inv")){
            ItemStackHandler old = new ItemStackHandler();
            old.deserializeNBT(compound.getCompound("inv"));
            itemHandler.setStackInSlot(0, old.getStackInSlot(1));
        }else{
            itemHandler.deserializeNBT(compound.getCompound("tugItemHandler"));
        }
        super.readAdditionalSaveData(compound);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        CompoundTag energyNBT = new CompoundTag();
        internalBattery.addAdditionalSaveData(energyNBT);
        compound.put("energy_storage", energyNBT);
        compound.put("tugItemHandler", itemHandler.serializeNBT());
        super.addAdditionalSaveData(compound);
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
                component.insert(toExtract, Transaction.openOuter());
            }
        }

        super.tick();
    }

    @Override
    protected boolean tickFuel() {
        return internalBattery.extract(ENERGY_USAGE, Transaction.openOuter()) > 0;
    }

    @Override
    public boolean isEmpty() {
        return itemHandler.getStackInSlot(0).isEmpty();
    }

    @Override
    public ItemStack getItem(int p_70301_1_) {
        return itemHandler.getStackInSlot(p_70301_1_);
    }


    @Override
    public void setItem(int p_70299_1_, ItemStack p_70299_2_) {
        if (!this.itemHandler.isItemValid(p_70299_1_, ItemVariant.of(p_70299_2_))){
            return;
        }
        this.itemHandler.insertSlot(p_70299_1_, ItemVariant.of(p_70299_2_), this.getMaxStackSize(), Transaction.openOuter());
        if (!p_70299_2_.isEmpty() && p_70299_2_.getCount() > this.getMaxStackSize()) {
            p_70299_2_.setCount(this.getMaxStackSize());
        }
    }

//    @Nonnull
//    @Override
//    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
//        if (cap == CapabilityEnergy.ENERGY) {
//            return holder.cast();
//        }
//
//        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
//            return handler.cast();
//        }
//
//        return super.getCapability(cap, side);
//    }
}
