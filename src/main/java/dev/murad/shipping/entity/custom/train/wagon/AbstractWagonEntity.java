package dev.murad.shipping.entity.custom.train.wagon;

import dev.murad.shipping.component.StallingComponent;
import dev.murad.shipping.entity.custom.train.AbstractTrainCarEntity;
import dev.murad.shipping.entity.custom.train.locomotive.AbstractLocomotiveEntity;
import dev.murad.shipping.setup.ModComponents;
import dev.murad.shipping.util.Train;
import io.github.fabricators_of_create.porting_lib.util.LazyOptional;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nonnull;
import java.util.Optional;

public abstract class AbstractWagonEntity extends AbstractTrainCarEntity {

    public AbstractWagonEntity(EntityType<?> p_38087_, Level p_38088_) {
        super(p_38087_, p_38088_);
    }

    public AbstractWagonEntity(EntityType<?> p_38087_, Level level, Double aDouble, Double aDouble1, Double aDouble2) {
        super(p_38087_, level, aDouble, aDouble1, aDouble2);
    }

    @Override
    public void setDominated(AbstractTrainCarEntity entity) {
        linkingHandler.dominated = Optional.of(entity);
    }


    @Override
    public void setDominant(AbstractTrainCarEntity entity) {
        this.setTrain(entity.getTrain());
        linkingHandler.dominant = Optional.of(entity);
    }

    @Override
    public void tick() {
        if(capability.isFrozen() || linkingHandler.train.getTug().map(s -> (AbstractLocomotiveEntity) s).map(AbstractLocomotiveEntity::shouldFreezeTrain).orElse(false)){
            this.setDeltaMovement(Vec3.ZERO);
        } else {
            super.tick();
        }
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
        this.setTrain(new Train<>(this));
    }

    @Override
    public void setTrain(Train<AbstractTrainCarEntity> train) {
        linkingHandler.train = train;
        train.setTail(this);
        linkingHandler.dominated.ifPresent(dominated -> {
            // avoid recursion loops
            if(!dominated.getTrain().equals(train)){
                dominated.setTrain(train);
            }
        });
    }

    // hack to disable hoppers
    public boolean isDockable() {
        return linkingHandler.dominant.map(dom -> this.distanceToSqr(dom) < 1.05).orElse(true);
    }


    public boolean allowDockInterface(){
        return isDockable();
    }

    private final StallingComponent capability = new StallingComponent() {
        @Override
        public void readFromNbt(CompoundTag tag) {

        }

        @Override
        public void writeToNbt(CompoundTag tag) {

        }

        @Override
        public boolean isDocked() {
            return delegate().map(StallingComponent::isDocked).orElse(false);
        }

        @Override
        public void dock(double x, double y, double z) {
            delegate().ifPresent(s -> s.dock(x, y, z));
        }

        @Override
        public void undock() {
            delegate().ifPresent(StallingComponent::undock);
        }

        @Override
        public boolean isStalled() {
            return delegate().map(StallingComponent::isStalled).orElse(false);
        }

        @Override
        public void stall() {
            delegate().ifPresent(StallingComponent::stall);
        }

        @Override
        public void unstall() {
            delegate().ifPresent(StallingComponent::unstall);
        }

        @Override
        public boolean isFrozen() {
            return AbstractWagonEntity.super.isFrozen();
        }

        @Override
        public void freeze() {
            AbstractWagonEntity.super.setFrozen(true);
        }

        @Override
        public void unfreeze() {
            AbstractWagonEntity.super.setFrozen(false);
        }

        private Optional<StallingComponent> delegate() {
            if (linkingHandler.train.getHead() instanceof AbstractLocomotiveEntity e) {
                return Optional.of(e.getComponent(ModComponents.STALLING));
            }
            return Optional.empty();
        }
    };

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
