package org.nekki.server.options.impl;

import org.nekki.server.domain.ChoiceOption;
import org.nekki.server.domain.RockPaperScissorsResult;
import org.nekki.server.options.OptionRule;

public class ScissorsOption implements OptionRule {

    @Override
    public RockPaperScissorsResult won(ChoiceOption choiceOption) {

        switch (choiceOption) {
            case PAPER -> {
                return RockPaperScissorsResult.WIN;
            }
            case ROCK -> {
                return RockPaperScissorsResult.LOSE;
            }
            case SCISSORS -> {
                return RockPaperScissorsResult.DRAW;
            }
            default -> // TODO Add message
                    throw new IllegalArgumentException("");
        }
    }
}
