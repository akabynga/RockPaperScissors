package org.nekki.server.options.impl;

import org.nekki.server.domain.ChoiceOption;
import org.nekki.server.domain.RockPaperScissorsResult;
import org.nekki.server.options.OptionRule;

public class PaperOption implements OptionRule {

    @Override
    public RockPaperScissorsResult won(ChoiceOption choiceOption) {

        switch (choiceOption) {
            case PAPER -> {
                return RockPaperScissorsResult.DRAW;
            }
            case ROCK -> {
                return RockPaperScissorsResult.WIN;
            }
            case SCISSORS -> {
                return RockPaperScissorsResult.LOSE;
            }
            default -> // TODO Add message
                    throw new IllegalArgumentException("");
        }
    }
}
