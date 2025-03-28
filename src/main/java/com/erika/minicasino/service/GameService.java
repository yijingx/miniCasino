package com.erika.minicasino.service;

import com.erika.minicasino.model.Game;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

public interface GameService {
    List<Game> getAllGames();
    Game getGameById(Long id);

    Game addGame(Game newGame);

    Map<Long, Game> loadFromXml(InputStream xml);
}
