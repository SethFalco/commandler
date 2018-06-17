package com.elypia.commandler.parsing.parsers;

import com.elypia.commandler.data.SearchScope;
import com.elypia.commandler.events.MessageEvent;
import com.elypia.commandler.parsing.IParamParser;

public class DoubleParser implements IParamParser<Double> {

    @Override
    public Double parse(MessageEvent event, SearchScope scope, String input) {
        try {
            return Double.parseDouble(input);
        } catch (NumberFormatException ex){
            event.invalidate("Parameter `" + input + "` is not a number.");
            return null;
        }
    }
}
