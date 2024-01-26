package org.nekki.server.options;

import org.nekki.server.domain.ChoiceOption;
import org.nekki.server.domain.RockPaperScissorsResult;

public interface OptionRule {

    RockPaperScissorsResult won(ChoiceOption choiceOption);
}
