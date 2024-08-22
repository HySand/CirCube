package me.zephyr.circube;

import com.simibubi.create.AllShapes;
import com.simibubi.create.foundation.utility.VoxelShaper;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CirCubeShapes {
    public static final VoxelShaper
            BEACON = getShape(0, 0, 0, 16, 2, 16)
            .add(1, 2, 1, 15, 3, 15)
            .add(2, 3, 2, 14, 8, 14)
            .add(3, 8, 3, 13, 16, 13)
            .forDirectional();


    private static AllShapes.Builder getShape(VoxelShape shape) {
        return new AllShapes.Builder(shape);
    }

    private static AllShapes.Builder getShape(double x1, double y1, double z1, double x2, double y2, double z2) {
        return getShape(cuboid(x1, y1, z1, x2, y2, z2));
    }

    private static VoxelShape cuboid(double x1, double y1, double z1, double x2, double y2, double z2) {
        return Block.box(x1, y1, z1, x2, y2, z2);
    }
}
