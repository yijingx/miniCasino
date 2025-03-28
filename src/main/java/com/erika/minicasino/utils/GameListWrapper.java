package com.erika.minicasino.utils;

import com.erika.minicasino.model.Game;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlRootElement(name = "games")
public class GameListWrapper {

    private List<Game> games;

    @XmlElement(name = "game")
    public List<Game> getGames() {
        return games;
    }

    public void setGames(List<Game> games) {
        this.games = games;
    }
}