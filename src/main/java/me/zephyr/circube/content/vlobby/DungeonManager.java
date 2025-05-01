package me.zephyr.circube.content.vlobby;

import com.mojang.serialization.DynamicOps;
import commoble.infiniverse.api.InfiniverseAPI;
import net.minecraft.commands.CommandRuntimeException;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;

import static me.zephyr.circube.CirCube.MOD_ID;


public class DungeonManager {
    private static final String DUNGEONS_DIR = "dungeons";

    public static void instantiateDimension(MinecraftServer server, String mapFolder) {
        InfiniverseAPI api = InfiniverseAPI.get();
        ResourceKey<Level> LEVEL_KEY = ResourceKey.create(Registries.DIMENSION, ResourceLocation.fromNamespaceAndPath(MOD_ID, "example_dimension"));
        api.getOrCreateLevel(server, LEVEL_KEY, () -> createLevel(server));
    }

    public static void unloadDimension(MinecraftServer server, String mapFolder) {

    }

    static LevelStem createLevel(MinecraftServer server)
    {
        ServerLevel oldLevel = server.overworld();
        DynamicOps<Tag> ops = RegistryOps.create(NbtOps.INSTANCE, server.registryAccess());
        ChunkGenerator oldChunkGenerator = oldLevel.getChunkSource().getGenerator();
        ChunkGenerator newChunkGenerator = ChunkGenerator.CODEC.encodeStart(ops, oldChunkGenerator)
                .flatMap(nbt -> ChunkGenerator.CODEC.parse(ops, nbt))
                .getOrThrow(false, s ->
                {
                    throw new CommandRuntimeException(Component.literal(String.format("Error copying dimension: %s", s)));
                });
        Holder<DimensionType> typeHolder = oldLevel.dimensionTypeRegistration();
        return new LevelStem(typeHolder, newChunkGenerator);
    }
}    