package bg.sofia.uni.fmi.mjt.cocktail.server;

import bg.sofia.uni.fmi.mjt.cocktail.server.command.CommandHandler;
import bg.sofia.uni.fmi.mjt.cocktail.server.storage.DefaultCocktailStorage;

public class Main {
    public static void main(String[] args) {
        final int port = 6666;
        Server server = new Server(port, new CommandHandler(new DefaultCocktailStorage()));

        server.start();
    }
}