package com.erika.minicasino.model;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "game")
public class Game {
    private Long id;
    private String name;
    private double chanceOfWinning;
    private double winningMultiplier;
    private double minBet;
    private double maxBet;

    @XmlElement
    public Long getId() {
        return id;
    }

    @XmlElement
    public String getName() {
        return name;
    }

    @XmlElement
    public double getChanceOfWinning() {
        return chanceOfWinning;
    }

    @XmlElement
    public double getWinningMultiplier() {
        return winningMultiplier;
    }

    @XmlElement
    public double getMinBet() {
        return minBet;
    }

    @XmlElement
    public double getMaxBet() {
        return maxBet;
    }
}
