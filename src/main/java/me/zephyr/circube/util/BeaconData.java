package me.zephyr.circube.util;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class BeaconData extends SavedData {
    private final Map<String, CompoundTag> dataMap = new HashMap<>();
    private String beaconName;
    private String hash256;
    private BlockPos pos;
    private Level world;

    public static BeaconData load(CompoundTag nbt) {
        BeaconData data = new BeaconData();
        for (String key : nbt.getAllKeys()) {
            data.dataMap.put(key, nbt.getCompound(key));
        }
        return data;
    }

    @Override
    public @NotNull CompoundTag save(@NotNull CompoundTag nbt) {
        for (Map.Entry<String, CompoundTag> entry : dataMap.entrySet()) {
            nbt.put(entry.getKey(), entry.getValue());
        }
        return nbt;
    }

    public void setData(String id, CompoundTag nbt) {
        dataMap.put(id, nbt);
        this.setDirty();
    }

    public CompoundTag getData(String id) {
        return dataMap.getOrDefault(id, new CompoundTag());
    }

    public void removeData(String id) {
        dataMap.remove(id);
        setDirty();
    }
}
