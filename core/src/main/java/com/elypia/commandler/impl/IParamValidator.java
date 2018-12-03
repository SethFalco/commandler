package com.elypia.commandler.impl;

import com.elypia.commandler.metadata.MetaParam;
import com.elypia.commandler.registers.ParseRegister;

import java.lang.annotation.Annotation;
import java.util.Objects;
import java.util.function.Function;

public abstract class IParamValidator<CE extends ICommandEvent, O, A extends Annotation> {

    protected Function<A, String> help;

    public IParamValidator(Function<A, String> help) {
        this.help = Objects.requireNonNull(help);
    }

    /**
     * @param event The command event that required validation.
     * @param output The output produced by the {@link ParseRegister} when parsing input.
     * @param annotation The validation annotation we're validation against.
     * @param param The meta-data for this parameter.
     * @return If this parameter has passed validation or if the command should be invalidated.
     */
    abstract public boolean validate(CE event, O output, A annotation, MetaParam param);

    public String help(A annotation) {
        return help.apply(annotation);
    }
}
