package dev.murad.shipping.block.fluid;

import dev.murad.shipping.block.IVesselLoader;
import dev.murad.shipping.component.FluidHandlerComponent;
import dev.murad.shipping.setup.ModComponents;
import dev.murad.shipping.setup.ModTileEntitiesTypes;
import dev.murad.shipping.util.FluidDisplayUtil;
import dev.murad.shipping.util.LinkableEntity;
import dev.onyxstudios.cca.api.v3.component.Component;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import io.github.fabricators_of_create.porting_lib.transfer.TransferUtil;
import io.github.fabricators_of_create.porting_lib.transfer.fluid.FluidTank;
import io.github.fabricators_of_create.porting_lib.transfer.fluid.item.FluidHandlerItemStack;
import io.github.fabricators_of_create.porting_lib.util.LazyOptional;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

public class FluidHopperTileEntity extends BlockEntity implements IVesselLoader {
    public static final long CAPACITY = FluidConstants.BUCKET * 10;
    private int cooldownTime = 0;

    public FluidHopperTileEntity(BlockPos pos, BlockState state) {
        super(ModTileEntitiesTypes.FLUID_HOPPER.get(), pos, state);
    }

    protected FluidTank tank = new FluidTank(CAPACITY) {
        @Override
        protected void onContentsChanged() {
            BlockState state = level.getBlockState(worldPosition);
            level.sendBlockUpdated(worldPosition, state, state, 3);
            setChanged();
        };
    };

    private final LazyOptional<FluidHandlerItemStack> holder = LazyOptional.of(() -> tank);



    public boolean use(Player player, InteractionHand hand){
        boolean result = FluidUtil.interactWithFluidHandler(player, hand, tank);
        player.displayClientMessage(FluidDisplayUtil.getFluidDisplay(tank), false);
        return result;
    }


//    @Override
//    @Nonnull
//    public <T extends Component> LazyOptional<T> getCapability(@Nonnull ComponentKey<T> capability, @Nullable Direction facing) {
//        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
//            return holder.cast();
//        return super.getCapability(capability, facing);
//    }

    public FluidTank getTank() {
        return this.tank;
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.getTank().readFromNBT(tag);
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        this.getTank().writeToNBT(tag);
    }

    @Nonnull
    @Override
    public CompoundTag getUpdateTag() {
        var tag = new CompoundTag();
        saveAdditional(tag);    // okay to send entire inventory on chunk load
        return tag;
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket packet) {
        this.load(packet.getTag());
    }

    private void serverTickInternal() {
        if (this.level != null) {
            --this.cooldownTime;
            if (this.cooldownTime <= 0) {
                // do not short-circuit
                this.cooldownTime = Boolean.logicalOr(this.tryExportFluid(), tryImportFluid()) ? 0 : 10;
            }
        }
    }

    private Optional<FluidHandlerComponent> getExternalFluidHandler(BlockPos pos){
        return Optional.ofNullable(this.level.getBlockEntity(pos))
                .map(tile -> tile.getComponent(ModComponents.FLUID_HANDLER))
                .flatMap(LazyOptional::resolve)
                .map(Optional::of).orElseGet(() -> Optional.of(IVesselLoader.getEntityComponent(pos, ModComponents.FLUID_HANDLER, this.level)));

    }

    private boolean tryImportFluid() {
        return getExternalFluidHandler(this.getBlockPos().above()).map(iFluidHandler ->
                TransferUtil.insertFluid(tank, iFluidHandler.stack(50)) != 0
        ).orElse(false);
    }

    private boolean tryExportFluid() {
        return getExternalFluidHandler(this.getBlockPos().relative(this.getBlockState().getValue(FluidHopperBlock.FACING)))
                .map(iFluidHandler ->
                        TransferUtil.extractFluid(tank, iFluidHandler.stack(50)) != 0
        ).orElse(false);
    }

    @Override
    public<T extends Entity & LinkableEntity<T>> boolean hold(T vehicle, Mode mode) {
        return Optional.of(vehicle.getComponent(ModComponents.FLUID_HANDLER)).map(iFluidHandler -> {
            switch (mode) {
                case IMPORT:
                    return TransferUtil.insertFluid(tank, iFluidHandler.stack(1)) != 0;
                case EXPORT:
                    return TransferUtil.extractFluid(tank, iFluidHandler.stack(1)) != 0;
                default:
                    return false;
            }
        }).orElse(false);
    }

    public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, FluidHopperTileEntity e) {
        e.serverTickInternal();
    }
}
