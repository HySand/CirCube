package me.zephyr.circube.util;

import me.zephyr.circube.content.beacon.MechanicalBeaconBlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

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
        nbt.putString("X", String.valueOf(blockEntity.getBlockPos().getX()));
        nbt.putString("Y", String.valueOf(blockEntity.getBlockPos().getY()));
        nbt.putString("Z", String.valueOf(blockEntity.getBlockPos().getZ()));
        nbt.putUUID("OwnerUUID", blockEntity.getOwner());

        data.setData(blockEntity.getBeaconId(), nbt);
    }

    public static void removeBeaconData(ServerLevel level, String id) {
        BeaconData storage = getOrCreateBeaconData(level);
        storage.removeData(id);
    }

    public static CompoundTag loadBeaconData(ServerLevel level, String id) {
        BeaconData storage = getOrCreateBeaconData(level);
        return storage.getData(id);
    }


    public static void savePlayerData(ServerPlayer player, String id) {
        CompoundTag data = player.getPersistentData();

        ListTag beacons;
        if (data.contains("Beacons", 9)) {
            beacons = data.getList("Beacons", 8);
        } else {
            beacons = new ListTag();
        }
        if (!beacons.contains(StringTag.valueOf(id))) {
            beacons.add(StringTag.valueOf(id));
        }

        data.put("Beacons", beacons);
    }
}
