package me.zephyr.circube;

import me.zephyr.circube.compact.packets.AddWaypointPacket;
import me.zephyr.circube.compact.packets.RenameWaypointPacket;
import me.zephyr.circube.content.teleport.beacon.packets.*;
import me.zephyr.circube.content.teleport.stabilizer.packets.*;
import me.zephyr.circube.content.vlobby.packets.JoinRoomPacket;
import me.zephyr.circube.content.vlobby.packets.LeaveRoomPacket;
import me.zephyr.circube.content.vlobby.packets.RoomDataPacket;
import me.zephyr.circube.content.vlobby.packets.RoomEntriesRequestPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import static me.zephyr.circube.CirCube.MOD_ID;

public class CirCubePackets {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            ResourceLocation.tryBuild(MOD_ID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void register() {
        int packetId = 0;
        CHANNEL.registerMessage(packetId++,
                TeleportPacket.class,
                TeleportPacket::encode,
                TeleportPacket::decode,
                TeleportPacket::handle
        );
        CHANNEL.registerMessage(packetId++,
                StabilizerEntriesRequestPacket.class,
                StabilizerEntriesRequestPacket::encode,
                StabilizerEntriesRequestPacket::decode,
                StabilizerEntriesRequestPacket::handle
        );
        CHANNEL.registerMessage(packetId++,
                StabilizerDataPacket.class,
                StabilizerDataPacket::encode,
                StabilizerDataPacket::decode,
                StabilizerDataPacket::handle
        );
        CHANNEL.registerMessage(packetId++,
                BeaconDeletePacket.class,
                BeaconDeletePacket::encode,
                BeaconDeletePacket::decode,
                BeaconDeletePacket::handle
        );
        CHANNEL.registerMessage(packetId++,
                BeaconOrderUpdatePacket.class,
                BeaconOrderUpdatePacket::encode,
                BeaconOrderUpdatePacket::decode,
                BeaconOrderUpdatePacket::handle
        );
        CHANNEL.registerMessage(packetId++,
                BeaconNameUpdatePacket.class,
                BeaconNameUpdatePacket::encode,
                BeaconNameUpdatePacket::decode,
                BeaconNameUpdatePacket::handle
        );
        CHANNEL.registerMessage(packetId++,
                BeaconIconUpdatePacket.class,
                BeaconIconUpdatePacket::encode,
                BeaconIconUpdatePacket::decode,
                BeaconIconUpdatePacket::handle
        );
        CHANNEL.registerMessage(packetId++,
                BeaconPositionUpdatePacket.class,
                BeaconPositionUpdatePacket::encode,
                BeaconPositionUpdatePacket::decode,
                BeaconPositionUpdatePacket::handle
        );
        CHANNEL.registerMessage(packetId++,
                BeaconRequestPacket.class,
                BeaconRequestPacket::encode,
                BeaconRequestPacket::decode,
                BeaconRequestPacket::handle
        );
        CHANNEL.registerMessage(packetId++,
                BeaconDataPacket.class,
                BeaconDataPacket::encode,
                BeaconDataPacket::decode,
                BeaconDataPacket::handle
        );
        CHANNEL.registerMessage(packetId++,
                AddWaypointPacket.class,
                AddWaypointPacket::encode,
                AddWaypointPacket::decode,
                AddWaypointPacket::handle
        );
        CHANNEL.registerMessage(packetId++,
                RenameWaypointPacket.class,
                RenameWaypointPacket::encode,
                RenameWaypointPacket::decode,
                RenameWaypointPacket::handle
        );
        CHANNEL.registerMessage(packetId++,
                RoomEntriesRequestPacket.class,
                RoomEntriesRequestPacket::encode,
                RoomEntriesRequestPacket::decode,
                RoomEntriesRequestPacket::handle
        );
        CHANNEL.registerMessage(packetId++,
                RoomDataPacket.class,
                RoomDataPacket::encode,
                RoomDataPacket::decode,
                RoomDataPacket::handle
        );
        CHANNEL.registerMessage(packetId++,
                JoinRoomPacket.class,
                JoinRoomPacket::encode,
                JoinRoomPacket::decode,
                JoinRoomPacket::handle
        );
        CHANNEL.registerMessage(packetId++,
                LeaveRoomPacket.class,
                LeaveRoomPacket::encode,
                LeaveRoomPacket::decode,
                LeaveRoomPacket::handle
        );
    }
}
