package dev.murad.shipping.component;

import dev.onyxstudios.cca.api.v3.component.Component;
import io.github.fabricators_of_create.porting_lib.util.FluidStack;

public interface FluidHandlerComponent extends Component {
    FluidStack stack(int amount);
}
