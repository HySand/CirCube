package me.zephyr.circube.content.vlobby.packets;

import me.zephyr.circube.content.vlobby.Dungeon;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.io.IOException;
import java.util.function.Supplier;

import static me.zephyr.circube.util.DataManager.getDungeonList;

public class JoinRoomPacket {
    int entryId;

    public JoinRoomPacket(int id) {
        entryId = id;
    }

    public static void encode(JoinRoomPacket packet, FriendlyByteBuf buffer) {
        buffer.writeInt(packet.entryId);
    }

    public static JoinRoomPacket decode(FriendlyByteBuf buffer) {
        return new JoinRoomPacket(buffer.readInt());
    }

    public static void handle(JoinRoomPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player != null) {
                Dungeon dungeon = getDungeonList().get(packet.entryId);
                dungeon.addPlayerToGame(player);
                if (dungeon.isGameReadyToStart()) {
                    try {
                        dungeon.setGameStatus(true, ctx.get().getSender().getServer());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
