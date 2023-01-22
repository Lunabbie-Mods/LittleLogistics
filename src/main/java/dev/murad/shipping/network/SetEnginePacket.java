package dev.murad.shipping.network;

import lombok.RequiredArgsConstructor;
import me.pepperbell.simplenetworking.C2SPacket;
import me.pepperbell.simplenetworking.SimpleChannel;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

@RequiredArgsConstructor
public class SetEnginePacket implements C2SPacket {
    public final int locoId;
    public final boolean state;

    public SetEnginePacket(FriendlyByteBuf buffer) {
        this.locoId = buffer.readInt();
        this.state = buffer.readBoolean();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(locoId);
        buf.writeBoolean(state);
    }

    @Override
    public void handle(MinecraftServer minecraftServer, ServerPlayer serverPlayer, ServerGamePacketListenerImpl serverGamePacketListener, PacketSender packetSender, SimpleChannel simpleChannel) {

    }
}
