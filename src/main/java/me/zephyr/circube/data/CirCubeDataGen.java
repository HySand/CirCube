package me.zephyr.circube.data;


import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.utility.FilesHelper;
import com.tterrag.registrate.providers.ProviderType;
import me.zephyr.circube.CirCube;
import me.zephyr.circube.CirCubeKeys;
import me.zephyr.circube.data.recipes.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.data.event.GatherDataEvent;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

@SuppressWarnings("removal")
public class CirCubeDataGen {
    private static final CreateRegistrate REGISTRATE = CirCube.getRegistrate();

    public static void gatherData(GatherDataEvent event) {
        addExtraRegistrateData();

        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        GeneratedEntriesProvider generatedEntriesProvider = new GeneratedEntriesProvider(output, lookupProvider);
        lookupProvider = generatedEntriesProvider.getRegistryProvider();
        generator.addProvider(event.includeServer(), generatedEntriesProvider);

        generator.addProvider(event.includeServer(), new CirCubeStandardRecipeGen(output));
        generator.addProvider(event.includeServer(), new CirCubeMillingRecipeGen(output));
        generator.addProvider(event.includeServer(), new CirCubeCrushingRecipeGen(output));
        generator.addProvider(event.includeServer(), new CirCubePressingRecipeGen(output));
        generator.addProvider(event.includeServer(), new CirCubeCompactingRecipeGen(output));
        generator.addProvider(event.includeServer(), new CirCubeMixingRecipeGen(output));
        generator.addProvider(event.includeClient(), new CirCubeCuttingRecipeGen(output));
        generator.addProvider(event.includeServer(), new CirCubeSequencedAssemblyRecipeGen(output));
        generator.addProvider(event.includeServer(), new CirCubeMechanicalCraftingRecipeGen(output));
        generator.addProvider(event.includeServer(), new CirCubeWashingRecipeGen(output));
        generator.addProvider(event.includeServer(), new CirCubeHauntingRecipeGen(output));
        generator.addProvider(event.includeServer(), new CirCubeFocusingRecipeGen(output));
        generator.addProvider(event.includeServer(), new CirCubeBulkFermentingRecipeGen(output));
    }

    private static void addExtraRegistrateData() {
        REGISTRATE.addDataGenerator(ProviderType.LANG, provider -> {
            BiConsumer<String, String> langConsumer = provider::add;

            provideDefaultLang("interface", langConsumer);
            provideDefaultLang("tooltips", langConsumer);
            CirCubeKeys.provideLang(langConsumer);
        });
    }

    private static void provideDefaultLang(String fileName, BiConsumer<String, String> consumer) {
        String path = "assets/circube/lang/default/" + fileName + ".json";
        JsonElement jsonElement = FilesHelper.loadJsonResource(path);
        if (jsonElement == null) {
            throw new IllegalStateException(String.format("Could not find default lang file: %s", path));
        }
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue().getAsString();
            consumer.accept(key, value);
        }
    }
}
