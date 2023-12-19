package com.whatareu.mechanics.handler;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.whatareu.mechanics.gameboard.Board;
import com.whatareu.mechanics.gameboard.Drawable;

final class Layout implements EntityModifier {

    private final List<Board> elements = new ArrayList<>();

    Layout(List<Board> elements) {
        Objects.requireNonNull(elements);
        this.elements.addAll(elements);
    }

    void removeAllDead() {
        elements.removeIf(Predicate.not(Board::state));
    }

    @Override
    public List<Board> elements() {
        return List.copyOf(elements);
    }

    List<Drawable> displayableElements() {
        return elements.stream()
                .sorted(Comparator.comparingInt(Board::lastTurnMove))
                .collect(Collectors.toList());
    }

    @Override
    public List<Board> elementsFiltered(Predicate<Board> filter) {
        return elements.stream()
                .filter(filter)
                .collect(Collectors.toList());
    }

    @Override
    public void addElement(Board boardElement) {
        Objects.requireNonNull(boardElement);
        elements().add(boardElement);
    }

    List<Board> elementsAt(int x, int y) {
        return elementsFiltered(e -> e.x() == x && e.y() == y);
    }

}
