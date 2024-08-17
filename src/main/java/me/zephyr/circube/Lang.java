package me.zephyr.circube;

import com.simibubi.create.foundation.utility.LangBuilder;
import com.simibubi.create.foundation.utility.LangNumberFormat;

import static me.zephyr.circube.Circube.MODID;

public class Lang {

    public static LangBuilder builder() {
        return new LangBuilder(MODID);
    }

    public static LangBuilder number(double d) {
        return builder().text(LangNumberFormat.format(d));
    }

    public static LangBuilder translate(String langKey, Object... args) {
        return builder().translate(langKey, args);
    }
}
