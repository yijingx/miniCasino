package com.erika.minicasino.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BetSummery {
    private int numberOfBets;
    private double totalBet;
    private double totalWon;
}
