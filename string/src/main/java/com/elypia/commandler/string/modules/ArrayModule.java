package com.elypia.commandler.string.modules;

import com.elypia.commandler.annotations.*;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.string.StringHandler;

import java.util.stream.IntStream;

@Module(name = "Array", aliases = "array", help = "The array module!")
public class ArrayModule extends StringHandler {

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