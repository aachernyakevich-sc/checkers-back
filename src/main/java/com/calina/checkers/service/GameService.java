package com.calina.checkers.service;

import com.calina.checkers.logic.Checker;
import com.calina.checkers.logic.Game;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;

@Service
public class GameService {
    HashMap<Integer, Game> games = new HashMap<>();
    private final String BLACK = "BLACK";
    private final String WHITE = "WHITE";
    private final String QUEEN = "QUEEN";

    private Boolean isOnBoard (int column, int row) {
        return (0 <= row && row <= 7 && 0 <= column && column <= 7);
    }

    private Boolean areThereEnemy (int cellColumn, int cellRow, String turnFraction, Checker[][] checkers) {
        return (isOnBoard(cellColumn, cellRow)
                && checkers[cellRow][cellColumn].getFraction() != null
                && !turnFraction.equals(checkers[cellRow][cellColumn].getFraction()));
    }


    private Boolean movePossible (int cellColumn, int cellRow, Checker activeChecker, Checker[][] checkers) {
        return (
                activeChecker.getFraction() != null &&
                        (Math.abs(activeChecker.getColumn()-cellColumn) == 1 && Math.abs(activeChecker.getRow()-cellRow) == 1) &&
                        isOnBoard(cellColumn, cellRow) &&
                        checkers[cellRow][cellColumn].getFraction() == null
        );
    }

    private Boolean canDoCatch(int column, int row, int columnDirection, int rowDirection, String turnFraction, Checker[][] checkers) {
        return areThereEnemy(column + columnDirection, row + rowDirection, turnFraction, checkers)
                && isOnBoard(column + 2 * columnDirection, row + 2 * rowDirection)
                && checkers[row + 2*rowDirection][column + 2*columnDirection].getFraction() == null;
    }

    private Boolean canThisCheckerCatch(int column, int row, String turnFraction, Checker[][] checkers) {
        return checkers[row][column].getRole() == null
                && (
                canDoCatch(column, row, 1,1, turnFraction, checkers) ||
                        canDoCatch(column, row, 1,-1, turnFraction, checkers) ||
                        canDoCatch(column, row, -1,-1, turnFraction, checkers) ||
                        canDoCatch(column, row, -1,1, turnFraction, checkers)
        );
    }


    private Boolean queenMovePossible (int cellColumn, int cellRow, Checker activeChecker, Checker[][] checkers) {
        if (QUEEN.equals(activeChecker.getRole()) && checkers[cellRow][cellColumn].getFraction() == null) {
            int columnDistance = cellColumn - activeChecker.getColumn();
            int rowDistance = cellRow - activeChecker.getRow();
            if (Math.abs(columnDistance) == Math.abs(rowDistance)) {
                int columnDirection = columnDistance / Math.abs(columnDistance);
                int rowDirection = rowDistance / Math.abs(rowDistance);
                int moveIndex = 1;
                for(; moveIndex < Math.abs(columnDistance); moveIndex++) {
                    int checkingColumn = activeChecker.getColumn() + moveIndex * columnDirection;
                    int checkingRow = activeChecker.getRow() + moveIndex * rowDirection;
                    if (checkers[checkingRow][checkingColumn].getFraction() != null) { break; }
                }
                return (moveIndex == Math.abs(columnDistance));
            }
        }
        return false;
    }

    private Boolean canDoCatchQueen (int column, int row, int columnDirection, int rowDirection, String turnFraction, Checker[][] checkers) {
        if (Math.abs(columnDirection) == Math.abs(rowDirection)) {
            int newColumn = column + columnDirection;
            int newRow = row + rowDirection;
            for (int i = 1; isOnBoard(newColumn, newRow); i++) {
                if (checkers[newRow][newColumn].getFraction() != null) {
                    return !turnFraction.equals(checkers[newRow][newColumn].getFraction())
                            && isOnBoard(newColumn + columnDirection, newRow + rowDirection)
                            && checkers[newRow + rowDirection][newColumn + columnDirection].getFraction() == null;
                }
                newColumn += columnDirection;
                newRow += rowDirection;
            }
        }
        return false;
    }

    private Boolean canThisQueenCatch (int column, int row, String turnFraction, Checker[][] checkers) {
        return turnFraction.equals(checkers[row][column].getFraction())
                && QUEEN.equals(checkers[row][column].getRole())
                && (
                canDoCatchQueen(column, row, 1, 1, turnFraction, checkers)
                        || canDoCatchQueen(column, row, 1, -1, turnFraction, checkers)
                        || canDoCatchQueen(column, row, -1, -1, turnFraction, checkers)
                        || canDoCatchQueen(column, row, -1, 1, turnFraction, checkers)
        );
    }

    private void fillCatchingCheckerList (String turnFraction, ArrayList<Checker> catchingCheckers, Checker[][] checkers) {
        if (catchingCheckers.size() > 0) { catchingCheckers.clear(); }
        for ( Checker[] row: checkers) {
            for (Checker checker: row) {
                if(turnFraction.equals(checker.getFraction())
                        && (canThisCheckerCatch(checker.getColumn(), checker.getRow(), turnFraction, checkers)
                        || canThisQueenCatch(checker.getColumn(), checker.getRow(), turnFraction, checkers))
                ) {
                    catchingCheckers.add(checker);
                }
            }
        }
    }


    private Boolean catchRelated (int cellColumn, int cellRow, String turnFraction, ArrayList<Checker> catchingCheckers, Checker[][] checkers) {
        ArrayList<Checker> potentialKiller = new ArrayList<>();
        for (Checker checker: catchingCheckers) {
            if (
                    (checker.getColumn() == cellColumn - 2
                            && checker.getRow() == cellRow - 2
                            && areThereEnemy(cellColumn - 1,cellRow - 1, turnFraction, checkers)) ||
                            (checker.getColumn() == cellColumn - 2
                                    && checker.getRow() == cellRow + 2
                                    && areThereEnemy(cellColumn - 1,cellRow + 1, turnFraction, checkers)) ||
                            (checker.getColumn() == cellColumn + 2
                                    && checker.getRow() == cellRow + 2
                                    && areThereEnemy(cellColumn + 1,cellRow + 1, turnFraction, checkers)) ||
                            (checker.getColumn() == cellColumn + 2
                                    && checker.getRow() == cellRow - 2
                                    && areThereEnemy(cellColumn + 1,cellRow - 1, turnFraction, checkers))
            ) {
                potentialKiller.add(checker);
            }
        }
        return potentialKiller.size() > 0;
    }

    private Boolean queenCatchRelated (int cellColumn, int cellRow, String turnFraction, ArrayList<Checker> catchingCheckers, Checker[][] checkers) {
        ArrayList<Checker> potentialKiller = new ArrayList<>();
        for (Checker checker: catchingCheckers) {
            if (QUEEN.equals(checker.getRole())
                    &&  ((cellColumn - checker.getColumn()) != 0) && ((cellRow - checker.getRow()) != 0)
            ) {
                int columnDirection = (cellColumn - checker.getColumn()) / Math.abs(cellColumn - checker.getColumn());
                int rowDirection = (cellRow - checker.getRow()) / Math.abs(cellRow - checker.getRow());
                if ( Math.abs(checker.getColumn()-cellColumn) == Math.abs(checker.getRow()-cellRow)
                        && isOnBoard(cellColumn - columnDirection, cellRow - rowDirection)
                        && isOnBoard(cellColumn - 2*columnDirection, cellRow - 2*rowDirection)
                ) {
                    if (canDoCatchQueen(checker.getColumn(), checker.getRow(), columnDirection, rowDirection, turnFraction, checkers)
                            && checkers[cellRow - rowDirection][cellColumn - columnDirection].getFraction() != null
                            && !turnFraction.equals(checkers[cellRow - rowDirection][cellColumn - columnDirection].getFraction())
                    ) {
                        potentialKiller.add(checker);
                    }
                }
            }
        }
        return potentialKiller.size() > 0;
    }

    private void fillMoveRelatedCells (Checker activeChecker, ArrayList<Checker> moveRelatedCells, Checker[][] checkers) {
        if (moveRelatedCells.size() > 0) {
            moveRelatedCells.clear();
        }
        for ( Checker[] row: checkers) {
            for (Checker checker: row) {
                if ( checker.getFraction() == null
                        && ( movePossible (checker.getColumn(), checker.getRow(), activeChecker, checkers) ||
                        queenMovePossible(checker.getColumn(), checker.getRow(), activeChecker, checkers))
                ) {
                    moveRelatedCells.add(checker);
                }
            }
        }
    }

    private void fillCatchRelatedCells (String turnFraction, ArrayList<Checker> catchRelatedCells, ArrayList<Checker> catchingCheckers, Checker[][] checkers) {
        if (catchRelatedCells.size() > 0) {
            catchRelatedCells.clear();
        }
        for ( Checker[] row: checkers) {
            for (Checker checker: row) {
                if (checker.getFraction() == null
                        && (catchRelated (checker.getColumn(), checker.getRow(), turnFraction, catchingCheckers, checkers) ||
                        queenCatchRelated(checker.getColumn(), checker.getRow(), turnFraction, catchingCheckers, checkers))
                ) {
                    catchRelatedCells.add(checker);
                }
            }
        }
    }


    public boolean haveThisChecker (ArrayList<Checker> checkers, Checker checker) {
        Checker result = checkers.stream().filter(el -> el.getRow() == checker.getRow()
                && el.getColumn() == checker.getColumn()).findFirst().orElse(null);
        return result != null;
    }

    private void hadBecomeQueen(int column, int row, Checker[][] checkers) {
        if ((WHITE.equals(checkers[row][column].getFraction()) && row == 0) ||
                (BLACK.equals(checkers[row][column].getFraction()) && row == 7)
        ) {
            checkers[row][column] = new Checker(row, column, checkers[row][column].getFraction(), QUEEN);
        }
    }

    public String victoryCheck(Integer gameID) {
        Game game = getGame(gameID);
        Checker activeChecker = game.getActiveChecker();
        String turnFraction = game.getTurnFraction();
        Checker[][] checkers = game.getCheckers();
        ArrayList<Checker> checkersOfFraction = new ArrayList<>();
        for ( Checker[] row: checkers) {
            for (Checker checker: row) {
                if (turnFraction.equals(checker.getFraction())) {
                    checkersOfFraction.add(checker);
                }
            }
        }

        if (checkersOfFraction.size() > 0) {
            int i = 0;
            for (; i < checkersOfFraction.size(); i++) {
                if (movePossible(checkersOfFraction.get(i).getColumn(), checkersOfFraction.get(i).getRow(), activeChecker, checkers) ||
                        canThisQueenCatch(checkersOfFraction.get(i).getColumn(), checkersOfFraction.get(i).getRow(), turnFraction, checkers)
                ) { break; }
            }
            if (i != checkersOfFraction.size()-1) return "";
        } else {
            if (turnFraction.equals(WHITE)) {
                return BLACK;
            } else {
                return WHITE;
            }
        }
        return "";
    }

    public void moveChecker (Integer id, int cellColumn, int cellRow) {
        Game game = getGame(id);
        Checker activeChecker = game.getActiveChecker();
        if (activeChecker.getFraction() != null){
            Checker chosenCell = new Checker(cellRow, cellColumn, null, null);
            String turnFraction = game.getTurnFraction();
            Checker[][] checkers = game.getCheckers();
            ArrayList<Checker> catchingCheckers = game.getCatchingCheckers();
            ArrayList<Checker> catchRelatedCells = game.getCatchRelatedCells();
            ArrayList<Checker> moveRelatedCells =  game.getMoveRelatedCells();
            if (haveThisChecker(catchRelatedCells, chosenCell)) {
                if (Math.abs(activeChecker.getRow() - cellRow) == Math.abs(activeChecker.getColumn() - cellColumn)) {
                    checkers[activeChecker.getRow()][activeChecker.getColumn()] = new Checker(activeChecker.getRow(), activeChecker.getColumn(), null, null);
                    checkers[cellRow][cellColumn] = new Checker(cellRow, cellColumn, activeChecker.getFraction(), activeChecker.getRole());
                    int columnDirection = (cellColumn - activeChecker.getColumn()) / Math.abs(cellColumn - activeChecker.getColumn());
                    int rowDirection = (cellRow - activeChecker.getRow()) / Math.abs(cellRow - activeChecker.getRow());
                    int caughtRow = cellRow - rowDirection;
                    int caughtColumn = cellColumn - columnDirection;
                    checkers[caughtRow][caughtColumn] = new Checker(caughtRow, caughtColumn, null, null);
                    hadBecomeQueen(cellColumn, cellRow, checkers);
                    game.setCheckers(checkers);
                    activeChecker = new Checker(null, null, null, null);
                    game.setActiveChecker(activeChecker);
                    fillCatchingCheckerList(turnFraction, catchingCheckers, checkers);
                    game.setCatchingCheckers(catchingCheckers);
                    fillCatchRelatedCells(turnFraction, catchRelatedCells, catchingCheckers, checkers);
                    game.setCatchRelatedCells(catchRelatedCells);
                    if (catchRelatedCells.size() == 0) {
                        if (turnFraction.equals(WHITE)) {
                            turnFraction = BLACK;
                            game.setTurnFraction(BLACK);
                        } else {
                            turnFraction = WHITE;
                            game.setTurnFraction(WHITE);
                        }
                    }
                    fillCatchingCheckerList(turnFraction, catchingCheckers, checkers);
                    game.setCatchingCheckers(catchingCheckers);
                    fillCatchRelatedCells(turnFraction, catchRelatedCells, catchingCheckers, checkers);
                    game.setCatchRelatedCells(catchRelatedCells);
                }
            } else if (haveThisChecker(moveRelatedCells, chosenCell)) {
                checkers[activeChecker.getRow()][activeChecker.getColumn()] = new Checker(activeChecker.getRow(), activeChecker.getColumn(), null, null);
                checkers[cellRow][cellColumn] = new Checker(cellRow, cellColumn, activeChecker.getFraction(), activeChecker.getRole());
                hadBecomeQueen(cellColumn, cellRow, checkers);
                game.setCheckers(checkers);
                activeChecker = new Checker(null, null, null, null);
                game.setActiveChecker(activeChecker);
                if (turnFraction.equals(WHITE)) {
                    turnFraction = BLACK;
                    game.setTurnFraction(BLACK);
                } else {
                    turnFraction = WHITE;
                    game.setTurnFraction(WHITE);
                }
                fillCatchingCheckerList(turnFraction, catchingCheckers, checkers);
                game.setCatchingCheckers(catchingCheckers);
                fillCatchRelatedCells(turnFraction, catchRelatedCells, catchingCheckers, checkers);
                game.setCatchRelatedCells(catchRelatedCells);
            }
        }
    }



    public Game createGame() {
        Game game = new Game(games.size());
        games.put(game.getId(), game);
        return game;
    }

    public Game getGame(Integer gameId) {
        return games.get(gameId);
    }

    public ArrayList<Checker> getMoves (Integer gameId, Checker activeChecker) {
        Game game = getGame(gameId);
        game.setActiveChecker(activeChecker);
        ArrayList<Checker> moveRelatedCells = game.getMoveRelatedCells();
        fillMoveRelatedCells(activeChecker, moveRelatedCells, game.getCheckers());
        game.setMoveRelatedCells(moveRelatedCells);

        return moveRelatedCells;
    }
}
