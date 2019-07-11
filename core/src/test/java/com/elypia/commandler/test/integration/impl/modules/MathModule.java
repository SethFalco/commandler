package com.elypia.commandler.test.integration.impl.modules;

import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.interfaces.Handler;

import javax.inject.Singleton;
import java.util.stream.*;

@Singleton
@Module(name = "Math", aliases = "math", help = "Perform mathamatical functions.")
public class MathModule implements Handler {

    @Command(name = "Sum", aliases = "sum")
    public long sum(
        @Param(name = "numbers", help = "A list of numbers to add together.") long[] numbers
    ) {
        return LongStream.of(numbers).sum();
    }

    @Command(name = "Sum (Decimal)", aliases = "sumd")
    public double sumd(@Param(name = "numbers", help = "A list of numbers to add together.") double[] numbers) {
        return DoubleStream.of(numbers).sum();
    }
}
