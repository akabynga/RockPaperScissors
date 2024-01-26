package org.nekki.server;

import org.nekki.server.domain.ChoiceOption;
import org.nekki.server.options.OptionRule;
import org.nekki.server.options.impl.PaperOption;
import org.nekki.server.options.impl.RockOption;
import org.nekki.server.options.impl.ScissorsOption;

import java.util.HashMap;
import java.util.Map;

public class ChoiceFactory {

    private volatile static ChoiceFactory instance;
    private final Map<ChoiceOption, OptionRule> optionsMapping = new HashMap<>();


    private ChoiceFactory() {
        optionsMapping.put(ChoiceOption.PAPER, new PaperOption());
        optionsMapping.put(ChoiceOption.ROCK, new RockOption());
        optionsMapping.put(ChoiceOption.SCISSORS, new ScissorsOption());
    }

    public static ChoiceFactory getInstance() {
        if (instance == null) {
            synchronized (ChoiceFactory.class) {
                if (instance == null) {
                    instance = new ChoiceFactory();
                }
            }
        }
        return instance;
    }

    public OptionRule getRule(ChoiceOption choiceOption) {

        if (!optionsMapping.containsKey(choiceOption)) {
            //TODO option not supported
            throw new IllegalArgumentException();
        }
        return optionsMapping.get(choiceOption);
    }


}
