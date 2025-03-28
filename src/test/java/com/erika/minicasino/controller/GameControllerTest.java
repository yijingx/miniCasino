package com.erika.minicasino.controller;

import com.erika.minicasino.model.Game;
import com.erika.minicasino.service.GameService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class GameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GameService gameService;

    @Autowired
    private ObjectMapper objectMapper;

    private Game testGame;

    @BeforeEach
    void setUp() {
        testGame = new Game(1L, "Roulette", 0.5, 2.0, 100.0, 5.0);
    }

    @Test
    void listGames_ShouldReturnListOfGames() throws Exception {
        List<Game> games = Arrays.asList(
                new Game(1L, "Roulette", 0.5, 2.0, 100.0, 5.0),
                new Game(2L, "Blackjack", 0.4, 3.0, 200.0, 10.0)
        );

        Mockito.when(gameService.getAllGames()).thenReturn(games);

        mockMvc.perform(get("/game"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.data[0].name").value("Roulette"));
    }

    @Test
    void getGame_ShouldReturnSpecificGame() throws Exception {
        Mockito.when(gameService.getGameById(1L)).thenReturn(testGame);

        mockMvc.perform(get("/game/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.name").value("Roulette"))
                .andExpect(jsonPath("$.data.chanceOfWinning").value(0.5));
    }

    @Test
    void addGame_ShouldReturnCreatedGame() throws Exception {
        Mockito.when(gameService.addGame(Mockito.any(Game.class))).thenReturn(testGame);

        mockMvc.perform(post("/game/games")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testGame)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.name").value("Roulette"));
    }
}