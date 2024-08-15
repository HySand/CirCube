package me.zephyr.circube.mixins.quark;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.violetmoon.quark.base.Quark;
import org.violetmoon.quark.base.handler.ContributorRewardHandler;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Properties;

@Mixin(value = ContributorRewardHandler.class, remap = false)
public abstract class ContributorRewardHandlerMixin {

    @Shadow
    private static Thread thread;

    @Shadow
    private static void load(Properties props) {
    }

    /**
     * @author zephyr
     * @reason use gitee mirror(fake)
     */
    @Overwrite
    public static void init() {
        if (thread != null && thread.isAlive())
            return;

        thread = new Thread(() -> {
            try {
                URL url = new URL("https://gitee.com/hysand/CirCube/raw/main/contributor.properties");
                URLConnection conn = url.openConnection();
                conn.setConnectTimeout(10 * 1000);
                conn.setReadTimeout(10 * 1000);

                Properties patreonTiers = new Properties();
                try (InputStreamReader reader = new InputStreamReader(conn.getInputStream())) {
                    patreonTiers.load(reader);
                    load(patreonTiers);
                }
            } catch (IOException e) {
                Quark.LOG.error("Failed to load patreon information", e);
            }
        });

        thread.setName("Quark Contributor Loading Thread");
        thread.setDaemon(true);
        thread.start();
    }
}

