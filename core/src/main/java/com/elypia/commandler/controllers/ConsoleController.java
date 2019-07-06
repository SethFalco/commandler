package com.elypia.commandler.controllers;

import com.elypia.commandler.interfaces.Controller;
import com.elypia.commandler.managers.DispatcherManager;

import java.util.Scanner;
import java.util.concurrent.Executors;

public class ConsoleController implements Controller {

    public ConsoleController(DispatcherManager dispatcher) {
        Scanner scanner = new Scanner(System.in);

        Executors.newSingleThreadExecutor().submit(() -> {
            String nextLine;

            while ((nextLine = scanner.nextLine()) != null)
                System.out.println(dispatcher.dispatch(this, nextLine, nextLine));
        });
    }

    @Override
    public Class<?> getMessageType() {
        return String.class;
    }
}
