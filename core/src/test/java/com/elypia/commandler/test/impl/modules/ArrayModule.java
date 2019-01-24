package com.elypia.commandler.test.impl.modules;

import com.elypia.commandler.*;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.metadata.ModuleData;

import java.util.stream.*;

@Module(id = "Array", aliases = "array", help = "Testing if parsers are parsing and using arrays correctly.")
public class ArrayModule extends Handler<String, String> {

    /**
     * Initialise the module, this will assign the values
     * in the module and create a {@link ModuleData} which is
     * what {@link Commandler} uses in runtime to identify modules,
     * commands or obtain any static data.
     *
     * @param commandler Our parent Commandler class.
     * @return Returns if the {@link #test()} for this module passed.
     */
    public ArrayModule(Commandler<String, String> commandler) {
        super(commandler);
    }

    @Command(id = "Collect Bools", aliases = "bools")
    public String collectBools(
        @Param(id = "bools", help = "A list of true/false.") boolean[] bools
    ) {
        int trueCount = 0;

        for (boolean bool : bools) {
            if (bool)
                trueCount++;
        }

        return String.format("%,d true, %,d false.", trueCount, bools.length - trueCount);
    }

    @Command(id = "Spell a Word", aliases = "spell")
    public String chars(
        @Param(id = "letters", help = "The letters that spell a word.") char[] chars
    ) {
        return new String(chars);
    }

    @Command(id = "Add Doubles", aliases = "doubles")
    public double doubles(
        @Param(id = "numbers", help = "A list of numbers to sum.") double[] numbers
    ) {
        return DoubleStream.of(numbers).sum();
    }

    @Command(id = "Add Floats", aliases = "floats")
    public long floats(
        @Param(id = "numbers", help = "A list of numbers to sum.") float[] numbers
    ) {
        int floats = 0;

        for (float in : numbers)
            floats += in;

        return floats;
    }

    @Command(id = "Add Longs", aliases = "longs")
    public long longs(
        @Param(id = "numbers", help = "A list of numbers to sum.") long[] numbers
    ) {
        return LongStream.of(numbers).sum();
    }

    @Command(id = "Add Shorts", aliases = "shorts")
    public long shorts(
        @Param(id = "numbers", help = "A list of numbers to sum.") short[] numbers
    ) {
        int shorts = 0;

        for (short in : numbers)
            shorts += in;

        return shorts;
    }

    @Command(id = "Add Bytes", aliases = "bytes")
    public long bytes(
        @Param(id = "numbers", help = "A list of numbers to sum.") byte[] numbers
    ) {
        int bytes = 0;

        for (byte in : numbers)
            bytes += in;

        return bytes;
    }

    @Command(id = "Add Ints", aliases = "sum", help = "I'll give you the total sum of a list of numbers.")
    public int sum(
        @Param(id = "numbers", help = "A list of numbers to sum.") int[] numbers
    ) {
        return sum(numbers, 1);
    }

    @Overload("Add Ints")
    public int sum(
        int[] numbers,
        @Param(id = "multiplier", help = "The muliplier to multiply the result by!") int multipler
    ) {
        return IntStream.of(numbers).sum() * multipler;
    }

    @Command(id = "Add Integers", aliases = "sumo", help = "I'll give you the total sum of a list of numbers!")
    public int sum(
        @Param(id = "numbers", help = "A list of numbers to sum.") Integer[] numbers
    ) {
        int total = 0;

        for (int i : numbers)
            total += i;

        return total;
    }
}
