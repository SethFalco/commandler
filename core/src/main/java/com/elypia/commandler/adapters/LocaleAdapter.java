package com.elypia.commandler.adapters;

import com.elypia.commandler.CommandlerEvent;
import com.elypia.commandler.annotations.Adapter;
import com.elypia.commandler.interfaces.ParamAdapter;
import com.elypia.commandler.metadata.MetaParam;
import org.slf4j.*;

import javax.inject.Singleton;
import java.util.*;

@Singleton
@Adapter(Locale.class)
public class LocaleAdapter implements ParamAdapter<Locale> {

    private static final Logger logger = LoggerFactory.getLogger(LocaleAdapter.class);

    private static final Locale[] LOCALES = Locale.getAvailableLocales();

    /** Many chat services use two regional indicators to represent a country. */
    private static final Map<String, String> regionalIndicators = Map.ofEntries(
        Map.entry("A", "\uD83C\uDDE6"),
        Map.entry("B", "\uD83C\uDDE7"),
        Map.entry("C", "\uD83C\uDDE8"),
        Map.entry("D", "\uD83C\uDDE9"),
        Map.entry("E", "\uD83C\uDDEA"),
        Map.entry("F", "\uD83C\uDDEB"),
        Map.entry("G", "\uD83C\uDDEC"),
        Map.entry("H", "\uD83C\uDDED"),
        Map.entry("I", "\uD83C\uDDEE"),
        Map.entry("J", "\uD83C\uDDEF"),
        Map.entry("K", "\uD83C\uDDF0"),
        Map.entry("L", "\uD83C\uDDF1"),
        Map.entry("M", "\uD83C\uDDF2"),
        Map.entry("N", "\uD83C\uDDF3"),
        Map.entry("O", "\uD83C\uDDF4"),
        Map.entry("P", "\uD83C\uDDF5"),
        Map.entry("Q", "\uD83C\uDDF6"),
        Map.entry("R", "\uD83C\uDDF7"),
        Map.entry("S", "\uD83C\uDDF8"),
        Map.entry("T", "\uD83C\uDDF9"),
        Map.entry("U", "\uD83C\uDDFA"),
        Map.entry("V", "\uD83C\uDDFB"),
        Map.entry("W", "\uD83C\uDDFC"),
        Map.entry("X", "\uD83C\uDDFD"),
        Map.entry("Y", "\uD83C\uDDFE"),
        Map.entry("Z", "\uD83C\uDDFF")
    );

    @Override
    public Locale adapt(String input, Class<? extends Locale> type, MetaParam param, CommandlerEvent<?> event) {
        for (Locale locale : LOCALES) {
            if (input.equalsIgnoreCase(locale.toLanguageTag()))
                return locale;

            if (!locale.getCountry().isBlank()) {
                String c = locale.getCountry();
                String dc = locale.getDisplayCountry(Locale.US);
                String dcl = locale.getDisplayCountry(locale);

                if (input.equalsIgnoreCase(c) || input.equalsIgnoreCase(dc) || input.equalsIgnoreCase(dcl))
                    return locale;

                if (input.equalsIgnoreCase(replaceWithIndictors(c)))
                    return locale;

                try {
                    if (input.equalsIgnoreCase(locale.getISO3Country()))
                        return locale;
                } catch (MissingResourceException ex) {
                    // Do nothing, there is nothing wrong with this in this case.
                }
            } else {
                String l = locale.getLanguage();
                String dl = locale.getDisplayLanguage(Locale.US);
                String dll = locale.getDisplayLanguage(locale);
                String lc = locale.getISO3Language();

                if (input.equalsIgnoreCase(l) || input.equalsIgnoreCase(dl) || input.equalsIgnoreCase(dll) || input.equalsIgnoreCase(lc))
                    return locale;
            }
        }

        return null;
    }

    /**
     * Replace all upper case characters with regional indicators
     * characters instead.
     *
     * @param input The source string to replace from.
     * @return The new string with all upper case characters replaced.
     */
    private String replaceWithIndictors(String input) {
        for (Map.Entry<String, String> entry : regionalIndicators.entrySet())
            input = input.replace(entry.getKey(), entry.getValue());

        return input;
    }
}
