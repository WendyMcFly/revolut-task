package io.dbulanova.revolut;

import org.jooby.Jooby;

public class App extends Jooby {

    {
        get("/", () -> "Hello World!");
    }

    public static void main(final String[] args) {
        run(App::new, args);
    }
}
