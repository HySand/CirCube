package me.zephyr.circube;


import com.simibubi.create.api.contraption.ContraptionMovementSetting;
import me.zephyr.circube.config.CirCubeConfigs;

public class CirCubeContraptionMovementSettings {
    public static void registerDefaults() {
        ContraptionMovementSetting.REGISTRY.register(CirCubeBlocks.GEYSER.get(), () -> CirCubeConfigs.server().kinetics.geyserMovement.get());
        ContraptionMovementSetting.REGISTRY.register(CirCubeBlocks.ANDESITE_BEACON.get(), () -> CirCubeConfigs.server().kinetics.andesiteBeaconMovement.get());
        ContraptionMovementSetting.REGISTRY.register(CirCubeBlocks.BRASS_BEACON.get(), () -> CirCubeConfigs.server().kinetics.brassBeaconMovement.get());
    }

}
