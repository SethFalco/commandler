package com.elypia.commandler.test.integration.impl.modules;

import com.elypia.commandler.Handler;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.*;

import java.util.stream.*;

@Module(name = "Array", aliases = "array", help = "Testing if adapters are parsing and using arrays correctly.")
public class ArrayModule implements Handler {

    @Command(name = "Collect Bools", aliases = "bools")
    public String collectBools(
        @Param(name = "bools", value = "A list of true/false.") boolean[] bools
    ) {
        int trueCount = 0;

        for (boolean bool : bools) {
            if (bool)
                trueCount++;
        }

        return String.format("%,d true, %,d false.", trueCount, bools.length - trueCount);
    }

    @Command(name = "Spell a Word", aliases = "spell")
    public String chars(
        @Param(name = "letters", value = "The letters that spell a word.") char[] chars
    ) {
        return new String(chars);
    }

    @Command(name = "Add Doubles", aliases = "doubles")
    public double doubles(
        @Param(name = "numbers", value = "A list of numbers to sum.") double[] numbers
    ) {
        return DoubleStream.of(numbers).sum();
    }

    @Command(name = "Add Floats", aliases = "floats")
    public long floats(
        @Param(name = "numbers", value = "A list of numbers to sum.") float[] numbers
    ) {
        int floats = 0;

        for (float in : numbers)
            floats += in;

        return floats;
    }

    @Command(name = "Add Longs", aliases = "longs")
    public long longs(
        @Param(name = "numbers", value = "A list of numbers to sum.") long[] numbers
    ) {
        return LongStream.of(numbers).sum();
    }

    @Command(name = "Add Shorts", aliases = "shorts")
    public long shorts(
        @Param(name = "numbers", value = "A list of numbers to sum.") short[] numbers
    ) {
        int shorts = 0;

        for (short in : numbers)
            shorts += in;

        return shorts;
    }

    @Command(name = "Add Bytes", aliases = "bytes")
    public long bytes(
        @Param(name = "numbers", value = "A list of numbers to sum.") byte[] numbers
    ) {
        int bytes = 0;

        for (byte in : numbers)
            bytes += in;

        return bytes;
    }

    @Command(name = "Add Ints", aliases = "sum", help = "I'll give you the total sum of a list of numbers.")
    public int sum(
        @Param(name = "numbers", value = "A list of numbers to sum.") int[] numbers
    ) {
        return sum(numbers, 1);
    }

    @Overload("Add Ints")
    public int sum(
        int[] numbers,
        @Param(name = "multiplier", value = "The muliplier to multiply the result by!") int multipler
    ) {
        return IntStream.of(numbers).sum() * multipler;
    }

    @Command(name = "Add Integers", aliases = "sumo", help = "I'll give you the total sum of a list of numbers!")
    public int sum(
        @Param(name = "numbers", value = "A list of numbers to sum.") Integer[] numbers
    ) {
        int total = 0;

        for (int i : numbers)
            total += i;

        return total;
    }
}
