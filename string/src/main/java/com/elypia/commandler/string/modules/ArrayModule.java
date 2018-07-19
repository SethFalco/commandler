package com.elypia.commandler.string.modules;

import com.elypia.commandler.annotations.*;
import com.elypia.commandler.string.StringHandler;

import java.util.Arrays;
import java.util.stream.IntStream;

@Module(name = "Array", aliases = "array", help = "The array module!")
public class ArrayModule extends StringHandler {

    @Command(name = "Add Numbers", aliases = "sum", help = "I'll give you the total sum of a list of numbers!")
    @Param(name = "numbers", help = "A list of numbers!")
    public int say(int[] numbers) {
        return IntStream.of(numbers).sum();
    }

    @Command(name = "Add Numbers", aliases = "sumo", help = "I'll give you the total sum of a list of numbers!")
    @Param(name = "numbers", help = "A list of numbers!")
    public int say(Integer[] numbers) {
        int total = 0;

        for (int i : numbers)
            total += i;

        return total;
    }
}