package dev.murad.shipping.component;

import dev.onyxstudios.cca.api.v3.component.Component;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;

public interface EnergyComponent extends Component {
    long getEnergyStored();

    long getMaxEnergyStored();

    long receiveEnergy(long maxTransfer, boolean b);

    long extract(long maxTransfer, Transaction transaction);

    void insert(long toExtract, Transaction transaction);
}
