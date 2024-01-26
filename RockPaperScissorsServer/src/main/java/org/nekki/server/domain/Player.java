package org.nekki.server.domain;

import lombok.Data;

public @Data class Player {

    private Long id;
    private String username;

    private ChoiceOption choice;

}
