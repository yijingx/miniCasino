package com.erika.minicasino.service.impl;

import com.erika.minicasino.common.ErrorCode;
import com.erika.minicasino.exception.BusinessException;
import com.erika.minicasino.model.Game;
import com.erika.minicasino.model.User;
import com.erika.minicasino.service.GameService;
import com.erika.minicasino.service.UserService;
import com.erika.minicasino.utils.GameListWrapper;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.*;

@Service
public class GameServiceImpl implements GameService {
    private Map<Long, Game> gameMap = new HashMap<>();
    private final List<Game> gameHistory = new ArrayList<>();
    public GameServiceImpl() {
        try {
            // Try loading from XML file in resources
            InputStream xml = getClass().getClassLoader().getResourceAsStream("games.xml");
            if (xml != null) {
                System.out.println("games.xml found in resources — loading games from file...");
                loadFromXml(xml);
            } else {
                System.out.println("games.xml not found. Loading default in-memory games...");
                initializeGames();
            }
        } catch (Exception e) {
            System.out.println("Failed to load games from XML. Falling back to default games.");
            e.printStackTrace();
            initializeGames();
        }
    }

    private void initializeGames() {
        gameMap.put(1L, new Game(1L, "Slot Machine", 0.2, 5.0, 1.0, 100.0));
        gameMap.put(2L, new Game(2L, "Roulette", 0.4, 2.5, 2.0, 50.0));
        gameMap.put(3L, new Game(3L, "Blackjack", 0.5, 2.0, 5.0, 200.0));
    }

    private void loadFromXml(InputStream xml) throws Exception {
        JAXBContext context = JAXBContext.newInstance(GameListWrapper.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        GameListWrapper wrapper = (GameListWrapper) unmarshaller.unmarshal(xml);
        for (Game game : wrapper.getGames()) {
            gameMap.put(game.getId(), game);
        }
        System.out.println("Loaded " + wrapper.getGames().size() + " games from XML.");
    }

    @Override
    public List<Game> getAllGames() {
        return new ArrayList<>(gameMap.values());
    }

    @Override
    public Game getGameById(Long id) {
        Game game = gameMap.get(id);
        if (game == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "Game with ID " + id + " not found");
        }
        return game;
    }

    @Override
    public Game addGame(Game game) {
        gameMap.put(game.getId(), game);
        return game;
    }
}
