package org.nekki.server;

import org.nekki.server.domain.ChoiceOption;
import org.nekki.server.domain.Player;
import org.nekki.utils.Console;
import org.nekki.utils.ResponseStatus;
import org.nekki.utils.StringUtil;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.*;

public class GameSession implements Runnable {
    private final Socket playerSocket1;
    private final Socket playerSocket2;

    private final ChoiceFactory choiceFactory = ChoiceFactory.getInstance();
    private final ExecutorService executorService = Executors.newFixedThreadPool(2);

    public GameSession(Socket playerSocket1, Socket playerSocket2) {
        this.playerSocket1 = playerSocket1;
        this.playerSocket2 = playerSocket2;
    }

    @Override
    public void run() {
        try (
                BufferedReader reader1 = new BufferedReader(new InputStreamReader(playerSocket1.getInputStream()));
                PrintWriter writer1 = new PrintWriter(playerSocket1.getOutputStream(), true);
                BufferedReader reader2 = new BufferedReader(new InputStreamReader(playerSocket2.getInputStream()));
                PrintWriter writer2 = new PrintWriter(playerSocket2.getOutputStream(), true)
        ) {
            Console.println("Game started");
            sendMessageToPlayers("All players connected", writer1, writer2);

            startGameRound(reader1, writer1, reader2, writer2);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void launchGame(Callable<Player> player1Story, Callable<Player> player2Story,
                            BufferedReader reader1, PrintWriter writer1, BufferedReader reader2, PrintWriter writer2) throws IOException {
        Future<Player> playerFuture1 = executorService.submit(player1Story);
        Future<Player> playerFuture2 = executorService.submit(player2Story);

        try {
            Player player1 = playerFuture1.get();
            Player player2 = playerFuture2.get();

            switch (choiceFactory.getRule(player1.getChoice()).won(player2.getChoice())) {
                case DRAW -> {
                    sendMessageToPlayers("Draw! Let's play again!", writer1, writer2);
                    startGameRound(player1, player2, reader1, writer1, reader2, writer2);
                }
                case WIN -> sendGameResult(writer1, player1.getChoice(), writer2, player2.getChoice());
                case LOSE -> sendGameResult(writer2, player2.getChoice(), writer1, player1.getChoice());
                default -> //TODO add message
                        throw new IllegalArgumentException();
            }

        } catch (InterruptedException | ExecutionException e) {
            // TODO handle it
            e.printStackTrace();
        }
    }

    private void startGameRound(Player prevPlayer1, Player prevPlayer2, BufferedReader reader1, PrintWriter writer1, BufferedReader reader2, PrintWriter writer2) throws IOException {
        launchGame(new PlayerStoryPath(prevPlayer1, reader1, writer1), new PlayerStoryPath(prevPlayer2,
                reader2, writer2), reader1, writer1, reader2, writer2);
    }

    private void startGameRound(BufferedReader reader1, PrintWriter writer1, BufferedReader reader2, PrintWriter writer2) throws IOException {
        launchGame(new PlayerStoryPath(reader1, writer1), new PlayerStoryPath(reader2, writer2),
                reader1, writer1, reader2, writer2);
    }

    private void sendGameResult(PrintWriter winnerWriter, ChoiceOption winnerOption, PrintWriter looserWriter, ChoiceOption looserOption) {
        String winnerChoice = StringUtil.capitalizeFirstLetter(winnerOption.name().toLowerCase());
        String looserChoice = StringUtil.capitalizeFirstLetter(looserOption.name().toLowerCase());
        sendMessageToPlayers("You won! :) " + winnerChoice + " beats " + looserChoice, winnerWriter);
        sendMessageToPlayers("You lost! :( " + winnerChoice + " beats " + looserChoice, looserWriter);
        sendMessageToPlayers(ResponseStatus.OK, winnerWriter, looserWriter);

    }


    private void sendMessageToPlayers(String message, PrintWriter... writers) {
        if (writers == null) {
            return;
        }
        for (PrintWriter printWriter : writers) {
            printWriter.println(message);
            System.out.println("Message send: " + message);
        }
    }
}