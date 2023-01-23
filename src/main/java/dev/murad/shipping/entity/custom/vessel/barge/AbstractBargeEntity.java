package dev.murad.shipping.entity.custom.vessel.barge;


import dev.murad.shipping.component.StallingComponent;
import dev.murad.shipping.entity.custom.vessel.VesselEntity;
import dev.murad.shipping.entity.custom.vessel.tug.AbstractTugEntity;
import dev.murad.shipping.setup.ModComponents;
import dev.murad.shipping.util.Train;
import io.github.fabricators_of_create.porting_lib.util.LazyOptional;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nonnull;
import java.util.Optional;

public abstract class AbstractBargeEntity extends VesselEntity {
    public AbstractBargeEntity(EntityType<? extends AbstractBargeEntity> type, Level world) {
        super(type, world);
        this.blocksBuilding = true;
        linkingHandler.train = new Train<>(this);
    }

    public AbstractBargeEntity(EntityType<? extends AbstractBargeEntity> type, Level worldIn, double x, double y, double z) {
        this(type, worldIn);
        this.setPos(x, y, z);
        this.setDeltaMovement(Vec3.ZERO);
        this.xo = x;
        this.yo = y;
        this.zo = z;
    }

    @Override
    protected boolean canAddPassenger(Entity passenger) {
        return false;
    }


    public abstract Item getDropItem();


    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (!this.level.isClientSide) {
            doInteract(player);
        }
        // don't interact *and* use current item
        return InteractionResult.CONSUME;
    }

    abstract protected void doInteract(Player player);

    public boolean hasWaterOnSides(){
        return super.hasWaterOnSides();
    }

    @Override
    public void setDominated(VesselEntity entity) {
        linkingHandler.dominated = Optional.of(entity);
    }

    @Override
    public void setDominant(VesselEntity entity) {
        this.setTrain(entity.getTrain());
        linkingHandler.dominant = Optional.of(entity);
    }

    @Override
    public void removeDominated() {
        if(!this.isAlive()){
            return;
        }
        linkingHandler.dominated = Optional.empty();
        linkingHandler.train.setTail(this);
    }

    @Override
    public void removeDominant() {
        if(!this.isAlive()){
            return;
        }
        linkingHandler.dominant = Optional.empty();
        this.setTrain(new Train(this));
    }

    @Override
    public void setTrain(Train<VesselEntity> train) {
        linkingHandler.train = train;
        train.setTail(this);
        linkingHandler.dominated.ifPresent(dominated -> {
            // avoid recursion loops
            if(!dominated.getTrain().equals(train)){
                dominated.setTrain(train);
            }
        });
    }

    @Override
    public void remove(RemovalReason r){
        if (!this.level.isClientSide) {
            this.spawnAtLocation(this.getDropItem());
        }
        super.remove(r);
    }

    // hack to disable hoppers
    public boolean isDockable() {
        return this.linkingHandler.dominant.map(dom -> this.distanceToSqr((Entity) dom) < 1.1).orElse(true);
    }

    public boolean allowDockInterface(){
        return isDockable();
    }

    public static StallingComponent createStallingComponent(AbstractBargeEntity entity) {
        return new StallingComponent() {
            @Override
            public void readFromNbt(CompoundTag tag) {

            }

            @Override
            public void writeToNbt(CompoundTag tag) {

            }

            @Override
            public boolean isDocked() {
                return entity.delegate().map(StallingComponent::isDocked).orElse(false);
            }

            @Override
            public void dock(double x, double y, double z) {
                entity.delegate().ifPresent(s -> s.dock(x, y, z));
            }

            @Override
            public void undock() {
                entity.delegate().ifPresent(StallingComponent::undock);
            }

            @Override
            public boolean isStalled() {
                return entity.delegate().map(StallingComponent::isStalled).orElse(false);
            }

            @Override
            public void stall() {
                entity.delegate().ifPresent(StallingComponent::stall);
            }

            @Override
            public void unstall() {
                entity.delegate().ifPresent(StallingComponent::unstall);
            }

            @Override
            public boolean isFrozen() {
                return entity.isFrozen();
            }

            @Override
            public void freeze() {
                entity.setFrozen(true);
            }

            @Override
            public void unfreeze() {
                entity.setFrozen(false);
            }
        };
    }

    private Optional<StallingComponent> delegate() {
        if (linkingHandler.train.getHead() instanceof AbstractTugEntity e) {
            return Optional.of(e.getComponent(ModComponents.STALLING));
        }
        return Optional.empty();
    }

    //private final LazyOptional<StallingComponent> capabilityOpt = LazyOptional.of(() -> capability);

//    @Nonnull
//    @Override
//    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap) {
//        if (cap == StallingComponent.STALLING_CAPABILITY) {
//            return capabilityOpt.cast();
//        }
//        return super.getCapability(cap);
//    }
}
