package com.calina.checkers.logic;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Checker {
    private Integer row;
    private Integer column;
    private String fraction;
    private String role;
}
