package com.elypia.commandler.controllers;

import com.elypia.commandler.CommandlerEvent;
import com.elypia.commandler.interfaces.ServiceController;

import java.util.Scanner;

public class ConsoleController implements ServiceController<String, String> {

    public ConsoleController() {
        Scanner scanner = new Scanner(System.in);
        String nextLine;

        while ((nextLine = scanner.nextLine()) != null)
            receive(nextLine);
    }

    @Override
    public String receive(String event) {
        return event;
    }

    @Override
    public void send(CommandlerEvent<String> event, String message) {
        System.out.println(message);
    }

    @Override
    public Class<?>[] getMessageTypes() {
        return new Class<?>[] {String.class};
    }
}
