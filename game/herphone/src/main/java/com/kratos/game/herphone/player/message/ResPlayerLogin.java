package com.kratos.game.herphone.player.message;

import lombok.Data;

@Data
public class ResPlayerLogin {
    private boolean create;
    private String token;
    private ResPlayerProfile player;
}
