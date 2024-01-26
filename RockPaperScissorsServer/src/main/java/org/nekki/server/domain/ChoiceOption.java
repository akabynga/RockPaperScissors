package org.nekki.server.domain;

public enum ChoiceOption {

    ROCK, PAPER, SCISSORS;


    public static boolean contains(String test) {
        for (ChoiceOption c : ChoiceOption.values()) {
            if (c.name().equals(test)) {
                return true;
            }
        }
        return false;
    }
}
