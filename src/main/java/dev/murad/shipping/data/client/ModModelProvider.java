package dev.murad.shipping.data.client;


import dev.murad.shipping.ShippingMod;
import dev.murad.shipping.block.dock.DockingBlockStates;
import dev.murad.shipping.block.energy.VesselChargerBlock;
import dev.murad.shipping.block.fluid.FluidHopperBlock;
import dev.murad.shipping.block.guiderail.CornerGuideRailBlock;
import dev.murad.shipping.block.rail.AbstractDockingRail;
import dev.murad.shipping.block.rail.SwitchRail;
import dev.murad.shipping.block.vesseldetector.VesselDetectorBlock;
import dev.murad.shipping.setup.ModBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.core.Direction;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.HopperBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ModelFile;

public class ModModelProvider extends FabricModelProvider {
    public ModModelProvider(FabricDataGenerator generator) {
        super(generator);
    }

    private ItemModelBuilder builder(ModelFile itemGenerated, String name) {
        return getBuilder(name).parent(itemGenerated).texture("layer0", "item/" + name);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators blockStateModelGenerator) {
        getVariantBuilder(ModBlocks.TUG_DOCK.get()).forAllStates(state -> ConfiguredModel.builder()
                .modelFile(getTugDockModel(state))
                .rotationY((int) state.getValue(DockingBlockStates.FACING).getOpposite().toYRot())
                .build()
        );

        getVariantBuilder(ModBlocks.BARGE_DOCK.get()).forAllStates(state -> ConfiguredModel.builder()
                .modelFile(getBargeDockModel(state))
                .rotationY((int) state.getValue(DockingBlockStates.FACING).getOpposite().toYRot())
                .build()
        );

        getVariantBuilder(ModBlocks.GUIDE_RAIL_CORNER.get()).forAllStates(state -> ConfiguredModel.builder()
                .modelFile(getCornerGuideRailModel(state))
                .rotationY((int) state.getValue(CornerGuideRailBlock.FACING).getOpposite().toYRot())
                .build()
        );

        getVariantBuilder(ModBlocks.VESSEL_DETECTOR.get()).forAllStates(state -> ConfiguredModel.builder()
                .modelFile(getVesselDetectorModel(state))
                .rotationY((int) state.getValue(VesselDetectorBlock.FACING).getOpposite().toYRot())
                .rotationX(xRotFromDir(state.getValue(VesselDetectorBlock.FACING).getOpposite()))
                .build()
        );

        getVariantBuilder(ModBlocks.GUIDE_RAIL_TUG.get()).forAllStates(state -> ConfiguredModel.builder()
                .modelFile(getTugGuideRailModel(state))
                .rotationY((int) state.getValue(CornerGuideRailBlock.FACING).getClockWise().toYRot())
                .build()
        );

        getVariantBuilder(ModBlocks.FLUID_HOPPER.get()).forAllStates(state -> ConfiguredModel.builder()
                .modelFile(models()
                        .withExistingParent("fluid_hopper", modLoc("fluid_hopper_parent_model"))
                )
                .rotationY((int) state.getValue(FluidHopperBlock.FACING).getClockWise().toYRot())
                .build()
        );

        getVariantBuilder(ModBlocks.VESSEL_CHARGER.get()).forAllStates(state -> ConfiguredModel.builder()
                .modelFile(models()
                        .withExistingParent("vessel_charger", modLoc("vessel_charger_parent_model"))
                )
                .rotationY((int) state.getValue(VesselChargerBlock.FACING).getOpposite().toYRot())
                .build()
        );

        getVariantBuilder(ModBlocks.SWITCH_RAIL.get()).forAllStates(state ->  {
            String outDir = state.getValue(SwitchRail.OUT_DIRECTION).getSerializedName();
            String powered = state.getValue(SwitchRail.POWERED) ? "on" : "off";
            return ConfiguredModel.builder()
                    .modelFile(models()
                            .withExistingParent("switch_rail_" + outDir + "_" + powered, mcLoc("rail_flat"))
                            .texture("rail", getBlTx("switch_rail_" + outDir + "_" + powered)))
                    .rotationY((int) state.getValue(SwitchRail.FACING).getOpposite().toYRot())
                    .build();
        });

        getVariantBuilder(ModBlocks.AUTOMATIC_SWITCH_RAIL.get()).forAllStates(state ->  {
            String outDir = state.getValue(SwitchRail.OUT_DIRECTION).getSerializedName();
            String powered = state.getValue(SwitchRail.POWERED) ? "on" : "off";
            return ConfiguredModel.builder()
                    .modelFile(models()
                            .withExistingParent("automatic_switch_rail_" + outDir + "_" + powered, mcLoc("rail_flat"))
                            .texture("rail", getBlTx("automatic_switch_rail_" + outDir + "_" + powered)))
                    .rotationY((int) state.getValue(SwitchRail.FACING).getOpposite().toYRot())
                    .build();
        });

        getVariantBuilder(ModBlocks.TEE_JUNCTION_RAIL.get()).forAllStates(state ->  {
            String powered = state.getValue(SwitchRail.POWERED) ? "on" : "off";
            return ConfiguredModel.builder()
                    .modelFile(models()
                            .withExistingParent("tee_junction_rail_" + powered, mcLoc("rail_flat"))
                            .texture("rail", getBlTx("tee_junction_rail_" + powered)))
                    .rotationY((int) state.getValue(SwitchRail.FACING).getOpposite().toYRot())
                    .build();
        });

        getVariantBuilder(ModBlocks.AUTOMATIC_TEE_JUNCTION_RAIL.get()).forAllStates(state ->  {
            String powered = state.getValue(SwitchRail.POWERED) ? "on" : "off";
            return ConfiguredModel.builder()
                    .modelFile(models()
                            .withExistingParent("automatic_tee_junction_rail_" + powered, mcLoc("rail_flat"))
                            .texture("rail", getBlTx("automatic_tee_junction_rail_" + powered)))
                    .rotationY((int) state.getValue(SwitchRail.FACING).getOpposite().toYRot())
                    .build();
        });

        getVariantBuilder(ModBlocks.JUNCTION_RAIL.get()).forAllStates(state -> ConfiguredModel.builder()
                .modelFile(models()
                        .withExistingParent("junction_rail", mcLoc("rail_flat"))
                        .texture("rail", getBlTx("junction_rail")))
                .build());

        getVariantBuilder(ModBlocks.CAR_DOCK_RAIL.get()).forAllStates(state -> {
            String inv = state.getValue(DockingBlockStates.INVERTED) ? "_extract" : "";

            return ConfiguredModel.builder()
                    .modelFile(models()
                            .withExistingParent("car_dock_rail" + inv, mcLoc("rail_flat"))
                            .texture("rail", getBlTx("car_dock_rail" + inv)))
                    .rotationY(state.getValue(AbstractDockingRail.RAIL_SHAPE).equals(RailShape.NORTH_SOUTH) ? 0 : 90)
                    .build();
        });

        getVariantBuilder(ModBlocks.LOCOMOTIVE_DOCK_RAIL.get()).forAllStates(state -> {
            String powered = state.getValue(DockingBlockStates.POWERED) ? "_powered" : "";

            return ConfiguredModel.builder()
                    .modelFile(models()
                            .withExistingParent("locomotive_dock_rail" + powered, mcLoc("rail_flat"))
                            .texture("rail", getBlTx("locomotive_dock_rail" + powered)))
                    .rotationY((int) state.getValue(DockingBlockStates.FACING).getOpposite().toYRot())
                    .build();
        });

        getVariantBuilder(ModBlocks.RAPID_HOPPER.get()).forAllStates(state -> ConfiguredModel.builder()
                .modelFile(getRapidHopperModel(state)
                )
                .rotationY((int) state.getValue(HopperBlock.FACING).getOpposite().toYRot())
                .build()
        );

    }

    @Override
    public void generateItemModels(ItemModelGenerators itemModelGenerator) {
        //ModelFile itemGenerated = getExistingFile(mcLoc("item/generated"));
        withExistingParent("tug_dock", modLoc("block/tug_dock"));
        withExistingParent("barge_dock", modLoc("block/barge_dock"));
        withExistingParent("guide_rail_corner", modLoc("block/guide_rail_corner"));
        withExistingParent("guide_rail_tug", modLoc("block/guide_rail_tug"));
        withExistingParent("fluid_hopper", modLoc("block/fluid_hopper"));
        withExistingParent("vessel_detector", modLoc("block/vessel_detector"));
        withExistingParent("vessel_charger", modLoc("block/vessel_charger"));

        builder(itemGenerated, "barge");
        builder(itemGenerated, "chunk_loader_barge");
        builder(itemGenerated, "fishing_barge");
        builder(itemGenerated, "fluid_barge");
        builder(itemGenerated, "seater_barge");
        builder(itemGenerated, "tug");
        builder(itemGenerated, "energy_tug");
        builder(itemGenerated, "steam_locomotive");
        builder(itemGenerated, "energy_locomotive");
        builder(itemGenerated, "chest_car");
        builder(itemGenerated, "chunk_loader_car");
        builder(itemGenerated, "fluid_car");
        builder(itemGenerated, "seater_car");
        builder(itemGenerated, "book");
        builder(itemGenerated, "tug_route")
                .override()
                .model(builder(itemGenerated, "tug_route_empty"))
                .predicate(new ResourceLocation(ShippingMod.MOD_ID, "routestate"), 1f).end();

        builder(itemGenerated, "spring")
                .override()
                .model(builder(itemGenerated, "spring_dominant_selected"))
                .predicate(new ResourceLocation(ShippingMod.MOD_ID, "springstate"), 1f).end();

        builder(itemGenerated, "conductors_wrench");
        builder(itemGenerated, "creative_capacitor");
        builder(itemGenerated, "rapid_hopper");
        builder(itemGenerated, "switch_rail");
        builder(itemGenerated, "automatic_switch_rail");
        builder(itemGenerated, "tee_junction_rail");
        builder(itemGenerated, "automatic_tee_junction_rail");
        builder(itemGenerated, "junction_rail");
        builder(itemGenerated, "car_dock_rail");
        builder(itemGenerated, "locomotive_dock_rail");

        builder(itemGenerated, "receiver_component");
        builder(itemGenerated, "transmitter_component");

        builder(itemGenerated, "locomotive_route")
                .override()
                .model(builder(itemGenerated, "locomotive_route_empty"))
                .predicate(new ResourceLocation(ShippingMod.MOD_ID, "locoroutestate"), 1f).end();
    }

    public static ResourceLocation getBlTx(String name){
        return new ResourceLocation(ShippingMod.MOD_ID, String.format("block/%s", name));
    }

    private ModelFile getTugDockModel(BlockState state){
        String inv = state.getValue(DockingBlockStates.INVERTED) ? "_inv" : "";
        String powered = state.getValue(DockingBlockStates.POWERED) ? "_powered" : "";
        return  models().orientable("tug_dock" + inv + powered,
                getBlTx("tug_dock"),
                getBlTx("tug_dock_front" + powered),
                getBlTx("tug_dock_top" + inv));
    }

    private ModelFile getCornerGuideRailModel(BlockState state){
        String inv = state.getValue(CornerGuideRailBlock.INVERTED) ? "_inv" : "";
        return  models().orientable("guide_rail_corner" + inv,
                getBlTx("guide_rail_side"),
                getBlTx("guide_rail_front" + inv),
                getBlTx("guide_rail_top" + inv));
    }

    private ModelFile getTugGuideRailModel(BlockState state){
        return  models().orientable("guide_rail_tug",
                getBlTx("guide_rail_side"),
                getBlTx("guide_rail_side"),
                getBlTx("guide_rail_front"));
    }

    private ModelFile getVesselDetectorModel(BlockState state){
        String powered = state.getValue(VesselDetectorBlock.POWERED) ? "_powered" : "";

        return models().withExistingParent("vessel_detector" + powered, modLoc("orientable_with_back"))
                .texture("side", getBlTx("vessel_detector_side"))
                .texture("front", getBlTx("vessel_detector_front"))
                .texture("back", getBlTx("vessel_detector_back" + powered));
    }

    private ModelFile getBargeDockModel(BlockState state){
        String inv = state.getValue(DockingBlockStates.INVERTED) ? "_extract" : "";
        return  models().orientable("barge_dock" + inv,
                getBlTx("barge_dock"),
                getBlTx("barge_dock_front" + inv),
                getBlTx("barge_dock_top"));
    }

    private int xRotFromDir(Direction direction){
        switch (direction) {
            case DOWN:
                return 270;
            case UP:
                return 90;
            default:
                return 0;
        }
    }

    private BlockModelBuilder getRapidHopperModel(BlockState state) {
        var side = state.getValue(HopperBlock.FACING).equals(Direction.DOWN) ? "" : "_side";
        return models()
                .withExistingParent("rapid_hopper" + side, mcLoc("hopper" + side))
                .texture("particle", getBlTx("rapid_hopper_outside"))
                .texture("top", getBlTx("rapid_hopper_top"))
                .texture("side", getBlTx("rapid_hopper_outside"))
                .texture("inside", getBlTx("rapid_hopper_inside"));
    }
}
