package dev.murad.shipping.data;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.providers.RegistrateProvider;

public class ShippingRegistrate extends AbstractRegistrate<ShippingRegistrate> {
    /**
     * Construct a new Registrate for the given mod ID.
     *
     * @param modid The mod ID for which objects will be registered
     */
    protected ShippingRegistrate(String modid) {
        super(modid);
    }

    public static ShippingRegistrate create(String modId) {
        return new ShippingRegistrate(modId);
    }
}
