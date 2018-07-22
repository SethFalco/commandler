package com.elypia.commandler.validation;

import com.elypia.commandler.CommandEvent;
import com.elypia.commandler.annotations.validation.param.Partial;
import com.elypia.commandler.impl.IParamValidator;
import com.elypia.commandler.metadata.MetaParam;

public class PartialValidator implements IParamValidator<CommandEvent, Object, Partial> {

    @Override
    public boolean validate(CommandEvent event, Object input, Partial annotation, MetaParam param) {
        Class<?> type = input.getClass();

//        if (type == boolean.class) {
//            boolean[] booleans = (boolean[])input;
//            Array.setBoolean(output, i, (boolean) o);
//        } else if (type == char.class) {
//            Array.setChar(output, i, (char) o);
//        } else if (type == double.class) {
//            Array.setDouble(output, i, (double) o);
//        } else if (type == float.class) {
//            Array.setFloat(output, i, (float) o);
//        } else if (type == long.class) {
//            Array.setLong(output, i, (long) o);
//        } else if (type == int.class) {
//            Array.setInt(output, i, (int) o);
//        } else if (type == short.class) {
//            Array.setShort(output, i, (short) o);
//        } else if (type == byte.class) {
//            Array.setByte(output, i, (byte) o);
//        } else {
//            Array.set(output, i, o);
//        }

        return false;
    }

    @Override
    public String help(Partial annotation) {
        return null;
    }
}
