package me.zephyr.circube;

import me.zephyr.circube.content.beacon.packets.BeaconIconUpdatePacket;
import me.zephyr.circube.content.beacon.packets.BeaconNameUpdatePacket;
import me.zephyr.circube.content.stabilizer.packets.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import static me.zephyr.circube.CirCube.MODID;

public class CirCubePackets {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(MODID, "main"),
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
    }
}
