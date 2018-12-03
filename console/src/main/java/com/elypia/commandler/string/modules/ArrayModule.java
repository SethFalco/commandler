package com.elypia.commandler.string.modules;

import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.string.StringHandler;

import java.util.stream.*;

@Module(name = "Array", aliases = "array", help = "The array module!")
public class ArrayModule extends StringHandler {

    private static final String BOOL_COUNT = "%,d true, %,d false";

    @Command(name = "Boolean List", aliases = "boolsum")
    @Param(name = "bools", help = "A list of true/false.")
    public String bools(boolean[] bools) {
        int trueCount = 0;

        for (boolean bool : bools) {
            if (bool)
                trueCount++;
        }

        return String.format(BOOL_COUNT, trueCount, bools.length - trueCount);
    }

    @Command(name = "Char List", aliases = "chars")
    @Param(name = "chars", help = "A list of chars.")
    public String chars(char[] chars) {
        return new String(chars);
    }

    @Command(name = "Add Doubles", aliases = "doubles")
    @Param(name = "numbers", help = "A list of numbers!")
    public String doubles(double[] numbers) {
        return String.format("%,.0f", DoubleStream.of(numbers).sum());
    }

    @Command(name = "Add Floats", aliases = "floats")
    @Param(name = "numbers", help = "A list of numbers!")
    public long floats(float[] numbers) {
        int floats = 0;

        for (float in : numbers)
            floats += in;

        return floats;
    }

    @Command(name = "Add Longs", aliases = "longs")
    @Param(name = "numbers", help = "A list of numbers!")
    public long longs(long[] numbers) {
        return LongStream.of(numbers).sum();
    }

    @Command(name = "Add Shorts", aliases = "shorts")
    @Param(name = "numbers", help = "A list of numbers!")
    public long shorts(short[] numbers) {
        int shorts = 0;

        for (short in : numbers)
            shorts += in;

        return shorts;
    }

    @Command(name = "Add Bytes", aliases = "bytes")
    @Param(name = "numbers", help = "A list of numbers!")
    public long bytes(byte[] numbers) {
        int bytes = 0;

        for (byte in : numbers)
            bytes += in;

        return bytes;
    }

    // ? Below we're just doing generic tests with integers

    @Command(id = 1, name = "Add Numbers", aliases = "sum", help = "I'll give you the total sum of a list of numbers!")
    @Param(name = "numbers", help = "A list of numbers!")
    public int sum(int[] numbers) {
        return sum(numbers, 1);
    }

    @Overload(1)
    @Param(name = "multiplier", help = "The muliplier to multiply the result by!")
    public int sum(int[] numbers, int multipler) {
        return IntStream.of(numbers).sum() * multipler;
    }

    @Command(name = "Add Numbers", aliases = "sumo", help = "I'll give you the total sum of a list of numbers!")
    @Param(name = "numbers", help = "A list of numbers!")
    public int sum(Integer[] numbers) {
        int total = 0;

        for (int i : numbers)
            total += i;

        return total;
    }
}
