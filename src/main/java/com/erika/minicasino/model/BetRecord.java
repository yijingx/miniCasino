package com.erika.minicasino.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BetRecord {
    private Long gameId;
    private double amountBet;
    private double amountWon;
    private boolean win;
}
