
package com.erika.minicasino.controller;

import com.erika.minicasino.common.BaseResponse;
import com.erika.minicasino.common.ResultUtils;
import com.erika.minicasino.model.BetRecord;
import com.erika.minicasino.model.BetSummery;
import com.erika.minicasino.model.User;
import com.erika.minicasino.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/user")
@Tag(name = "User Controller", description = "Operations related to users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @Operation(summary = "Get all users")
    @ApiResponse(responseCode = "200", description = "List of users",
            content = @Content(schema = @Schema(implementation = User.class)))
    @GetMapping
    public BaseResponse<List<User>> getAllUsers() {
        List<User> result = userService.getAllUsers();
        return ResultUtils.success(result);
    }

    @Operation(summary = "Register a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User registered successfully"),
            @ApiResponse(responseCode = "40000", description = "Invalid input"),
            @ApiResponse(responseCode = "40001", description = "Duplicate data"),
            @ApiResponse(responseCode = "40101", description = "No Authorization"),
    })
    @PostMapping("/register")
    public BaseResponse<User> registerUser(@Valid @RequestBody User user) {
        userService.registerUser(user);
        return ResultUtils.success(user);
    }

    @Operation(summary = "Get user by username")
    @GetMapping("/{username}")
    public BaseResponse<User> getUser(@PathVariable String username) {
        User user = userService.getUser(username);
        return ResultUtils.success(user);
    }

    @Operation(summary = "Deposit money into user balance")
    @PostMapping("/deposit")
    public BaseResponse<String> deposit(@RequestParam String username, @RequestParam double amount) {
        userService.deposit(username, amount);
        return ResultUtils.success("Deposit successful!");
    }

    @Operation(summary = "Get the balance of a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Balance retrieved successfully"),
            @ApiResponse(responseCode = "40001", description = "User not found")
    })
    @GetMapping("/balance")
    public BaseResponse<Double> getBalance(@RequestParam String username) {
        double balance = userService.getUser(username).getBalance();
        return ResultUtils.success(balance);
    }

    @Operation(summary = "Place a bet")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Bet placed successfully",
                    content = @Content(schema = @Schema(implementation = BetRecord.class))),
            @ApiResponse(responseCode = "40000", description = "Invalid bet parameters (e.g., negative amount or out-of-range)",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "40001", description = "Game not found",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "40101", description = "User not found",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "50000", description = "Internal error or insufficient balance",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @PostMapping("/placeBet")
    public BaseResponse<BetRecord> placeBet(@Parameter(description = "Username of the user") @RequestParam String username,
                                            @Parameter(description = "ID of the game") @RequestParam long gameId,
                                            @Parameter(description = "Amount to bet") @RequestParam double betAmount) throws InterruptedException {
        BetRecord betRecord = userService.placeBet(username, gameId, betAmount);
        return ResultUtils.success(betRecord);
    }

    @Operation(summary = "Get summary of user's betting history")
    @GetMapping("/betSummary")
    public BaseResponse<BetSummery> getSummary(@RequestParam String userName) {
        BetSummery betSummery = userService.getBetSummary(userName);
        return ResultUtils.success(betSummery);
    }
}