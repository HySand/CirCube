// TeleportPacket.java
package me.zephyr.circube.content.vlobby.packets;

import io.netty.buffer.Unpooled;
import me.zephyr.circube.CirCube;
import me.zephyr.circube.CirCubeMenuTypes;
import me.zephyr.circube.content.vlobby.LobbyMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class OpenLobbyPacket {
    public OpenLobbyPacket() {
    }

    public static void encode(OpenLobbyPacket packet, FriendlyByteBuf buffer) {
    }

    public static OpenLobbyPacket decode(FriendlyByteBuf buffer) {
        return new OpenLobbyPacket();
    }

    public static void handle(OpenLobbyPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player != null) {
                player.openMenu(new SimpleMenuProvider(
                        (containerId, playerInventory, playerEntity) ->
                                new LobbyMenu(CirCubeMenuTypes.LOBBY_MENU.get(), containerId, playerInventory, new FriendlyByteBuf(Unpooled.buffer())),
                        Component.empty()
                ));
            }
        });
        ctx.get().setPacketHandled(true);
    }
}