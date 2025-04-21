package me.zephyr.circube.compact.packets;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import xaero.common.XaeroMinimapSession;
import xaero.common.core.IXaeroMinimapClientPlayNetHandler;
import xaero.common.minimap.waypoints.Waypoint;
import xaero.common.minimap.waypoints.WaypointsManager;
import xaero.common.settings.ModSettings;
import xaero.minimap.XaeroMinimap;

import java.io.IOException;
import java.util.function.Supplier;

import static me.zephyr.circube.compact.CirCubeXaerosMinimap.getWaypoint;

public class RenameWaypointPacket {
    private final String name;
    private final BlockPos pos;

    public RenameWaypointPacket(String name, BlockPos pos) {
        this.name = name;
        this.pos = pos;
    }

    public static void encode(RenameWaypointPacket packet, FriendlyByteBuf buffer) {
        buffer.writeUtf(packet.name);
        buffer.writeBlockPos(packet.pos);
    }

    public static RenameWaypointPacket decode(FriendlyByteBuf buffer) {
        String name = buffer.readUtf();
        BlockPos pos = buffer.readBlockPos();

        return new RenameWaypointPacket(name, pos);
    }

    public static void handle(RenameWaypointPacket packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> handlePacket(packet, context));
        });
        context.get().setPacketHandled(true);
    }

    private static void handlePacket(RenameWaypointPacket packet, Supplier<NetworkEvent.Context> ctx) {
        String name = packet.name;
        BlockPos pos = packet.pos;

        IXaeroMinimapClientPlayNetHandler clientLevel = (IXaeroMinimapClientPlayNetHandler) (Minecraft.getInstance().player.connection);
        XaeroMinimapSession session = clientLevel.getXaero_minimapSession();
        WaypointsManager waypointsManager = session.getWaypointsManager();
        Waypoint waypoint = getWaypoint(waypointsManager.getWaypoints().getList(), pos);
        if (waypoint == null) {
            Waypoint instant = new Waypoint(pos.getX(), pos.getY() + 2, pos.getZ(), name, name.substring(0, 1), (int) (Math.random() * ModSettings.ENCHANT_COLORS.length), 0, false);
            waypointsManager.getWaypoints().getList().add(instant);
        } else {
            waypoint.setName(name);
            waypoint.setSymbol(name.substring(0, 1));
            waypoint.setX(pos.getX());
            waypoint.setY(pos.getY() + 2);
            waypoint.setZ(pos.getZ());
        }

        try {
            XaeroMinimap.instance.getSettings().saveWaypoints(waypointsManager.getCurrentWorld());
        } catch (IOException error) {
            error.printStackTrace();
        }
    }
}
