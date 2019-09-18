package org.elypia.commandler;

import org.elypia.commandler.adapters.*;
import org.elypia.commandler.exceptions.AdapterRequiredException;
import org.elypia.commandler.managers.*;
import org.elypia.commandler.metadata.MetaAdapter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AdapterManagerTest {

    @Test
    public void adapterTest() {
        MetaAdapter meta = new MetaAdapter(BooleanAdapter.class, Boolean.class, boolean.class);
        AdapterManager adapter = new AdapterManager(new InjectionManager(), meta);

        assertAll("Verify we can get our adapter back correctly.",
            () -> assertNotNull(adapter.getAdapter(Boolean.class)),
            () -> assertThrows(AdapterRequiredException.class, () -> adapter.getAdapter(StringAdapter.class))
        );
    }
}
