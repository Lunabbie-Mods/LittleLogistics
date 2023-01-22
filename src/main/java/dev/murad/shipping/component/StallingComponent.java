package dev.murad.shipping.component;


import dev.onyxstudios.cca.api.v3.component.Component;

public interface StallingComponent extends Component {
    //Capability<StallingComponent> STALLING_CAPABILITY = CapabilityManager.get(new CapabilityToken<StallingComponent>(){});
//    static void register(RegisterCapabilitiesEvent event) {
//        event.register(StallingComponent.class);
//    }

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
