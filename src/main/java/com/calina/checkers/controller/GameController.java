package com.calina.checkers.controller;

import com.calina.checkers.dto.FinishedMoveDto;
import com.calina.checkers.dto.GameDto;
import com.calina.checkers.dto.MoveDto;
import com.calina.checkers.dto.PossibleMovesDto;
import com.calina.checkers.logic.Checker;
import com.calina.checkers.logic.Game;
import com.calina.checkers.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequiredArgsConstructor
@RequestMapping("/game")
public class GameController {
    private final GameService gameService;

    @GetMapping("/createGame")
    public GameDto getCheckers() {
        Game game = gameService.createGame();
        return new GameDto(game.getId(), game.getCheckers(), game.getTurnFraction());
    }

    @PostMapping("/possibleMoves")
    public ArrayList<Checker> getPossibleMoves(@RequestBody PossibleMovesDto possibleMovesDto) {
        return gameService.getMoves(possibleMovesDto.getId(), possibleMovesDto.getActiveChecker());
    }

    @PostMapping("/moveChecker")
    public FinishedMoveDto moveChecker(@RequestBody MoveDto moveDto) {
        Checker cell = moveDto.getChosenCell();
        gameService.moveChecker(moveDto.getId(), cell.getColumn(), cell.getRow());

        Game game = gameService.getGame(moveDto.getId());
        FinishedMoveDto finishedMoveDto = new FinishedMoveDto(game.getTurnFraction(), game.getCheckers(),
                game.getCatchingCheckers(), game.getCatchRelatedCells(), gameService.victoryCheck(moveDto.getId()));
        return finishedMoveDto;
    }


}
