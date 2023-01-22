package dev.murad.shipping.capability;


import dev.onyxstudios.cca.api.v3.component.Component;

public interface StallingCapability extends Component {
    Capability<StallingCapability> STALLING_CAPABILITY = CapabilityManager.get(new CapabilityToken<StallingCapability>(){});
    static void register(RegisterCapabilitiesEvent event) {
        event.register(StallingCapability.class);
    }

    boolean isDocked();
    void dock(double x, double y, double z);
    void undock();

    boolean isStalled();
    void stall();
    void unstall();

    boolean isFrozen();
    void freeze();
    void unfreeze();
}
