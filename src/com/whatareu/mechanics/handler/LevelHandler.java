package com.whatareu.mechanics.handler;

import com.whatareu.mechanics.Trajectory;
import com.whatareu.mechanics.Shift;
import com.whatareu.mechanics.gameboard.Board;
import com.whatareu.mechanics.gameboard.Point;
import com.whatareu.mechanics.gamelogic.OperandBEnum;
import com.whatareu.tools.Status;
import com.whatareu.mechanics.gamelogic.Directive;

import java.awt.*;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public final class LevelHandler implements MotionTracker {
    private final int width;
    private final int height;
    private final Layout model;
    private final RuleHandler ruleManager;
    private final DisplayHandler displayManager;

    public LevelHandler(int width, int height, List<Board> boardElements, List<Directive> defaultRules) {
        Objects.requireNonNull(boardElements);
        Objects.requireNonNull(defaultRules);
        this.width = width;
        this.height = height;
        this.model = new Layout(boardElements);
        this.ruleManager = new RuleHandler(model, defaultRules);
        this.displayManager = new DisplayHandler(model, width, height);
    }

    public void displayGame(Graphics2D graphics, int x, int y, int windowWidth, int windowHeight) {
        displayManager.display(graphics, x, y, windowWidth, windowHeight);
    }

    private void applySuperPositionRules(Board first, Board second) {
        if (first.state() && second.state()) {
            var rules = ruleManager.rulesOf(first.type());
            rules.addAll(ruleManager.rulesOf(second.type()));
            rules.forEach(rule -> rule.onSuperposition(first, second, ruleManager));
        }
    }

    private void updateElement(Board element) {
        model.elementsAt(element.x(), element.y()).forEach(other -> applySuperPositionRules(element, other));
        if (!element.state()) {
            ruleManager.rulesOf(element.type()).forEach(r -> r.onDeath(element, model));
        }
    }

    private void updateElement() {
        model.elements().forEach(this::updateElement);
    }

    public void update() {
        ruleManager.update();
        ruleManager.rules().forEach(rule -> rule.onRuleCreation(model));
        updateElement();
        model.removeAllDead();
    }

    @Override
    public boolean tryToMove(Board movingElement, Shift move) {
        Objects.requireNonNull(movingElement);
        Objects.requireNonNull(move);

        var destX = movingElement.x() + move.vectorX();
        var destY = movingElement.y() + move.vectorY();

        if (destX < 0 || destX >= width || destY < 0 || destY >= height) {
            return false;
        }

        var others = model.elementsAt(destX, destY);
        if (!others.isEmpty()) {
            for (var other : others) {
                for (var otherRule : ruleManager.rulesOf(other.type())) {
                    var canMove = otherRule.onMove(movingElement, other, ruleManager, move, this);
                    if (!canMove) {
                        return false;
                    }
                }
            }
        }
        movingElement.move(move);
        return true;
    }

    public void moveYou(Trajectory direction) {
        var move = switch (direction) {
            case UP -> new Shift(0, -1);
            case DOWN -> new Shift(0, 1);
            case LEFT -> new Shift(-1, 0);
            case RIGHT -> new Shift(1, 0);
        };

        var yous = sortByDirection(getElementsWithProperty(OperandBEnum.YOU), direction);
        yous.forEach(you -> tryToMove(you, move));
    }

    private static List<Board> sortByDirection(List<Board> youList, Trajectory direction) {
        Comparator<Board> comp;
        if (direction == Trajectory.UP || direction == Trajectory.DOWN) {
            comp = Comparator.comparingInt(Point::y);
        } else {
            comp = Comparator.comparingInt(Point::x);
        }
        if (direction == Trajectory.DOWN || direction == Trajectory.RIGHT) {
            comp = comp.reversed();
        }
        return youList.stream().sorted(comp).collect(Collectors.toList());
    }

    public Status checkGameStatus() {
        var yous = getElementsWithProperty(OperandBEnum.YOU);
        if (yous.isEmpty()) {
            return Status.LOSE;
        } else {
            var wins = getElementsWithProperty(OperandBEnum.WIN);
            for (var you : yous) {
                for (var win : wins) {
                    if (you.x() == win.x() && you.y() == win.y()) {
                        return Status.WIN;
                    }
                }
            }
            return Status.ONGOING;
        }
    }

    private List<Board> getElementsWithProperty(OperandBEnum type) {
        return model.elementsFiltered(e -> ruleManager.hasProperty(type, e.type()));
    }
}
