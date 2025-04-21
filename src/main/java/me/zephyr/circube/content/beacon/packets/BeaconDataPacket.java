package me.zephyr.circube.content.beacon.packets;

import me.zephyr.circube.util.DataManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class BeaconDataPacket {
    List<String> beacons;

    public BeaconDataPacket(List<String> data) {
        this.beacons = data;
    }

    public static void encode(BeaconDataPacket packet, FriendlyByteBuf buffer) {
        buffer.writeVarInt(packet.beacons.size());
        for (String s : packet.beacons) {
            buffer.writeUtf(s);
        }
    }

    public static BeaconDataPacket decode(FriendlyByteBuf buffer) {
        int size = buffer.readVarInt();
        List<String> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(buffer.readUtf());
        }
        return new BeaconDataPacket(list);
    }

    public static void handle(BeaconDataPacket packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            if (context.get().getDirection().getReceptionSide().isClient()) {
                DataManager.clientBeaconList = packet.beacons;
            }
        });
        context.get().setPacketHandled(true);
    }
}
