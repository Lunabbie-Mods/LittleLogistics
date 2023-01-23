package dev.murad.shipping.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import dev.murad.shipping.ShippingConfig;
import dev.murad.shipping.ShippingMod;
import dev.murad.shipping.event.ModRenderType;
import dev.murad.shipping.item.LocoRouteItem;
import dev.murad.shipping.item.TugRouteItem;
import dev.murad.shipping.network.client.EntityPosition;
import dev.murad.shipping.network.client.VehicleTrackerPacketHandler;
import dev.murad.shipping.setup.EntityItemMap;
import dev.murad.shipping.setup.ModItems;
import dev.murad.shipping.util.LocoRoute;
import dev.murad.shipping.util.LocoRouteNode;
import dev.murad.shipping.util.RailHelper;
import dev.murad.shipping.util.TugRouteNode;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BeaconRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.stream.Collectors;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {
    private static Vec3 computeFixedDistance(Vec3 target, Vec3 position){
        target = target.add(0, 2, 0);
        Vec3 delta = position.vectorTo(target);
        if(delta.length() < 5)
            return target;
        else return position.add(delta.normalize().scale(5));
    }

    private static final ResourceLocation BEAM_LOCATION = new ResourceLocation(ShippingMod.MOD_ID, "textures/entity/beacon_beam.png");

    @Inject(method = "renderChunkLayer", at = @At("TAIL"))
    private void littlelogistics$onRenderWorldLast(RenderType renderType, PoseStack poseStack, double d, double e, double f, Matrix4f matrix4f, CallbackInfo ci) {
        if (renderType.equals(RenderType.tripwire())) {
            Player player = Minecraft.getInstance().player;
            ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);

            float partialTick = Minecraft.getInstance().getDeltaFrameTime();

            if (stack.getItem().equals(ModItems.LOCO_ROUTE.get())) {
                MultiBufferSource.BufferSource buffer = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());
                Vec3 cameraOff = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();

                LocoRoute route = LocoRouteItem.getRoute(stack);
                var list = route.stream().map(LocoRouteNode::toBlockPos).collect(Collectors.toList());
                for (BlockPos block : list) {
                    poseStack.pushPose();
                    poseStack.translate(block.getX() - cameraOff.x, 1 - cameraOff.y, block.getZ() - cameraOff.z);
                    BeaconRenderer.renderBeaconBeam(poseStack, buffer, BEAM_LOCATION, partialTick,
                            1F, player.level.getGameTime(), player.level.getMinBuildHeight() + 1, 1024,
                            DyeColor.RED.getTextureDiffuseColors(), 0F, 0.25F);

                    poseStack.popPose();
                }

                for (BlockPos block : list) {
                    poseStack.pushPose();
                    // handling for removed blocks and blocks out of distance
                    var shape = RailHelper.getRail(block, player.level)
                            .map(pos -> RailHelper.getShape(pos, player.level))
                            .orElse(RailShape.EAST_WEST);
                    double baseY = (shape.getName().contains("ascending") ? 0.1 : 0);
                    double baseX = 0;
                    double baseZ = 0;
                    Runnable mulPose = () -> {
                    };
                    switch (shape) {
                        case ASCENDING_EAST -> {
                            baseX = 0.2;
                            mulPose = () -> poseStack.mulPose(Vector3f.ZP.rotationDegrees(45));
                        }
                        case ASCENDING_WEST -> {
                            baseX = 0.1;
                            baseY += 0.7;
                            mulPose = (() -> poseStack.mulPose(Vector3f.ZP.rotationDegrees(-45)));

                        }
                        case ASCENDING_NORTH -> {
                            baseZ = 0.1;
                            baseY += 0.7;
                            mulPose = () -> poseStack.mulPose(Vector3f.XP.rotationDegrees(45));
                        }
                        case ASCENDING_SOUTH -> {
                            baseZ = 0.2;
                            mulPose = () -> poseStack.mulPose(Vector3f.XP.rotationDegrees(-45));
                        }
                    }

                    poseStack.translate(block.getX() + baseX - cameraOff.x, block.getY() + baseY - cameraOff.y, block.getZ() + baseZ - cameraOff.z);
                    AABB a = new AABB(0, 0, 0, 1, 0.2, 1).deflate(0.2, 0, 0.2);
                    mulPose.run();
                    LevelRenderer.renderLineBox(poseStack, buffer.getBuffer(ModRenderType.LINES), a, 1.0f, 0.3f, 0.3f, 0.5f);
                    poseStack.popPose();

                }


                buffer.endBatch();
            }

            if (stack.getItem().equals(ModItems.TUG_ROUTE.get())) {
                if (ShippingConfig.Client.DISABLE_TUG_ROUTE_BEACONS.get()) {
                    return;
                }
                Vec3 vector3d = Minecraft.getInstance().getEntityRenderDispatcher().camera.getPosition();
                double d0 = vector3d.x();
                double d1 = vector3d.y();
                double d2 = vector3d.z();
                MultiBufferSource.BufferSource renderTypeBuffer = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());
                List<TugRouteNode> route = TugRouteItem.getRoute(stack);
                for (int i = 0, routeSize = route.size(); i < routeSize; i++) {
                    TugRouteNode node = route.get(i);

                    poseStack.pushPose();
                    poseStack.translate(node.getX() - d0, 1 - d1, node.getZ() - d2);

                    BeaconRenderer.renderBeaconBeam(poseStack, renderTypeBuffer, BEAM_LOCATION, partialTick,
                            1F, player.level.getGameTime(), player.level.getMinBuildHeight(), 1024,
                            DyeColor.RED.getTextureDiffuseColors(), 0.2F, 0.25F);
                    poseStack.popPose();
                    poseStack.pushPose();
                    poseStack.translate(node.getX() - d0, player.getY() + 2 - d1, node.getZ() - d2);
                    poseStack.scale(-0.025F, -0.025F, -0.025F);

                    poseStack.mulPose(Minecraft.getInstance().getEntityRenderDispatcher().cameraOrientation());

                    //Matrix4f matrix4f = poseStack.last().pose();

                    Font fontRenderer = Minecraft.getInstance().font;
                    String text = node.getDisplayName(i);
                    float width = (-fontRenderer.width(text) / (float) 2);
                    fontRenderer.drawInBatch(text, width, 0.0F, -1, true, matrix4f, renderTypeBuffer, true, 0, 15728880);
                    poseStack.popPose();

                }
                renderTypeBuffer.endBatch();
            }

            if (stack.getItem().equals(ModItems.CONDUCTORS_WRENCH.get()) && player.level.dimension().toString().equals(VehicleTrackerPacketHandler.toRenderDimension)) {
                MultiBufferSource.BufferSource renderTypeBuffer = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());
                var camera2 = Minecraft.getInstance().getEntityRenderDispatcher().camera;
                Vec3 cameraPosition = camera2.getPosition();
                double d0 = cameraPosition.x();
                double d1 = cameraPosition.y();
                double d2 = cameraPosition.z();

                for (EntityPosition position : VehicleTrackerPacketHandler.toRender) {
                    Entity entity = player.level.getEntity(position.id());
                    double x;
                    double y;
                    double z;
                    if (entity == null) {
                        //FIXME: partial ticks weren't working properly
                        x = (position.pos().x);
                        y = (position.pos().y);
                        z = (position.pos().z);

                    } else {
                        x = Mth.lerp(partialTick, entity.xOld, entity.getX());
                        y = Mth.lerp(partialTick, entity.yOld, entity.getY());
                        z = Mth.lerp(partialTick, entity.zOld, entity.getZ());
                    }

                    Vec3 posToRender = computeFixedDistance(new Vec3(x, y, z), cameraPosition);

                    poseStack.pushPose();
                    poseStack.translate(posToRender.x - d0, posToRender.y - d1, posToRender.z - d2);
                    // TODO get camera somehow
//                    poseStack.mulPose(Vector3f.YP.rotationDegrees(-camera.getYRot()));
//                    poseStack.mulPose(Vector3f.XP.rotationDegrees(camera.getXRot()));

                    Minecraft.getInstance().getItemRenderer().renderStatic(
                            new ItemStack(EntityItemMap.get(position.type())),
                            ItemTransforms.TransformType.GROUND,
                            150,
                            OverlayTexture.NO_OVERLAY,
                            poseStack,
                            renderTypeBuffer,
                            position.id()
                    );

                    poseStack.scale(-0.025F, -0.025F, -0.025F);
                    //Matrix4f matrix4f = poseStack.last().pose();
                    Font fontRenderer = Minecraft.getInstance().font;
                    String text = String.valueOf(Math.round(position.pos().distanceTo(player.position()) * 10) / 10d);
                    float width = (-fontRenderer.width(text) / (float) 2);
                    fontRenderer.drawInBatch(text, width, 0.0F, -1, true, matrix4f, renderTypeBuffer, true, 0, 15728880);
                    poseStack.popPose();


                }

                renderTypeBuffer.endBatch();
            }
        }
    }
}
