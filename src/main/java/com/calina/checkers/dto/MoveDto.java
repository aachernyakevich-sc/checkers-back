package com.calina.checkers.dto;

import com.calina.checkers.logic.Checker;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor

public class MoveDto {
    private Integer id;
    private Checker chosenCell;
}
