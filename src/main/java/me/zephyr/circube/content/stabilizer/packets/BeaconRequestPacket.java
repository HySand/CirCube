package me.zephyr.circube.content.stabilizer.packets;

import me.zephyr.circube.CirCubePackets;
import me.zephyr.circube.content.stabilizer.StabilizerEntry;
import me.zephyr.circube.util.DataManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

public class BeaconRequestPacket {
    private final UUID playerUUID;

    public BeaconRequestPacket(UUID playerUUID) {
        this.playerUUID = playerUUID;
    }

    public static void encode(BeaconRequestPacket packet, FriendlyByteBuf buffer) {
        buffer.writeUUID(packet.playerUUID);
    }

    public static BeaconRequestPacket decode(FriendlyByteBuf buffer) {
        return new BeaconRequestPacket(buffer.readUUID());
    }

    public static void handle(BeaconRequestPacket packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            ServerPlayer serverPlayer = context.get().getSender();
            if (serverPlayer != null) {
                // 获取玩家的 Beacon ID 列表
                List<String> beaconIds = DataManager.getBeaconIds(serverPlayer);
                // 根据 Beacon ID 加载 Beacon 信息
                List<StabilizerEntry> entries = DataManager.loadBeaconEntries(serverPlayer, beaconIds);
                // 发送数据包到客户端
                CirCubePackets.CHANNEL.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new StabilizerDataPacket(entries));
            }
        });
        context.get().setPacketHandled(true);
    }
}