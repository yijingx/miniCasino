package com.erika.minicasino.service;

import com.erika.minicasino.exception.BusinessException;
import com.erika.minicasino.model.Game;
import com.erika.minicasino.service.impl.GameServiceImpl;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
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

    @Test
    void testLoadFromXml_ShouldParseAndReturnGames() throws Exception {
        String xmlContent = """
            <games>
                <game>
                    <id>1</id>
                    <name>Slot Machine</name>
                    <chanceOfWinning>0.2</chanceOfWinning>
                    <winningMultiplier>5.0</winningMultiplier>
                    <minBet>1.0</minBet>
                    <maxBet>100.0</maxBet>
                </game>
                <game>
                    <id>2</id>
                    <name>Roulette</name>
                    <chanceOfWinning>0.4</chanceOfWinning>
                    <winningMultiplier>2.5</winningMultiplier>
                    <minBet>2.0</minBet>
                    <maxBet>50.0</maxBet>
                </game>
            </games>
        """;

        InputStream xmlStream = new ByteArrayInputStream(xmlContent.getBytes(StandardCharsets.UTF_8));
        Map<Long, Game> result = gameService.loadFromXml(xmlStream);

        assertEquals(2, result.size());

        Game game1 = result.get(1L);
        assertNotNull(game1);
        assertEquals("Slot Machine", game1.getName());
        assertEquals(0.2, game1.getChanceOfWinning());
        assertEquals(5.0, game1.getWinningMultiplier());

        Game game2 = result.get(2L);
        assertNotNull(game2);
        assertEquals("Roulette", game2.getName());
    }
}
