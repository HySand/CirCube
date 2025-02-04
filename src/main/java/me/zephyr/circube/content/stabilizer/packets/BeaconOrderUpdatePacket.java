package me.zephyr.circube.content.stabilizer.packets;

import me.zephyr.circube.util.DataManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class BeaconOrderUpdatePacket {
    private final List<String> beaconIds;

    public BeaconOrderUpdatePacket(List<String> beaconIds) {
        this.beaconIds = beaconIds;
    }

    public static void encode(BeaconOrderUpdatePacket packet, FriendlyByteBuf buffer) {
        buffer.writeCollection(packet.beaconIds, FriendlyByteBuf::writeUtf);
    }

    public static BeaconOrderUpdatePacket decode(FriendlyByteBuf buffer) {
        List<String> beaconIds = buffer.readCollection(ArrayList::new, FriendlyByteBuf::readUtf);
        return new BeaconOrderUpdatePacket(beaconIds);
    }

    public static void handle(BeaconOrderUpdatePacket packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            ServerPlayer serverPlayer = context.get().getSender();
            if (serverPlayer != null) {
                // 更新服务端玩家数据中的 Beacon ID 顺序
                DataManager.updateBeaconOrder(serverPlayer, packet.beaconIds);
            }
        });
        context.get().setPacketHandled(true);
    }
}
