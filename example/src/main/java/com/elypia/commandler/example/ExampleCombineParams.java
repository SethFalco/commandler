package com.elypia.commandler.example;

import com.elypia.commandler.annotations.Param;
import com.elypia.commandler.interfaces.CommandParams;

public class ExampleCombineParams implements CommandParams {

    @Param()
    private String one;

    @Param(defaultValue = "")
    private String two;

    @Param(defaultValue = "")
    private String three;

    public String getOne() {
        return one;
    }

    public String getTwo() {
        return two;
    }

    public String getThree() {
        return three;
    }

    @Override
    public String[] getParams() {
        return new String[] {"one", "two", "three"};
    }
}
