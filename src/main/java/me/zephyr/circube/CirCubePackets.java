package me.zephyr.circube;

import me.zephyr.circube.content.beacon.packets.*;
import me.zephyr.circube.content.stabilizer.packets.*;
import me.zephyr.circube.content.vlobby.packets.OpenLobbyPacket;
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
                BeaconRequestPacket.class,
                BeaconRequestPacket::encode,
                BeaconRequestPacket::decode,
                BeaconRequestPacket::handle
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
                OpenLobbyPacket.class,
                OpenLobbyPacket::encode,
                OpenLobbyPacket::decode,
                OpenLobbyPacket::handle
        );
        CHANNEL.registerMessage(packetId++,
                BeaconLoadPacket.class,
                BeaconLoadPacket::encode,
                BeaconLoadPacket::decode,
                BeaconLoadPacket::handle
        );
        CHANNEL.registerMessage(packetId++,
                BeaconSyncPacket.class,
                BeaconSyncPacket::encode,
                BeaconSyncPacket::decode,
                BeaconSyncPacket::handle
        );
    }
}
