package com.calina.checkers.dto;

import com.calina.checkers.logic.Checker;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FinishedMoveDto {
    private String turnFraction;
    private Checker [][] checkers;
    private ArrayList<Checker> catchingCheckers;
    private ArrayList<Checker> catchRelatedCells;
    private String victory;
}
