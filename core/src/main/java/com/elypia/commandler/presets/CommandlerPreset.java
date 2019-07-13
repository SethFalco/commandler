package com.elypia.commandler.presets;

import com.elypia.commandler.adapters.*;
import com.elypia.commandler.interfaces.Preset;
import com.elypia.commandler.modules.HelpModule;
import com.elypia.commandler.providers.*;

public class CommandlerPreset extends Preset {

    public CommandlerPreset(boolean modules, boolean adapters, boolean providers) {
        super(modules, adapters, providers);
    }

    public Class<?>[] getModules() {
        return new Class<?>[] {
            HelpModule.class
        };
    }

    public Class<?>[] getAdapters() {
        return new Class<?>[] {
            BooleanAdapter.class,
            CharAdapter.class,
            DurationAdapter.class,
            EnumAdapter.class,
            LocaleAdapter.class,
            MetaCommandAdapter.class,
            MetaModuleAdapter.class,
            NumberAdapter.class,
            StringAdapter.class,
            TimeUnitAdapter.class,
            UrlAdapter.class
        };
    }

    public Class<?>[] getProviders() {
        return new Class<?>[] {
            MiscToStringProvider.class,
            NumberToStringProvider.class
        };
    }
}
