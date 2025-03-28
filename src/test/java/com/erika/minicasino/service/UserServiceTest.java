package com.erika.minicasino.service;

import com.erika.minicasino.exception.BusinessException;
import com.erika.minicasino.model.BetRecord;
import com.erika.minicasino.model.Game;
import com.erika.minicasino.model.User;
import com.erika.minicasino.service.GameService;
import com.erika.minicasino.service.UserService;
import com.erika.minicasino.service.impl.UserServiceImpl;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceTest {
    private UserService userService;
    private GameService gameService;

    @BeforeEach
    void setUp() {
        gameService = mock(GameService.class);
        userService = new UserServiceImpl(gameService);
    }

    @Test
    void testRegister_UserSuccessfully() {
        User user = new User("Bo", "bo123", LocalDate.of(2000, 1, 1));
        User result = userService.registerUser(user);

        assertEquals("bo123", result.getUsername());
        assertEquals(1, userService.getAllUsers().size());
    }

    @Test
    void testRegister_UserNoUsername() {
        User user = new User("Bo", "", LocalDate.of(2000, 1, 1));
//        User result = userService.registerUser(user);

        assertThrows(BusinessException.class, () -> userService.registerUser(user));
    }

    @Test
    void testRegister_UserTooYoung() {
        User underageUser = new User("Young", "kiddo", LocalDate.now().minusYears(17));
        assertThrows(BusinessException.class, () -> userService.registerUser(underageUser));
    }

    @Test
    void testDeposit() {
        User user = new User("Bo", "bo123", LocalDate.of(2000, 1, 1));
        userService.registerUser(user);
        userService.deposit("bo123", 100.0);

        assertEquals(100.0, userService.getUser("bo123").getBalance());
    }

    @Test
    void testDeposit_WithNegativeAmount() {
        User user = new User("Bo", "bo123", LocalDate.of(2000, 1, 1));
        userService.registerUser(user);

        assertThrows(BusinessException.class, () -> userService.deposit("bo123", -10));
    }

    @Test
    void testPlaceBet_WinScenario() {
        // Arrange: Create a game with 100% win chance
        Game testGame = new Game(4L, "Lucky Spin", 1.0, 2.0, 5.0, 100.0);
        when(gameService.getGameById(4L)).thenReturn(testGame);

        // Register a user with 100 balance
        User user = new User("Bo", "bo123", LocalDate.of(2000, 1, 1), 100.0);
        userService.registerUser(user);

        // Act: Place a 10 EUR bet
        BetRecord result = userService.placeBet("bo123", 4L, 10.0);

        // Assert
        assertTrue(result.isWin());
        assertEquals(20.0, result.getAmountWon());
        assertEquals(110.0, user.getBalance()); // balance + winnings
        assertEquals(1, user.getBetHistory().size());
        BetRecord savedBet = user.getBetHistory().get(0);
        assertEquals(10.0, savedBet.getAmountBet());
        assertEquals(20.0, savedBet.getAmountWon());
        assertTrue(savedBet.isWin());
    }

    @Test
    void testPlaceBet_LoseScenario() {
        // Arrange: Create a game with 0% win chance
        Game testGame = new Game(4L, "Lucky Spin", 0.0, 2.0, 5.0, 100.0);
        when(gameService.getGameById(4L)).thenReturn(testGame);

        // Register a user with 100 balance
        User user = new User("Bo", "bo123", LocalDate.of(2000, 1, 1), 100.0);
        userService.registerUser(user);

        // Act: Place a 10 EUR bet
        BetRecord result = userService.placeBet("bo123", 4L, 10.0);

        // Assert
        assertFalse(result.isWin());
        assertEquals(0.0, result.getAmountWon());
        assertEquals(90.0, user.getBalance());
    }

    @Test
    void testPlaceBetInvalidAmount() {
        User user = new User("Bo", "bo123", LocalDate.of(2000, 1, 1), 100);
        userService.registerUser(user);
        Game game = new Game(4L, "Game", 1.0, 2.0, 1, 100);
        when(gameService.getGameById(4L)).thenReturn(game);

        assertThrows(BusinessException.class,
                () -> userService.placeBet("bo123", 1L, 0));
    }

}
