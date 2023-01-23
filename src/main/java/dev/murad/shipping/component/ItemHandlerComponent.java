package dev.murad.shipping.component;

import dev.onyxstudios.cca.api.v3.component.Component;
import net.minecraft.world.item.ItemStack;

public interface ItemHandlerComponent extends Component {
    boolean isItemValid(Integer j, ItemStack stack);
}
