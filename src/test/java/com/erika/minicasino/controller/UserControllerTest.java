package com.erika.minicasino.controller;

import com.erika.minicasino.common.ErrorCode;
import com.erika.minicasino.controller.UserController;
import com.erika.minicasino.exception.BusinessException;
import com.erika.minicasino.model.BetRecord;
import com.erika.minicasino.model.User;
import com.erika.minicasino.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private ObjectMapper objectMapper;
    private User testUser;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        testUser = new User("erika", "erika123", LocalDate.of(2000, 1, 1));
        // Set up ObjectMapper with JavaTimeModule
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Test
    void registerUser_ShouldReturnSuccess() throws Exception {
        // userService.registerUser(User) is a void method
        Mockito.when(userService.registerUser(Mockito.any(User.class)))
                .thenReturn(testUser);

        mockMvc.perform(post("/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("ok"))
                .andExpect(jsonPath("$.data.name").value("erika"))
                .andExpect(jsonPath("$.data.username").value("erika123"))
                .andExpect(jsonPath("$.data.birthdate").value("2000-01-01"));
    }

    @Test
    void registerUser_DuplicateUser() throws Exception {
        doThrow(new BusinessException(ErrorCode.DUPLICATE_DATA, "User already exists"))
                .when(userService).registerUser(Mockito.any(User.class));

        mockMvc.perform(post("/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isOk()) // still 200 OK due to handler
                .andExpect(jsonPath("$.code").value(40100))
                .andExpect(jsonPath("$.message").value("Data already exists"))
                .andExpect(jsonPath("$.description").value("User already exists"))
                .andExpect(jsonPath("$.data").doesNotExist());
    }

    @Test
    void deposit_ShouldReturnSuccessWrappedInBaseResponse() throws Exception {
        String username = "erika123";
        double amount = 100.0;

        Mockito.doNothing().when(userService).deposit(username, amount);

        mockMvc.perform(post("/user/deposit")
                        .param("username", username)
                        .param("amount", String.valueOf(amount)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("ok"))
                .andExpect(jsonPath("$.data").value("Deposit successful!"));
    }

    @Test
    void getBalance_ShouldReturnUserBalance() throws Exception {
        String username = "erika123";
        User mockUser = new User("erika", username, LocalDate.of(2000, 1, 1));
        mockUser.setBalance(150.0);

        Mockito.when(userService.getUser(username)).thenReturn(mockUser);

        mockMvc.perform(get("/user/balance")
                        .param("username", username))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").value(150.0));
    }

    @Test
    void placeBet_ShouldReturnBetRecord() throws Exception {
        String username = "erika123";
        long gameId = 1L;
        double betAmount = 10.0;

        // Create a mock BetRecord to return
        BetRecord mockBet = new BetRecord(gameId, betAmount, 20.0, true);

        Mockito.when(userService.placeBet(username, gameId, betAmount)).thenReturn(mockBet);

        mockMvc.perform(post("/user/placeBet")
                        .param("username", username)
                        .param("gameId", String.valueOf(gameId))
                        .param("betAmount", String.valueOf(betAmount)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.gameId").value((int) gameId))
                .andExpect(jsonPath("$.data.amountBet").value(betAmount))
                .andExpect(jsonPath("$.data.amountWon").value(20.0))
                .andExpect(jsonPath("$.data.win").value(true));
    }
}