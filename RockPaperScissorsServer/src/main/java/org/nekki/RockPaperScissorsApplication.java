package org.nekki;

import org.nekki.server.GameSession;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RockPaperScissorsApplication {

    private static final int PORT = 8888;
    private static final ExecutorService executor = Executors.newFixedThreadPool(2);

    public static void main(String[] args) {

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server running, waiting connections...");

            while (true) {
                Socket player1 = serverSocket.accept();
                System.out.println("Connected: " + player1.getLocalAddress());
                Socket player2 = serverSocket.accept();
                System.out.println("Connected: " + player2.getLocalAddress());

                System.out.println("Players connected. Starting game session.");
                executor.submit(new GameSession(player1, player2));
            }
        } catch (IOException e) {
            executor.shutdownNow();
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }
    }
}