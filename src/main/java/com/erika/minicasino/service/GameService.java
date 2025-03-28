package com.erika.minicasino.service;

import com.erika.minicasino.model.Game;

import java.util.List;

public interface GameService {
    List<Game> getAllGames();
    Game getGameById(Long id);

    Game addGame(Game newGame);

}
