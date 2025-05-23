package me.zephyr.circube.config;

import net.createmod.catnip.config.ConfigBase;

public class CServer extends ConfigBase {
    public final CStress stressValues = nested(0, CStress::new, Comments.stress);
    public final CKinetics kinetics = nested(0, CKinetics::new, Comments.kinetics);

    @Override
    public String getName() {
        return "server";
    }

    private static class Comments {
        static String stress = "Fine tune the kinetic stats of individual components";
        static String kinetics = "Parameters and abilities of CirCube's kinetic mechanisms";
    }
}
