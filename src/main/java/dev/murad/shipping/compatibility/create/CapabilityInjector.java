package dev.murad.shipping.compatibility.create;

import com.simibubi.create.content.contraptions.components.structureMovement.train.capability.CapabilityMinecartController;
import com.simibubi.create.content.contraptions.components.structureMovement.train.capability.MinecartController;
import dev.murad.shipping.component.StallingComponent;
import dev.murad.shipping.entity.custom.train.wagon.SeaterCarEntity;
import dev.murad.shipping.setup.ModComponents;
import dev.onyxstudios.cca.api.v3.component.Component;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import io.github.fabricators_of_create.porting_lib.util.LazyOptional;
import org.jetbrains.annotations.NotNull;

public class CapabilityInjector {

    public static class TrainCarController extends MinecartController {
        public static TrainCarController EMPTY;
        private final LazyOptional<StallingComponent> stallingCapability;

        public TrainCarController(SeaterCarEntity entity) {
            super(entity);
            stallingCapability = entity == null ? LazyOptional.empty() : LazyOptional.ofObject(entity.getComponent(ModComponents.STALLING));
        }

        public boolean isStalled() {
            return stallingCapability.map(StallingComponent::isFrozen).orElse(false);
        }

        public void setStalledExternally(boolean stall) {
            stallingCapability.ifPresent(cap -> {
                if (stall) {
                    cap.freeze();
                } else {
                    cap.unfreeze();
                }
            });
        }

        public static TrainCarController empty() {
            return EMPTY != null ? EMPTY : (EMPTY = new TrainCarController(null));
        }
    }

    public static LazyOptional<?> constructMinecartControllerCapability(SeaterCarEntity entity) {
        return LazyOptional.of(() -> new TrainCarController(entity));
    }

    public static <T extends Component> boolean isMinecartControllerComponent(@NotNull ComponentKey<T> cap) {
        // TODO
        // return cap == CapabilityMinecartController.MINECART_CONTROLLER_CAPABILITY;
        return false;
    }
}
