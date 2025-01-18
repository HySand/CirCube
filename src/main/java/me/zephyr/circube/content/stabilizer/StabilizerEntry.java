package me.zephyr.circube.content.stabilizer;

import net.minecraft.core.BlockPos;

public class StabilizerEntry {
    private String name;   // 传送点名称
    private BlockPos location;  // 传送点位置

    // 构造函数
    public StabilizerEntry(String name, BlockPos location) {
        this.name = name;
        this.location = location;
    }

    // 获取传送点名称
    public String getName() {
        return name;
    }

    // 设置传送点名称
    public void setName(String name) {
        this.name = name;
    }


    // 获取传送点位置
    public BlockPos getLocation() {
        return location;
    }

    // 设置传送点位置
    public void setLocation(BlockPos location) {
        this.location = location;
    }
}
