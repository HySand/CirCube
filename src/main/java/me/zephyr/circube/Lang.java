package me.zephyr.circube;

import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.foundation.utility.LangBuilder;
import com.simibubi.create.foundation.utility.LangNumberFormat;
import net.minecraft.network.chat.MutableComponent;

import static me.zephyr.circube.CirCube.MODID;

public class Lang {

    public static MutableComponent translateDirect(String key, Object... args) {
        return Components.translatable(MODID + "." + key, resolveBuilders(args));
    }

    public static LangBuilder builder() {
        return new LangBuilder(MODID);
    }

    public static LangBuilder number(double d) {
        return builder().text(LangNumberFormat.format(d));
    }

    public static LangBuilder translate(String langKey, Object... args) {
        return builder().translate(langKey, args);
    }

    public static LangBuilder text(String text) {
        return builder().text(text);
    }

    public static Object[] resolveBuilders(Object[] args) {
        for (int i = 0; i < args.length; i++)
            if (args[i] instanceof LangBuilder cb)
                args[i] = cb.component();
        return args;
    }
}
