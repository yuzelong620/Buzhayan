package com.kratos.game.herphone.game.chose.message;

import lombok.Data;

import java.util.Map;

@Data
public class ResGameProgress {
    private Map<Integer, Long> progress;
}
