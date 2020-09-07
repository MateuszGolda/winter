package com.codecool.winter;

import java.io.IOException;

public class App {
    public static void main(String[] args) throws IOException, NoSuchMethodException {
        var winter = new Winter();
        winter.registerHandlers(ExampleHandler.class, HttpRequestHandler.class);
        winter.run();
    }
}
