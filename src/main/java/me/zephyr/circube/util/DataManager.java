package me.zephyr.circube.util;

import me.zephyr.circube.CirCubePackets;
import me.zephyr.circube.content.beacon.MechanicalBeaconBlock;
import me.zephyr.circube.content.beacon.MechanicalBeaconBlockEntity;
import me.zephyr.circube.content.beacon.PositionControl;
import me.zephyr.circube.content.beacon.packets.BeaconDataPacket;
import me.zephyr.circube.content.stabilizer.StabilizerEntry;
import me.zephyr.circube.content.vlobby.RoomEntry;
import net.createmod.catnip.nbt.NBTHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.PacketDistributor;

import java.util.*;
import java.util.stream.Collectors;

public class DataManager {
    private static final Map<UUID, List<String>> beaconMap = new HashMap<>();
    private static final List<RoomEntry> gameList = new ArrayList<>();

    public static List<String> clientBeaconList = new ArrayList<>();

    public static BeaconData getOrCreateBeaconData(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(BeaconData::load, BeaconData::new, "circube_beacon");
    }

    public static void saveBeaconData(ServerLevel level, MechanicalBeaconBlockEntity blockEntity) {
        BeaconData data = getOrCreateBeaconData(level.getServer().overworld());

        CompoundTag nbt = new CompoundTag();
        nbt.putString("Name", blockEntity.getBeaconName());
        nbt.putString("Owner", blockEntity.getOwnerName());
        nbt.putString("World", level.dimension().location().toString());
        nbt.putString("Icon", blockEntity.getIcon());
        nbt.putInt("X", blockEntity.getBlockPos().getX());
        nbt.putInt("Y", blockEntity.getBlockPos().getY());
        nbt.putInt("Z", blockEntity.getBlockPos().getZ());
        nbt.putUUID("OwnerUUID", blockEntity.getOwner());
        NBTHelper.writeEnum(nbt, "PositionMode", blockEntity.getPositionMode());

        data.setData(blockEntity.getBeaconId(), nbt);
    }

    public static void removeBeaconData(ServerLevel level, String id) {
        BeaconData storage = getOrCreateBeaconData(level);
        storage.removeData(id);
    }

    public static CompoundTag loadBeaconData(ServerPlayer serverPlayer, String id) {
        BeaconData storage = getOrCreateBeaconData(serverPlayer.getServer().overworld());
        if (storage.getData(id).isEmpty()) {
            removeBeaconFromPlayer(serverPlayer, id);
            return null;
        }
        return storage.getData(id);
    }

    public static List<StabilizerEntry> loadBeaconEntries(ServerPlayer serverPlayer) {
        List<StabilizerEntry> entries = new ArrayList<>();
        List<String> beaconIds = getBeaconIdsInMemory(serverPlayer);
        for (String id : beaconIds) {
            CompoundTag beaconData = loadBeaconData(serverPlayer, id);
            if (beaconData != null) {
                String name = beaconData.getString("Name");
                BlockPos pos = new BlockPos(
                        beaconData.getInt("X"),
                        beaconData.getInt("Y"),
                        beaconData.getInt("Z")
                );
                String icon = beaconData.getString("Icon");
                String owner = beaconData.getString("Owner");
                String levelName = beaconData.getString("World");
                ServerLevel level = serverPlayer.getServer().getLevel((ResourceKey.create(Registries.DIMENSION, ResourceLocation.parse(levelName))));
                BlockState state = level.getBlockState(pos);
                boolean active = state.hasProperty(MechanicalBeaconBlock.ACTIVE) && state.getValue(MechanicalBeaconBlock.ACTIVE);
                PositionControl positionMode = PositionControl.valueOf(beaconData.getString("PositionMode"));
                entries.add(new StabilizerEntry(id, name, levelName, pos, icon, owner, active, positionMode));
            } else {
                removeBeaconFromPlayer(serverPlayer, id);
            }
        }
        return entries;
    }

    public static boolean addBeaconToPlayerData(ServerPlayer player, String id) {
        CompoundTag data = player.getPersistentData();

        ListTag beacons;
        if (data.contains("Beacons", 9)) {
            beacons = data.getList("Beacons", Tag.TAG_STRING);
        } else {
            beacons = new ListTag();
        }
        if (!beacons.contains(StringTag.valueOf(id))) {
            beacons.add(StringTag.valueOf(id));
            data.put("Beacons", beacons);
            return true;
        }
        return false;
    }

    public static List<String> getBeaconIds(ServerPlayer player) {
        CompoundTag data = player.getPersistentData();
        return data.getList("Beacons", Tag.TAG_STRING)
                .stream()
                .map(Tag::getAsString)
                .collect(Collectors.toList());
    }

    public static void removeBeaconFromPlayer(ServerPlayer player, String beaconId) {
        CompoundTag playerData = player.getPersistentData();
        ListTag beaconList = playerData.getList("Beacons", Tag.TAG_STRING);
        beaconList.removeIf(tag -> tag.getAsString().equals(beaconId));
        removeBeaconFromMemory(player, beaconId);
    }

    public static void updateBeaconOrder(ServerPlayer player, List<String> beaconIds) {
        CompoundTag playerData = player.getPersistentData();
        ListTag beaconList = new ListTag();
        beaconIds.forEach(beaconId -> beaconList.add(StringTag.valueOf(beaconId)));
        playerData.put("Beacons", beaconList);

        beaconMap.remove(player.getUUID());
        beaconMap.put(player.getUUID(), beaconIds);
    }

    public static void updateBeaconName(ServerLevel level, String beaconId, String newName) {
        BeaconData storage = getOrCreateBeaconData(level.getServer().overworld());
        CompoundTag beaconData = storage.getData(beaconId);
        if (beaconData != null) {
            beaconData.putString("Name", newName);
            storage.setData(beaconId, beaconData);
        }
    }

    public static void updateBeaconIcon(ServerLevel level, String beaconId, String newIcon) {
        BeaconData storage = getOrCreateBeaconData(level.getServer().overworld());
        CompoundTag beaconData = storage.getData(beaconId);
        if (beaconData != null) {
            beaconData.putString("Icon", newIcon);
            storage.setData(beaconId, beaconData);
        }
    }

    public static void updateBeaconPositionMode(ServerLevel level, String beaconId, PositionControl positionMode) {
        BeaconData storage = getOrCreateBeaconData(level.getServer().overworld());
        CompoundTag beaconData = storage.getData(beaconId);
        if (beaconData != null) {
            beaconData.putString("PositionMode", String.valueOf(positionMode));
            storage.setData(beaconId, beaconData);
        }
    }

    public static void addBeaconsToMemory(UUID uuid, List<String> beaconIds) {
        beaconMap.putIfAbsent(uuid, beaconIds);
    }

    public static void addBeaconToMemory(ServerPlayer player, String beaconId) {
        UUID uuid = player.getUUID();
        List<String> beaconIds = beaconMap.get(uuid);
        beaconIds.add(beaconId);
        beaconMap.putIfAbsent(uuid, beaconIds);
        List<String> beaconList = getBeaconIdsInMemory(player);
        BeaconDataPacket beaconSyncPacket = new BeaconDataPacket(beaconList);
        CirCubePackets.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), beaconSyncPacket);
    }

    public static void loadBeaconsToMemory(ServerPlayer player) {
        List<String> beaconIds = getBeaconIds(player);
        beaconMap.putIfAbsent(player.getUUID(), beaconIds);
    }

    public static List<String> getBeaconIdsInMemory(ServerPlayer player) {
        return beaconMap.get(player.getUUID());
    }

    private static void removeBeaconFromMemory(ServerPlayer player, String beaconId) {
        List<String> beaconIds = beaconMap.get(player.getUUID());
        beaconIds.remove(beaconId);
        beaconMap.put(player.getUUID(), beaconIds);
    }

    public static List<RoomEntry> getRoomEntries() {
        return gameList;
    }

    public static void addGameToList(RoomEntry entry) {
        gameList.add(entry);
    }

    public static void setGameStatus(String gameName, boolean start) {
        for (RoomEntry entry : gameList) {
            if (entry.getName().equals(gameName)) {
                if (start) {
                    entry.startGame();
                } else {
                    entry.stopGame();
                }

            }
        }
    }

    public static void addPlayerToGame(ServerPlayer player, String name) {
        for (RoomEntry entry : gameList) {
            if (entry.getName().equals(name) && !entry.isStarted() && !entry.isStarted()) {
                removePlayerFromOtherEntries(player.getUUID());
                entry.addPlayer(player.getUUID());
                if (entry.getPlayers().size() == entry.getMaxPlayers()) {
                    setGameStatus(name, true);
                }
            }
        }
    }

    public static void removePlayerFromGame(ServerPlayer player, String name) {
        for (RoomEntry entry : gameList) {
            if (entry.getName().equals(name) && !entry.isStarted()) {
                entry.removePlayer(player.getUUID());
            }
        }
    }

    private static void removePlayerFromOtherEntries(UUID uuid) {
        for (RoomEntry entry : gameList) {
            entry.getPlayers().remove(uuid);
        }
    }
}
