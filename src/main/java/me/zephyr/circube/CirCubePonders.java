package me.zephyr.circube;

import com.simibubi.create.foundation.ponder.PonderRegistrationHelper;
import com.simibubi.create.foundation.ponder.PonderRegistry;
import com.simibubi.create.infrastructure.ponder.AllPonderTags;
import me.zephyr.circube.ponder.SpringScenes;

import static me.zephyr.circube.CirCube.MODID;
import static me.zephyr.circube.CirCubeBlocks.SPRING;

public class CirCubePonders {
    static final PonderRegistrationHelper HELPER = new PonderRegistrationHelper(MODID);

    public static void register() {
        HELPER.forComponents(SPRING)
                .addStoryBoard("spring", SpringScenes::Spring, AllPonderTags.KINETIC_SOURCES)
                .addStoryBoard("spring_signal", SpringScenes::SpringSignal, AllPonderTags.KINETIC_SOURCES);
        PonderRegistry.TAGS.forTag(AllPonderTags.KINETIC_SOURCES).add(SPRING);
    }
}
