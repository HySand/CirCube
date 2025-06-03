package me.zephyr.circube.content.teleport.stabilizer.packets;

import me.zephyr.circube.content.teleport.beacon.PositionControl;
import me.zephyr.circube.content.teleport.stabilizer.StabilizerEntry;
import me.zephyr.circube.content.teleport.stabilizer.StabilizerScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class StabilizerDataPacket {
    private final List<StabilizerEntry> entries;

    public StabilizerDataPacket(List<StabilizerEntry> entries) {
        this.entries = entries;
    }

    public static void encode(StabilizerDataPacket packet, FriendlyByteBuf buffer) {
        buffer.writeInt(packet.entries.size());
        for (StabilizerEntry entry : packet.entries) {
            buffer.writeUtf(entry.getBeaconId());
            buffer.writeUtf(entry.getName());
            buffer.writeUtf(entry.getIconName());
            buffer.writeUtf(entry.getLevelName());
            buffer.writeBlockPos(entry.getLocation());
            buffer.writeUtf(entry.getOwner());
            buffer.writeBoolean(entry.isActive());
            buffer.writeEnum(entry.getPositionControl());
        }
    }

    public static StabilizerDataPacket decode(FriendlyByteBuf buffer) {
        int size = buffer.readInt();
        List<StabilizerEntry> entries = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            String id = buffer.readUtf();
            String name = buffer.readUtf();
            String icon = buffer.readUtf();
            String levelName = buffer.readUtf();
            BlockPos pos = buffer.readBlockPos();
            String owner = buffer.readUtf();
            boolean active = buffer.readBoolean();
            PositionControl positionMode = buffer.readEnum(PositionControl.class);
            entries.add(new StabilizerEntry(id, name, levelName, pos, icon, owner, active, positionMode));
        }
        return new StabilizerDataPacket(entries);
    }

    public static void handle(StabilizerDataPacket packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            Minecraft client = Minecraft.getInstance();
            if (client.screen instanceof StabilizerScreen screen) {
                screen.updateTeleportEntries(packet.getEntries());
            }
        });
        context.get().setPacketHandled(true);
    }

    public List<StabilizerEntry> getEntries() {
        return entries;
    }
}