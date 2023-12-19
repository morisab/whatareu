package com.whatareu.mechanics.handler;

import java.util.List;
import java.util.function.Predicate;

import com.whatareu.mechanics.gameboard.Board;

public interface EntityModifier {
    List<Board> elements();

    List<Board> elementsFiltered(Predicate<Board> filter);

    void addElement(Board boardElement);
}
