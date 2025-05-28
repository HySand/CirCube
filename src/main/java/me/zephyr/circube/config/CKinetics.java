package me.zephyr.circube.config;

import com.simibubi.create.api.contraption.ContraptionMovementSetting;
import net.createmod.catnip.config.ConfigBase;

public class CKinetics extends ConfigBase {
    public final ConfigGroup contraptions = group(1, "contraptions", "Moving Contraptions");
    public final ConfigEnum<ContraptionMovementSetting> geyserMovement =
            e(ContraptionMovementSetting.UNMOVABLE, "movableGeyser", Comments.geyserMovement);
    public final ConfigEnum<ContraptionMovementSetting> andesiteBeaconMovement =
            e(ContraptionMovementSetting.UNMOVABLE, "movableAndesiteBeacon", Comments.andesiteBeaconMovement);
    public final ConfigEnum<ContraptionMovementSetting> brassBeaconMovement =
            e(ContraptionMovementSetting.UNMOVABLE, "movableBrassBeacon", Comments.brassBeaconMovement);

    @Override
    public String getName() {
        return "kinetics";
    }

    private static class Comments {
        static String geyserMovement = "Configure how Geyser blocks can be moved by contraptions.";
        static String andesiteBeaconMovement = "Configure how Andesite Beacon blocks can be moved by contraptions.";
        static String brassBeaconMovement = "Configure how Brass Beacon blocks can be moved by contraptions.";
    }
}
