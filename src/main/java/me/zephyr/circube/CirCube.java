package me.zephyr.circube;

import com.mojang.logging.LogUtils;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.KineticStats;
import com.simibubi.create.foundation.item.TooltipModifier;
import com.simibubi.create.foundation.pack.ModFilePackResources;
import com.tacz.guns.api.resource.ResourceManager;
import me.zephyr.circube.config.CirCubeConfigs;
import me.zephyr.circube.content.vlobby.Dungeon;
import me.zephyr.circube.content.vlobby.dungeons.Arena;
import me.zephyr.circube.content.vlobby.dungeons.Pit;
import me.zephyr.circube.content.vlobby.dungeons.Test;
import me.zephyr.circube.content.vlobby.dungeons.Workshop;
import me.zephyr.circube.data.CirCubeDataGen;
import me.zephyr.circube.event.HealthAmplifier;
import me.zephyr.circube.event.PasswordCrackingEvents;
import me.zephyr.circube.worldgen.CirCubeFeatures;
import net.createmod.catnip.lang.FontHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.forgespi.language.IModFileInfo;
import net.minecraftforge.forgespi.locating.IModFile;
import org.slf4j.Logger;

import static me.zephyr.circube.util.DataManager.addDungeonToList;
import static me.zephyr.circube.util.Utils.generatePassword;
import static net.lpcamors.optical.COMod.loc;

@Mod(CirCube.MOD_ID)
public class CirCube {
    public static final String MOD_ID = "circube";
    public static final String MOD_NAME = "CirCube";
    public static final Logger LOGGER = LogUtils.getLogger();
    private static final CreateRegistrate REGISTRATE = CreateRegistrate.create(MOD_ID)
            .defaultCreativeTab((ResourceKey<CreativeModeTab>) null)
            .setTooltipModifierFactory(item ->
                    new ItemDescription.Modifier(item, FontHelper.Palette.STANDARD_CREATE)
                            .andThen(TooltipModifier.mapNull(KineticStats.create(item)))
            );
    public static IEventBus modEventBus;
    public static IEventBus forgeEventBus;

    static {
        REGISTRATE.setTooltipModifierFactory(item -> new ItemDescription.Modifier(item, FontHelper.Palette.STANDARD_CREATE)
                .andThen(TooltipModifier.mapNull(KineticStats.create(item))));
    }

    public CirCube(FMLJavaModLoadingContext context) {
        modEventBus = context.getModEventBus();
        forgeEventBus = MinecraftForge.EVENT_BUS;

        forgeEventBus.register(this);

        modEventBus.addListener(this::onCommonSetup);
        if (FMLEnvironment.dist == Dist.DEDICATED_SERVER) {
            modEventBus.addListener(this::onDedicatedServerSetup);
        } else {
            modEventBus.addListener(this::addPackFinders);
        }


        REGISTRATE.registerEventListeners(modEventBus);

        CirCubePackets.register();
        CirCubeBlocks.register();
        CirCubeItems.register();
        CirCubeEffects.register(modEventBus);
        CirCubeFeatures.register(modEventBus);
        CirCubeMenuTypes.register();
        CirCubeCreativeTabs.register(modEventBus);
        CirCubeConfigs.register(context);

        PasswordCrackingEvents.PASSWORD = generatePassword();

        modEventBus.addListener(EventPriority.LOWEST, CirCubeDataGen::gatherData);
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> CirCubeClient.onCirCubeClient(modEventBus, forgeEventBus));

        registerExtraGunPack("cgp");
    }

    public static CreateRegistrate getRegistrate() {
        return REGISTRATE;
    }

    public static ResourceLocation asResource(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

    private static void registerExtraGunPack(String packname) {
        String jarPackPath = String.format("/assets/%s/gunpack/%s", MOD_ID, packname);
        ResourceManager.registerExportResource(CirCube.class, jarPackPath);
    }

    private void onDedicatedServerSetup(FMLDedicatedServerSetupEvent event) {
        int dungeonId = 0;
        Dungeon arena = new Arena(dungeonId++, "arena", 1, 3);
        Dungeon pit = new Pit(dungeonId++, "pit", 3, 3);
        Dungeon workshop = new Workshop(dungeonId++, "workshop", 3, 3);
        Dungeon test = new Test(dungeonId++, "testd", 0, 1);
        addDungeonToList(arena);
        addDungeonToList(pit);
        addDungeonToList(workshop);
        addDungeonToList(test);
    }

    private void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            CirCubeContraptionMovementSettings.registerDefaults();
            HealthAmplifier.initBoss();
        });
    }

    public void addPackFinders(AddPackFindersEvent event) {
        if (event.getPackType() == PackType.CLIENT_RESOURCES) {
            IModFileInfo modFileInfo = ModList.get().getModFileById(MOD_ID);
            if (modFileInfo == null) {
                LOGGER.error("Could not find CirCube mod file info; built-in resource packs will be missing!");
                return;
            }
            IModFile modFile = modFileInfo.getFile();
            event.addRepositorySource(consumer -> {
                Pack pack = Pack.readMetaAndCreate(loc("circube_resource").toString(), Component.literal("CirCube Server Resource Pack"), false, id -> new ModFilePackResources(id, modFile, "resourcepacks/circube_resource"), PackType.CLIENT_RESOURCES, Pack.Position.TOP, PackSource.BUILT_IN);
                if (pack != null) {
                    consumer.accept(pack);
                }
            });
        }
    }
}
