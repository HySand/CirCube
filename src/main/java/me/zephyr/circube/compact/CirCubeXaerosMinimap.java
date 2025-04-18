package me.zephyr.circube.compact;

import me.zephyr.circube.CirCubePackets;
import me.zephyr.circube.compact.packets.AddWaypointPacket;
import me.zephyr.circube.compact.packets.RenameWaypointPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.PacketDistributor;
import xaero.common.minimap.waypoints.Waypoint;

import java.util.ArrayList;

public class CirCubeXaerosMinimap {
    public static void addWaypoint(String name, BlockPos pos, ServerPlayer serverPlayer) {
        AddWaypointPacket beaconActivatedPacket = new AddWaypointPacket(name, pos);
        CirCubePackets.CHANNEL.send(PacketDistributor.PLAYER.with(() -> serverPlayer), beaconActivatedPacket);
    }

    public static void updateWaypoint(String name, BlockPos pos, ServerPlayer serverPlayer) {
        RenameWaypointPacket beaconRenamePacket = new RenameWaypointPacket(name, pos);
        CirCubePackets.CHANNEL.send(PacketDistributor.PLAYER.with(() -> serverPlayer), beaconRenamePacket);
    }

    public static Waypoint getWaypoint(ArrayList<Waypoint> waypoints, BlockPos beaconPos) {
        for (Waypoint waypoint : waypoints) {
            if (waypoint.getX() == beaconPos.getX() && waypoint.getY() == beaconPos.getY() + 2 && waypoint.getZ() == beaconPos.getZ()) {
                return waypoint;
            }
        }
        return null;
    }
}


