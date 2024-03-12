package com.calina.checkers.logic;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class Game {
    private Integer id;

    private final String BLACK = "BLACK";
    private final String WHITE = "WHITE";
    private final String QUEEN = "QUEEN";

    Checker activeChecker = new Checker(null, null, null, null);
    private ArrayList<Checker> catchingCheckers = new ArrayList<>();
    private ArrayList<Checker> catchRelatedCells = new ArrayList<>();
    private ArrayList<Checker> moveRelatedCells = new ArrayList<>();
    private String turnFraction = WHITE;

    private Checker [][] checkers = {
            {
                    new Checker(0,0,null,null),
                    new Checker(0,1, BLACK,null),
                    new Checker(0,2,null,null),
                    new Checker(0,3, BLACK,null),
                    new Checker(0,4,null,null),
                    new Checker(0,5, BLACK,null),
                    new Checker(0,6,null,null),
                    new Checker(0,7, BLACK,null)
            },
            {
                    new Checker(1,0,BLACK,null),
                    new Checker(1,1, null,null),
                    new Checker(1,2,BLACK,null),
                    new Checker(1,3, null,null),
                    new Checker(1,4,BLACK,null),
                    new Checker(1,5, null,null),
                    new Checker(1,6,BLACK,null),
                    new Checker(1,7, null,null)
            },
            {
                    new Checker(2,0,null,null),
                    new Checker(2,1, BLACK,null),
                    new Checker(2,2,null,null),
                    new Checker(2,3, BLACK,null),
                    new Checker(2,4,null,null),
                    new Checker(2,5, BLACK,null),
                    new Checker(2,6,null,null),
                    new Checker(2,7, BLACK,null)
            },
            {
                    new Checker(3,0,null,null),
                    new Checker(3,1, null,null),
                    new Checker(3,2,null,null),
                    new Checker(3,3, null,null),
                    new Checker(3,4,null,null),
                    new Checker(3,5, null,null),
                    new Checker(3,6,null,null),
                    new Checker(3,7, null,null)
            },
            {
                    new Checker(4,0,null,null),
                    new Checker(4,1, null,null),
                    new Checker(4,2,null,null),
                    new Checker(4,3, null,null),
                    new Checker(4,4,null,null),
                    new Checker(4,5, null,null),
                    new Checker(4,6,null,null),
                    new Checker(4,7, null,null)
            },
            {
                    new Checker(5,0,WHITE, null),
                    new Checker(5,1, null,null),
                    new Checker(5,2,WHITE,null),
                    new Checker(5,3, null,null),
                    new Checker(5,4,WHITE,null),
                    new Checker(5,5, null,null),
                    new Checker(5,6,WHITE,null),
                    new Checker(5,7, null,null)
            },
            {
                    new Checker(6,0,null,null),
                    new Checker(6,1, WHITE,null),
                    new Checker(6,2,null,null),
                    new Checker(6,3, WHITE,null),
                    new Checker(6,4,null,null),
                    new Checker(6,5, WHITE,null),
                    new Checker(6,6,null,null),
                    new Checker(6,7, WHITE,null)
            },
            {
                    new Checker(7,0,WHITE,null),
                    new Checker(7,1, null,null),
                    new Checker(7,2,WHITE,null),
                    new Checker(7,3, null,null),
                    new Checker(7,4,WHITE,null),
                    new Checker(7,5, null,null),
                    new Checker(7,6,WHITE,null),
                    new Checker(7,7, null,null)
            }
    };

    public Game (Integer id) {
        this.id = id;
    }
}
