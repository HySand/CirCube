package me.zephyr.circube.content.vlobby;

import commoble.infiniverse.api.InfiniverseAPI;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.FlatLevelSource;
import net.minecraft.world.level.levelgen.flat.FlatLayerInfo;
import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorSettings;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static me.zephyr.circube.CirCube.MOD_ID;


public class DungeonManager {
    public static Map<String, String> dungeonnameMap = new HashMap<>();

    public static ServerLevel instantiateDimension(MinecraftServer server, String mapName) throws IOException {
        String timestamp = String.valueOf(java.time.Instant.now().getEpochSecond());
        String mapFolder = mapName.toLowerCase() + "_" + timestamp;
        copyDimensionFolder(mapName, timestamp);
        dungeonnameMap.put(mapName, mapFolder);
        InfiniverseAPI api = InfiniverseAPI.get();
        ResourceKey<Level> levelKey = ResourceKey.create(Registries.DIMENSION, ResourceLocation.fromNamespaceAndPath(MOD_ID, mapFolder));
        return api.getOrCreateLevel(server, levelKey, () -> createLevel(server));
    }

    public static void unloadDimension(MinecraftServer server, String mapFolder) {
        dungeonnameMap.remove(mapFolder);
        InfiniverseAPI api = InfiniverseAPI.get();
        ResourceKey<Level> levelKey = ResourceKey.create(Registries.DIMENSION, ResourceLocation.fromNamespaceAndPath(MOD_ID, mapFolder));
        api.markDimensionForUnregistration(server, levelKey);
    }

    public static LevelStem createLevel(MinecraftServer server) {
        ServerLevel oldLevel = server.overworld();
        Holder<DimensionType> typeHolder = oldLevel.dimensionTypeRegistration();

        RegistryAccess registryAccess = oldLevel.registryAccess();
        Registry<Biome> biomeRegistry = registryAccess.registryOrThrow(Registries.BIOME);
        Holder<Biome> plainsBiome = biomeRegistry.getHolderOrThrow(Biomes.PLAINS);

        FlatLevelGeneratorSettings flatSettings = new FlatLevelGeneratorSettings(
                Optional.empty(),
                plainsBiome,
                List.of()
        );
        flatSettings.getLayersInfo().add(new FlatLayerInfo(1, Blocks.BARRIER));
        flatSettings.updateLayers();

        ChunkGenerator flatGenerator = new FlatLevelSource(flatSettings);
        return new LevelStem(typeHolder, flatGenerator);
    }

    public static void copyDimensionFolder(String mapName, String timestamp) throws IOException {
        Path serverRoot = Path.of(".");
        Path source = serverRoot.resolve("Resources").resolve(mapName.toLowerCase());
        Path target = serverRoot.resolve("world").resolve("dimensions").resolve(MOD_ID).resolve(mapName + "_" + timestamp);

        try (Stream<Path> paths = Files.walk(source)) {
            paths.forEach(src -> {
                try {
                    Path dest = target.resolve(source.relativize(src));
                    if (Files.isDirectory(src)) {
                        Files.createDirectories(dest);
                    } else {
                        Files.copy(src, dest, StandardCopyOption.REPLACE_EXISTING);
                    }
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            });
        }
    }


}    