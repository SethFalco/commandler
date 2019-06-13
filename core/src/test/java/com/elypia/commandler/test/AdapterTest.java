package com.elypia.commandler.test;

import com.elypia.commandler.adapters.*;
import com.elypia.commandler.exceptions.AdapterRequiredException;
import com.elypia.commandler.metadata.ParamAdapter;
import com.elypia.commandler.metadata.data.MetaAdapter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AdapterTest {

    @Test
    public void adapterTest() {
        MetaAdapter meta = new MetaAdapter(BooleanAdapter.class, Boolean.class, boolean.class);
        ParamAdapter adapter = new ParamAdapter(meta);

        assertNotNull(adapter.getAdapter(Boolean.class));
        assertThrows(AdapterRequiredException.class, () -> adapter.getAdapter(StringAdapter.class));
    }
}
