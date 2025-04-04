/*
 * Copyright 2019-2025 Seth Falco and Commandler Contributors
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

package fun.falco.commandler.producers;

import java.text.NumberFormat;
import java.util.Locale;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.apache.deltaspike.core.api.message.LocaleResolver;

@RequestScoped
public class NumberFormatProducer {

    private LocaleResolver resolver;

    @Inject
    public NumberFormatProducer(LocaleResolver resolver) {
        this.resolver = resolver;
    }

    @Produces
    public NumberFormat getNumberFormat() {
        Locale locale = resolver.getLocale();
        return NumberFormat.getInstance(locale);
    }
}
