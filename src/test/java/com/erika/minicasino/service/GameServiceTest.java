package com.erika.minicasino.service;

import com.erika.minicasino.exception.BusinessException;
import com.erika.minicasino.model.Game;
import com.erika.minicasino.service.impl.GameServiceImpl;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class GameServiceTest {
    private GameService gameService;

    @BeforeEach
    void setUp() {
        gameService = new GameServiceImpl(); // resets internal map
    }

    @Test
    void testGetAllGames_ShouldReturnPreloadedGames() {
        List<Game> games = gameService.getAllGames();
        assertEquals(3, games.size());
    }

    @Test
    void testGetGameById_ValidId() {
        Game game = gameService.getGameById(1L);
        assertNotNull(game);
        assertEquals("Slot Machine", game.getName());
    }

    @Test
    void testGetGameById_InvalidId_ShouldThrowException() {
        assertThrows(BusinessException.class, () -> gameService.getGameById(999L));
    }

    @Test
    void testAddGame_ShouldAddAndRetrieveGame() {
        Game newGame = new Game(99L, "New Game", 0.8, 3.0, 50.0, 2.0);
        gameService.addGame(newGame);

        Game retrieved = gameService.getGameById(99L);
        assertEquals("New Game", retrieved.getName());
        assertEquals(0.8, retrieved.getChanceOfWinning());
    }

    @Test
    void testAddGame_OverridesExisting() {
        Game original = gameService.getGameById(1L);
        Game updated = new Game(1L, "Updated Game", 0.9, 4.0, 80.0, 1.0);

        gameService.addGame(updated);
        Game result = gameService.getGameById(1L);

        assertEquals("Updated Game", result.getName());
        assertEquals(0.9, result.getChanceOfWinning());
    }
}
