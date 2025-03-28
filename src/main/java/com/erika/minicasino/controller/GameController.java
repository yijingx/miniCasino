package com.erika.minicasino.controller;

import com.erika.minicasino.common.BaseResponse;
import com.erika.minicasino.common.ResultUtils;
import com.erika.minicasino.model.Game;
import com.erika.minicasino.service.GameService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/game")
public class GameController {
    @Autowired
    private GameService gameService;

    @Operation(summary = "Get list of all available games")
    @ApiResponse(responseCode = "200", description = "List of games returned successfully")
    @GetMapping
    public BaseResponse<List<Game>> listGames() {
        List<Game> gameList = gameService.getAllGames();
        return ResultUtils.success(gameList);
    }

    @Operation(summary = "Get a specific game by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Game found",
                    content = @Content(schema = @Schema(implementation = Game.class))),
            @ApiResponse(responseCode = "40001", description = "Game not found",
                    content = @Content)
    })
    @GetMapping("/{id}")
    public BaseResponse<Game> getGame(@PathVariable Long id) {
        Game game = gameService.getGameById(id);
        return ResultUtils.success(game);
    }

    @Operation(summary = "Add a new game to the list")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Game added successfully",
                    content = @Content(schema = @Schema(implementation = Game.class)))
    })
    @PostMapping("/games")
    public BaseResponse<Game> addGame(@RequestBody Game newGame) {
        Game game = gameService.addGame(newGame);
        return ResultUtils.success(game);
    }
}
