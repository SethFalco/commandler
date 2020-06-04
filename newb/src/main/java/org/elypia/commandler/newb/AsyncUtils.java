/*
 * Copyright 2019-2020 Elypia CIC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.elypia.commandler.newb;

import org.jboss.weld.context.WeldAlterableContext;
import org.jboss.weld.context.api.ContextualInstance;
import org.jboss.weld.context.bound.*;
import org.jboss.weld.manager.api.WeldManager;

import javax.enterprise.inject.spi.CDI;
import java.lang.annotation.Annotation;
import java.util.*;

public final class AsyncUtils {

    private AsyncUtils() {
        // Do nothing
    }

    public static Map<Class<? extends Annotation>, Collection<ContextualInstance<?>>> copyContext() {
        Map<Class<? extends Annotation>, Collection<ContextualInstance<?>>> scopeToContextualInstances = new HashMap<>();

        for (WeldAlterableContext context : CDI.current().select(WeldManager.class).get().getActiveWeldAlterableContexts())
            scopeToContextualInstances.put(context.getScope(), context.getAllContextualInstances());

        return scopeToContextualInstances;
    }

    public static BoundRequestContext applyContext(Map<Class<? extends Annotation>, Collection<ContextualInstance<?>>> scopeToContextualInstances) {
        BoundRequestContext requestContext = CDI.current().select(BoundRequestContext.class, BoundLiteral.INSTANCE).get();
        Map<String, Object> requestMap = new HashMap<>();
        requestContext.associate(requestMap);
        requestContext.activate();

        if (scopeToContextualInstances.get(requestContext.getScope()) != null)
            requestContext.clearAndSet(scopeToContextualInstances.get(requestContext.getScope()));

        return requestContext;
    }
}
