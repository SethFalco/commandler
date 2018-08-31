//package com.elypia.commandler.validation;
//
//import com.elypia.commandler.CommandEvent;
//import com.elypia.commandler.annotations.validation.param.Length;
//import com.elypia.commandler.impl.IParamValidator;
//import com.elypia.commandler.metadata.MetaParam;
//
//import java.util.function.Function;
//
//public class LengthValidator extends IParamValidator<CommandEvent, String, Length> {
//
//    public static final Function<Length, String> DEFAULT_HELP = (length) -> {
//        StringBuilder builder = new StringBuilder("This parameter must be ");
//
//        if (length.min() == Length.DEFAULT_MIN && length.max() != Length.DEFAULT_MAX)
//            builder.append(String.format("less than %,d characters long!", length.max()));
//        else if (length.min() != Length.DEFAULT_MIN && length.max() == Length.DEFAULT_MAX)
//            builder.append(String.format("more than %,d characters long!", length.min()));
//        else
//            return String.format("between %,d and %,d characters long.", length.min(), length.max());
//
//        return builder.toString();
//    };
//
//    public LengthValidator(Function<Length, String> help) {
//        super(help);
//    }
//
//    @Override
//    public boolean validate(CommandEvent event, String s, Length length, MetaParam param) {
//        int l = s.length();
//        return l >= length.min() && l <= length.max();
//    }
//}
