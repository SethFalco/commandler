package com.elypia.commandler.dispatchers;

import com.elypia.commandler.CommandlerEvent;
import com.elypia.commandler.exceptions.*;
import com.elypia.commandler.interfaces.*;

public class TemplateDispatcher implements Dispatcher {

    @Override
    public boolean isValid(Object event, String content) {
        return true;
    }

    @Override
    public Object dispatch(Controller controller, Object event, String content) {
        return null;
    }

    @Override
    public CommandlerEvent parse(Controller controller, Object source, String content) throws OnlyPrefixException, NoDefaultCommandException, ModuleNotFoundException, ParamCountMismatchException {
        return null;
    }
}
