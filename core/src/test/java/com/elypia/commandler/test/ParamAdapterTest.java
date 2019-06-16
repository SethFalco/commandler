package com.elypia.commandler.test;

import com.elypia.commandler.adapters.*;
import com.elypia.commandler.core.ParamAdapter;
import com.elypia.commandler.exceptions.init.AdapterRequiredException;
import com.elypia.commandler.meta.data.MetaAdapter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ParamAdapterTest {

    @Test
    public void adapterTest() {
        MetaAdapter meta = new MetaAdapter(BooleanAdapter.class, Boolean.class, boolean.class);
        ParamAdapter adapter = new ParamAdapter(meta);

        assertNotNull(adapter.getAdapter(Boolean.class));
        assertThrows(AdapterRequiredException.class, () -> adapter.getAdapter(StringAdapter.class));
    }
}
