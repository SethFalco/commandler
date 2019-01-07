package com.elypia.commandler.test.impl.modules;

import com.elypia.commandler.*;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.impl.CommandEvent;
import com.elypia.commandler.metadata.ModuleData;

import java.io.IOException;
import java.net.Socket;
import java.util.StringJoiner;
import java.util.regex.Matcher;

@Module(id = "Match", aliases = "match")
public class MatchModule extends Handler<String, String> {

    /**
     * Initialise the module, this will assign the values
     * in the module and create a {@link ModuleData} which is
     * what {@link Commandler} uses in runtime to identify modules,
     * commands or obtain any static data.
     *
     * @param commandler Our parent Commandler class.
     * @return Returns if the {@link #test()} for this module passed.
     */
    public MatchModule(Commandler<String, String> commandler) {
        super(commandler);
    }

    @Override
    public boolean test() {
        try (Socket socket = new Socket("http://converstions.io/", 80)) {
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Match("(?:^|\\s)(?<currency>[$$€])(?<value>[\\d,]+(?:\\.\\d{1,2})?)(?:$|\\s)")
    public String convertCurrency(CommandEvent<String, String> event, Matcher matcher) {
        // Imagine on startup we cached these values from API
        final double USD_TO_GBP = 0.79;
        final double USD_TO_EUR = 0.88;

        StringJoiner joiner = new StringJoiner("\n\n");

        while (matcher.find()) {
            StringBuilder builder = new StringBuilder();

            builder.append(matcher.group(0));
            String currency = matcher.group("currency");
            double value = Double.parseDouble(matcher.group("value").replace(",", ""));

            switch (currency) {
                case "$": {
                    builder.append("GBP: " + (value * USD_TO_GBP));
                    builder.append("EUR: " + (value * USD_TO_EUR));
                    break;
                }
                case "£": {
                    builder.append("USD: " + (value / USD_TO_GBP));
                    builder.append("EUR: " + (value / USD_TO_GBP * USD_TO_EUR));
                    break;
                }
                case "€": {
                    builder.append("USD: " + (value / USD_TO_EUR));
                    builder.append("GBP: " + (value / USD_TO_EUR * USD_TO_GBP));
                    break;
                }
            }

            joiner.add(builder.toString());
        }

        return joiner.toString();
    }
}
