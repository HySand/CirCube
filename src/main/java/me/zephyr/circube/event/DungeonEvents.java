package me.zephyr.circube.event;

import commoble.infiniverse.api.InfiniverseAPI;
import commoble.infiniverse.api.UnregisterDimensionEvent;
import me.zephyr.circube.CirCube;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static me.zephyr.circube.CirCube.MOD_ID;

public class DungeonEvents {
    @Mod.EventBusSubscriber(modid = MOD_ID)
    public class CleanDimensionsEvent {
        @SubscribeEvent
        public static void onServerStart(ServerStartedEvent event) {
            MinecraftServer server = event.getServer();
            InfiniverseAPI api = InfiniverseAPI.get();
            server.getAllLevels().forEach(level -> {
                ResourceKey<Level> levelKey = level.dimension();
                if (level.dimension().location().getNamespace().equals(MOD_ID)) {
                    api.markDimensionForUnregistration(server, levelKey);
                    CirCube.LOGGER.debug(api.getLevelsPendingUnregistration().toString());
                }
            });
        }

        @SubscribeEvent
        public static void onDimensionUnregister(UnregisterDimensionEvent event) throws IOException {
            Path serverRoot = Path.of(".");
            Path target = serverRoot.resolve("world").resolve("dimensions").resolve(MOD_ID).resolve(event.getLevel().dimension().location().getPath());

            ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
            executor.schedule(() -> {
                if (Files.exists(target)) {
                    try {
                        Files.walk(target)
                                .sorted(java.util.Comparator.reverseOrder())
                                .forEach(path -> {
                                    try {
                                        Files.delete(path);
                                    } catch (IOException e) {
                                        throw new RuntimeException("Failed to delete: " + path, e);
                                    }
                                });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }, 3, TimeUnit.SECONDS);

            executor.shutdown();
        }
    }

}
