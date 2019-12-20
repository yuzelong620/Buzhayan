package com.kratos.game.herphone.player.message;

import lombok.Data;

import java.util.List;

@Data
public class ResGoodName {
    private List<Name> nameList;

    @Data
    public static class Name {
        private Integer score;
        private String xing;
        private String ming;
        private String bihua;
    }
}
