package org.nekki.server;

import org.nekki.server.domain.ChoiceOption;
import org.nekki.server.domain.Player;
import org.nekki.utils.Console;
import org.nekki.utils.ResponseStatus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.Callable;

public class PlayerStoryPath implements Callable<Player> {

    private final BufferedReader reader;
    private final PrintWriter writer;
    private final Player player;

    public PlayerStoryPath(BufferedReader reader, PrintWriter writer) {
        this.reader = reader;
        this.writer = writer;
        this.player = new Player();
        this.player.setId(Thread.currentThread().getId());
    }

    public PlayerStoryPath(Player player, BufferedReader reader, PrintWriter writer) {
        this.reader = reader;
        this.writer = writer;
        this.player = player;
    }

    @Override
    public Player call() throws Exception {

        if (player.getUsername() == null) {
            writer.println("Enter your name: ");
            player.setUsername(reader.readLine());
            writer.println("Hello " + player.getUsername() + "!");
        }
        player.setChoice(receiveChoice());

        Console.println(player.getUsername() + " chose " + player.getChoice().name().toLowerCase());
        writer.println("Your choice: " + player.getChoice().name().toLowerCase() + ". Waiting your opponent!");

        return player;
    }

    private ChoiceOption receiveChoice() throws IOException {
        String choice = null;
        writer.println("The battle has begun!");

        while (choice == null || !ChoiceOption.contains(choice.toUpperCase())) {
            writer.println("Choice your weapon: Rock, Paper, Scissors!");
            choice = reader.readLine();
            if (ChoiceOption.contains(choice.toUpperCase())) {
                writer.println(ResponseStatus.OK);
                return ChoiceOption.valueOf(choice.toUpperCase());
            } else {
                writer.println(ResponseStatus.BAD_REQUEST);
            }
        }
        return ChoiceOption.valueOf(choice.toUpperCase());
    }
}
