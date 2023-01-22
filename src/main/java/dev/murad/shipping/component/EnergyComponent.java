package dev.murad.shipping.component;

import dev.onyxstudios.cca.api.v3.component.Component;

public interface EnergyComponent extends Component {
    long getEnergyStored();

    long getMaxEnergyStored();

    int receiveEnergy(int maxTransfer, boolean b);
}
