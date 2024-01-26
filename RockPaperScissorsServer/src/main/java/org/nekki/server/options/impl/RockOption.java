package org.nekki.server.options.impl;

import org.nekki.server.domain.ChoiceOption;
import org.nekki.server.domain.RockPaperScissorsResult;
import org.nekki.server.options.OptionRule;

public class RockOption implements OptionRule {

    @Override
    public RockPaperScissorsResult won(ChoiceOption choiceOption) {

        switch (choiceOption) {
            case PAPER -> {
                return RockPaperScissorsResult.LOSE;
            }
            case ROCK -> {
                return RockPaperScissorsResult.DRAW;
            }
            case SCISSORS -> {
                return RockPaperScissorsResult.WIN;
            }
            default -> // TODO Add message
                    throw new IllegalArgumentException("");
        }
    }
}
