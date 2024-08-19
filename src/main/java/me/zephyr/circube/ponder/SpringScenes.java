package me.zephyr.circube.ponder;

import com.simibubi.create.content.kinetics.gauge.StressGaugeBlockEntity;
import com.simibubi.create.content.redstone.nixieTube.NixieTubeBlockEntity;
import com.simibubi.create.foundation.ponder.SceneBuilder;
import com.simibubi.create.foundation.ponder.SceneBuildingUtil;
import com.simibubi.create.foundation.ponder.element.InputWindowElement;
import com.simibubi.create.foundation.utility.Pointing;
import com.simibubi.create.foundation.utility.VecHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

public class SpringScenes {
    public static void Spring(SceneBuilder scene, SceneBuildingUtil util) {
        scene.title("spring", "Store Your Stress Temporarily");
        scene.configureBasePlate(0, 0, 5);
        scene.world.showSection(util.select.layer(0), Direction.UP);

        BlockPos handCrank = util.grid.at(0, 1, 2);
        BlockPos spring = util.grid.at(1, 1, 2);
        BlockPos gauge = util.grid.at(3, 1, 2);
        BlockPos deployer = util.grid.at(4, 1, 2);

        for (int i = 0; i < 3; i++) {
            scene.idle(5);
            scene.world.showSection(util.select.position(1 + i, 1, 2), Direction.DOWN);
        }

        scene.idle(15);
        scene.overlay.showText(50)
                .text("Spring can store excess stress")
                .placeNearTarget()
                .pointAt(util.vector.topOf(spring));
        scene.idle(70);

        scene.world.showSection(util.select.position(handCrank), Direction.DOWN);
        scene.idle(10);
        scene.overlay.showControls(new InputWindowElement(VecHelper.getCenterOf(handCrank), Pointing.DOWN).rightClick(), 40);
        scene.idle(7);
        scene.world.setKineticSpeed(util.select.everywhere(), 32);
        scene.world.modifyBlockEntityNBT(util.select.position(gauge), StressGaugeBlockEntity.class, nbt -> nbt.putFloat("Value", 1f));
        scene.effects.indicateSuccess(gauge);
        scene.idle(40);

        scene.overlay.showText(60)
                .text("Spring will use as much of the remaining stress as possible")
                .attachKeyFrame()
                .pointAt(util.vector.blockSurface(gauge, Direction.NORTH))
                .placeNearTarget();
        scene.idle(70);

        scene.world.setKineticSpeed(util.select.everywhere(), 8);
        scene.world.modifyBlockEntityNBT(util.select.position(gauge), StressGaugeBlockEntity.class, nbt -> nbt.putFloat("Value", .2f));
        scene.idle(10);
        scene.world.showSection(util.select.position(deployer), Direction.DOWN);
        scene.idle(10);

        scene.overlay.showText(50)
                .text("Spring will continue to generate stresses after losing external stresses")
                .placeNearTarget()
                .pointAt(util.vector.topOf(spring));
        scene.idle(10);
        scene.world.moveDeployer(deployer, 1, 25);
        scene.idle(60);

        scene.world.hideSection(util.select.position(deployer), Direction.UP);
        scene.world.setKineticSpeed(util.select.everywhere(), 8);

        scene.overlay.showText(80)
                .text("Spring depletes internal stresses according to the stress impact, even if there are no other loads, and it depletes stresses itself")
                .placeNearTarget()
                .pointAt(util.vector.topOf(spring));
        scene.idle(60);

        scene.world.setKineticSpeed(util.select.everywhere(), 0);
        scene.world.modifyBlockEntityNBT(util.select.position(gauge), StressGaugeBlockEntity.class, nbt -> nbt.putFloat("Value", 0f));

        scene.idle(40);
        scene.markAsFinished();
    }

    public static void SpringSignal(SceneBuilder scene, SceneBuildingUtil util) {
        scene.title("spring_signal", "Spring Signal");
        scene.configureBasePlate(0, 0, 5);
        scene.world.showSection(util.select.layer(0), Direction.UP);

        BlockPos spring = util.grid.at(2, 1, 3);
        BlockPos nixieTube = util.grid.at(2, 2, 3);
        BlockPos clutch = util.grid.at(1, 1, 3);
        BlockPos deployer = util.grid.at(0, 1, 3);
        BlockPos lever = util.grid.at(4, 1, 1);
        scene.world.setKineticSpeed(util.select.everywhere(), 32);

        for (int i = 5; i >= 0; i--) {
            scene.idle(5);
            scene.world.showSection(util.select.position(i, 1, 3), Direction.DOWN);
        }
        scene.idle(5);
        scene.world.modifyBlockEntityNBT(util.select.position(nixieTube), NixieTubeBlockEntity.class, nbt -> nbt.putInt("RedstoneStrength", 8));
        scene.world.showSection(util.select.position(2, 2, 3), Direction.DOWN);
        for (int z = 1; z < 3; z++) {
            scene.idle(5);
            scene.world.showSection(util.select.position(4, 1, z), Direction.DOWN);
        }

        scene.idle(10);
        scene.overlay.showText(50)
                .text("Spring emits different redstone signals in different situations")
                .placeNearTarget()
                .pointAt(util.vector.topOf(spring));
        scene.idle(70);

        scene.idle(10);
        scene.overlay.showText(50)
                .text("Signal strength 8 on charging")
                .attachKeyFrame()
                .placeNearTarget()
                .pointAt(util.vector.topOf(nixieTube)
                        .add(0, -0.35, 0));
        scene.idle(70);

        scene.world.toggleRedstonePower(util.select.fromTo(lever, lever.south(2)));
        scene.effects.indicateRedstone(lever);
        scene.world.setKineticSpeed(util.select.position(4, 1, 3), 0);
        scene.world.setKineticSpeed(util.select.fromTo(deployer, deployer.east(3)), 8);
        scene.world.toggleRedstonePower(util.select.position(clutch));
        scene.world.modifyBlockEntityNBT(util.select.position(nixieTube), NixieTubeBlockEntity.class, nbt -> nbt.putInt("RedstoneStrength", 0));
        scene.idle(10);
        scene.overlay.showText(50)
                .text("No signal during working normally")
                .attachKeyFrame()
                .placeNearTarget()
                .pointAt(util.vector.topOf(nixieTube)
                        .add(0, -0.35, 0));
        scene.idle(10);
        scene.world.moveDeployer(deployer, 1, 25);
        scene.idle(26);
        scene.world.moveDeployer(deployer, -1, 25);
        scene.idle(30);
        scene.world.moveDeployer(deployer, 1, 25);
        scene.idle(26);
        scene.world.moveDeployer(deployer, -1, 25);
        scene.idle(38);

        scene.world.setKineticSpeed(util.select.fromTo(deployer, deployer.east(3)), 0);
        scene.world.toggleRedstonePower(util.select.position(clutch));
        scene.world.modifyBlockEntityNBT(util.select.position(nixieTube), NixieTubeBlockEntity.class, nbt -> nbt.putInt("RedstoneStrength", 4));
        scene.overlay.showText(50)
                .text("Signal strength 4 on standby")
                .attachKeyFrame()
                .placeNearTarget()
                .pointAt(util.vector.topOf(nixieTube)
                        .add(0, -0.35, 0));
        scene.idle(70);

        scene.markAsFinished();
    }
}
