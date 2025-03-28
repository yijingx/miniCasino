package com.erika.minicasino.controller;

import com.erika.minicasino.common.BaseResponse;
import com.erika.minicasino.common.ErrorCode;
import com.erika.minicasino.common.ResultUtils;
import com.erika.minicasino.model.Game;
import com.erika.minicasino.service.GameService;
import com.erika.minicasino.utils.GameListWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/game")
@Tag(name = "Game Controller", description = "Operations related to games")
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

    @Operation(summary = "Upload list of games from an XML file")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Games uploaded and added successfully"),
            @ApiResponse(responseCode = "50000", description = "Invalid XML file or server error")
    })
    @PostMapping("/upload-xml")
    public BaseResponse<Map<Long, Game>> uploadGamesFromXml(@RequestParam("file") MultipartFile file) {
        try {
            Map<Long, Game> updatedMap = gameService.loadFromXml(file.getInputStream());
            return ResultUtils.success(updatedMap);
        } catch (IOException e) {
            return ResultUtils.error(ErrorCode.SYSTEM_ERROR, "Invalid XML file");
        }
    }
}
