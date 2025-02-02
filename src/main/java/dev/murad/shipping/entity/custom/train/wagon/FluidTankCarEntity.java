package dev.murad.shipping.entity.custom.train.wagon;

import dev.murad.shipping.entity.custom.vessel.tug.AbstractTugEntity;
import dev.murad.shipping.setup.ModEntityTypes;
import dev.murad.shipping.setup.ModItems;
import dev.murad.shipping.util.FluidDisplayUtil;
import io.github.fabricators_of_create.porting_lib.transfer.fluid.FluidTank;
import io.github.fabricators_of_create.porting_lib.transfer.fluid.item.FluidHandlerItemStack;
import io.github.fabricators_of_create.porting_lib.util.FluidStack;
import io.github.fabricators_of_create.porting_lib.util.LazyOptional;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class FluidTankCarEntity extends AbstractWagonEntity {
    public static long CAPACITY = FluidConstants.BUCKET * 10;
    protected FluidTank tank = new FluidTank(CAPACITY){
        @Override
        protected void onContentsChanged(){
            sendInfoToClient();
        }
    };
    private static final EntityDataAccessor<Integer> VOLUME = SynchedEntityData.defineId(AbstractTugEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<String> FLUID_TYPE = SynchedEntityData.defineId(AbstractTugEntity.class, EntityDataSerializers.STRING);
    private Fluid clientCurrFluid = Fluids.EMPTY;
    private int clientCurrAmount = 0;
    private final LazyOptional<FluidHandlerItemStack> holder = LazyOptional.of(() -> tank);

    public FluidTankCarEntity(EntityType<?> p_38087_, Level p_38088_) {
        super(p_38087_, p_38088_);
    }

    public FluidTankCarEntity(Level level, Double aDouble, Double aDouble1, Double aDouble2) {
        super(ModEntityTypes.FLUID_CAR.get(), level, aDouble, aDouble1, aDouble2);

    }

    @Override
    public ItemStack getPickResult() {
        return new ItemStack(ModItems.FLUID_CAR.get());
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(FLUID_TYPE, "minecraft:empty");
        entityData.define(VOLUME, 0);
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand){
        if(!this.level.isClientSide){
            FluidUtil.interactWithFluidHandler(player, InteractionHand.MAIN_HAND, tank);
            player.displayClientMessage(FluidDisplayUtil.getFluidDisplay(tank), false);
        }
        return InteractionResult.CONSUME;
    }

    public FluidStack getFluidStack(){
        return tank.getFluid();
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag)
    {
        super.readAdditionalSaveData(tag);
        tank.readFromNBT(tag);
        sendInfoToClient();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag)
    {
        super.addAdditionalSaveData(tag);
        tank.writeToNBT(tag);
    }

    private void sendInfoToClient(){
        entityData.set(VOLUME, tank.getFluidAmount());
        entityData.set(FLUID_TYPE, Registry.FLUID.getKey(tank.getFluid().getFluid()).toString());
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);

        if(level.isClientSide) {
            if(VOLUME.equals(key)) {
                clientCurrAmount =  entityData.get(VOLUME);
                tank.setFluid(new FluidStack(clientCurrFluid, clientCurrAmount));
            } else if (FLUID_TYPE.equals(key)) {
                ResourceLocation fluidName = new ResourceLocation(entityData.get(FLUID_TYPE));
                clientCurrFluid = Registry.FLUID.getValue(fluidName);
                tank.setFluid(new FluidStack(clientCurrFluid, clientCurrAmount));
            }
        }
    }

//    @Override
//    @Nonnull
//    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing)
//    {
//        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
//            return holder.cast();
//        return super.getCapability(capability, facing);
//    }
}
