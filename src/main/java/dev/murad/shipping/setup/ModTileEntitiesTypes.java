package dev.murad.shipping.setup;

import dev.murad.shipping.block.dock.BargeDockTileEntity;
import dev.murad.shipping.block.dock.TugDockTileEntity;
import dev.murad.shipping.block.energy.VesselChargerTileEntity;
import dev.murad.shipping.block.fluid.FluidHopperTileEntity;
import dev.murad.shipping.block.rail.blockentity.LocomotiveDockTileEntity;
import dev.murad.shipping.block.rail.blockentity.TrainCarDockTileEntity;
import dev.murad.shipping.block.rapidhopper.RapidHopperTileEntity;
import dev.murad.shipping.block.vesseldetector.VesselDetectorTileEntity;
import io.github.fabricators_of_create.porting_lib.util.RegistryObject;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class ModTileEntitiesTypes {
    public static final RegistryObject<BlockEntityType<TugDockTileEntity>> TUG_DOCK = register(
            "tug_dock",
            TugDockTileEntity::new,
            ModBlocks.TUG_DOCK
    );

    public static final RegistryObject<BlockEntityType<BargeDockTileEntity>> BARGE_DOCK = register(
            "barge_dock",
            BargeDockTileEntity::new,
            ModBlocks.BARGE_DOCK
    );

    public static final RegistryObject<BlockEntityType<LocomotiveDockTileEntity>> LOCOMOTIVE_DOCK = register(
            "locomotive_dock",
            LocomotiveDockTileEntity::new,
            ModBlocks.LOCOMOTIVE_DOCK_RAIL
    );

    public static final RegistryObject<BlockEntityType<TrainCarDockTileEntity>> CAR_DOCK = register(
            "car_dock",
            TrainCarDockTileEntity::new,
            ModBlocks.CAR_DOCK_RAIL
    );

    public static final RegistryObject<BlockEntityType<VesselDetectorTileEntity>> VESSEL_DETECTOR = register(
            "vessel_detector",
            VesselDetectorTileEntity::new,
            ModBlocks.VESSEL_DETECTOR
    );

    public static final RegistryObject<BlockEntityType<FluidHopperTileEntity>> FLUID_HOPPER = register(
            "fluid_hopper",
            FluidHopperTileEntity::new,
            ModBlocks.FLUID_HOPPER
    );

    public static final RegistryObject<BlockEntityType<VesselChargerTileEntity>> VESSEL_CHARGER = register(
            "vessel_charger",
            VesselChargerTileEntity::new,
            ModBlocks.VESSEL_CHARGER
    );

    public static final RegistryObject<BlockEntityType<RapidHopperTileEntity>> RAPID_HOPPER = register(
            "rapid_hopper",
            RapidHopperTileEntity::new,
            ModBlocks.RAPID_HOPPER
    );

    private static <T extends BlockEntity> RegistryObject<BlockEntityType<T>> register(
            String name,
            BlockEntityType.BlockEntitySupplier<T> factory,
            RegistryObject<? extends Block> block) {
        return Registration.TILE_ENTITIES.register(name, () ->
                BlockEntityType.Builder.of(factory, block.get()).build(null));
    }

    public static void register () {

    }
}
