package org.nekki;

import org.nekki.utils.ResponseStatus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class RockPaperScissorsClient {

    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 8888);
             BufferedReader serverReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter serverWriter = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in))) {

            if (socket.isConnected()) {
                System.out.println("Connection established");
            } else {
                System.out.println("Connection not established");
            }
            // All players connected
            System.out.println(serverReader.readLine());
            // enter your name
            System.out.println(serverReader.readLine());
            String playerName = consoleReader.readLine();
            serverWriter.println(playerName);

            System.out.println("Waiting another player...");
            // Hello
            System.out.println(serverReader.readLine());
            //The battle has begun!
            System.out.println(serverReader.readLine());

            String summaryCode = null;
            while (!ResponseStatus.OK.equals(summaryCode)) {
                summaryCode = playGame(serverReader, serverWriter, consoleReader);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String playGame(BufferedReader serverReader, PrintWriter serverWriter, BufferedReader consoleReader) throws IOException {
        String response = null;
        while (!ResponseStatus.OK.equals(response)) {
            // Choice your weapon: Rock, Paper, Scissors!
            if (ResponseStatus.BAD_REQUEST.equals(response)) {
                System.out.println("Wrong choice!");
            }
            System.out.println(serverReader.readLine());
            String choice = consoleReader.readLine();
            serverWriter.println(choice);
            response = serverReader.readLine();
        }
        // your choice...
        String yourChoice = serverReader.readLine();
        String result = serverReader.readLine();
        System.out.println(yourChoice);
        System.out.println(result);
        return serverReader.readLine();
    }
}