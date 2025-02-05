package me.zephyr.circube.util;

import me.zephyr.circube.content.beacon.MechanicalBeaconBlock;
import me.zephyr.circube.content.beacon.MechanicalBeaconBlockEntity;
import me.zephyr.circube.content.stabilizer.StabilizerEntry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DataManager {
    public static BeaconData getOrCreateBeaconData(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(BeaconData::load, BeaconData::new, "circube_beacon");
    }

    public static void saveBeaconData(ServerLevel level, MechanicalBeaconBlockEntity blockEntity) {
        BeaconData data = getOrCreateBeaconData(level);

        CompoundTag nbt = new CompoundTag();
        nbt.putString("Name", blockEntity.getBeaconName());
        nbt.putString("Owner", blockEntity.getOwnerName());
        nbt.putString("World", level.dimension().location().toShortLanguageKey());
        nbt.putString("Icon", blockEntity.getIcon());
        nbt.putInt("X", blockEntity.getBlockPos().getX());
        nbt.putInt("Y", blockEntity.getBlockPos().getY());
        nbt.putInt("Z", blockEntity.getBlockPos().getZ());
        nbt.putUUID("OwnerUUID", blockEntity.getOwner());

        data.setData(blockEntity.getBeaconId(), nbt);
    }

    public static void removeBeaconData(ServerLevel level, String id) {
        BeaconData storage = getOrCreateBeaconData(level);
        storage.removeData(id);
    }

    public static CompoundTag loadBeaconData(ServerPlayer serverPlayer, String id) {
        BeaconData storage = getOrCreateBeaconData(serverPlayer.serverLevel());
        if (storage.getData(id).isEmpty()) {
            removeBeacon(serverPlayer, id);
            return null;
        }
        return storage.getData(id);
    }

    public static List<StabilizerEntry> loadBeaconEntries(ServerPlayer serverPlayer, List<String> beaconIds) {
        List<StabilizerEntry> entries = new ArrayList<>();
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
                BlockState state = serverPlayer.serverLevel().getBlockState(pos);
                boolean active = state.hasProperty(MechanicalBeaconBlock.ACTIVE) && state.getValue(MechanicalBeaconBlock.ACTIVE);
                entries.add(new StabilizerEntry(id, name, pos, icon, owner, active));
            }
        }
        return entries;
    }

    public static void savePlayerData(ServerPlayer player, String id) {
        CompoundTag data = player.getPersistentData();

        ListTag beacons;
        if (data.contains("Beacons", 9)) {
            beacons = data.getList("Beacons", Tag.TAG_STRING);
        } else {
            beacons = new ListTag();
        }
        if (!beacons.contains(StringTag.valueOf(id))) {
            beacons.add(StringTag.valueOf(id));
        }

        data.put("Beacons", beacons);
    }

    public static List<String> getBeaconIds(ServerPlayer player) {
        CompoundTag data = player.getPersistentData();
        return data.getList("Beacons", Tag.TAG_STRING)
                .stream()
                .map(tag -> tag.getAsString())
                .collect(Collectors.toList());
    }

    public static void removeBeacon(ServerPlayer player, String beaconId) {
        CompoundTag playerData = player.getPersistentData();
        ListTag beaconList = playerData.getList("Beacons", Tag.TAG_STRING);
        beaconList.removeIf(tag -> tag.getAsString().equals(beaconId));
    }

    public static void updateBeaconOrder(ServerPlayer player, List<String> beaconIds) {
        CompoundTag playerData = player.getPersistentData();
        ListTag beaconList = new ListTag();
        beaconIds.forEach(beaconId -> beaconList.add(StringTag.valueOf(beaconId)));
        playerData.put("Beacons", beaconList);
    }

    public static void updateBeaconName(ServerLevel level, String beaconId, String newName) {
        BeaconData storage = getOrCreateBeaconData(level);
        CompoundTag beaconData = storage.getData(beaconId);
        if (beaconData != null) {
            beaconData.putString("Name", newName);
            storage.setData(beaconId, beaconData);
        }
    }

    public static void updateBeaconIcon(ServerLevel level, String beaconId, String newIcon) {
        BeaconData storage = getOrCreateBeaconData(level);
        CompoundTag beaconData = storage.getData(beaconId);
        if (beaconData != null) {
            beaconData.putString("Icon", newIcon);
            storage.setData(beaconId, beaconData);
        }
    }
}
