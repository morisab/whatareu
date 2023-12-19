package com.whatareu.mechanics.gamelogic;

import java.util.Objects;

import com.whatareu.mechanics.Shift;
import com.whatareu.mechanics.gameboard.Board;
import com.whatareu.mechanics.handler.EntityModifier;
import com.whatareu.mechanics.handler.MotionTracker;
import com.whatareu.mechanics.handler.PropertyChecker;

public record Directive(OperandA leftOperand, Operator operator, OperandB rightOperand) {
    public Directive {
        Objects.requireNonNull(leftOperand);
        Objects.requireNonNull(operator);
        Objects.requireNonNull(rightOperand);
    }

    public OperandBEnum rightOperandType() {
        return rightOperand.operandType();
    }

    public boolean onMove(Board trigger, Board receiver, PropertyChecker checker, Shift movement,
            MotionTracker observer) {
        Objects.requireNonNull(trigger);
        Objects.requireNonNull(receiver);
        Objects.requireNonNull(movement);
        Objects.requireNonNull(observer);
        return operator.onMove(rightOperand, trigger, receiver, checker, movement, observer);
    }

    public void onSuperposition(Board first, Board second, PropertyChecker checker) {
        Objects.requireNonNull(first);
        Objects.requireNonNull(second);
        Objects.requireNonNull(checker);
        operator.onSuperposition(rightOperand, first, second, checker);
    }

    public void onRuleCreation(EntityModifier elementEditor) {
        Objects.requireNonNull(elementEditor);
        operator.onRuleCreation(leftOperand, rightOperand, elementEditor);
    }

    public void onDeath(Board dyingElement, EntityModifier elementEditor) {
        Objects.requireNonNull(dyingElement);
        Objects.requireNonNull(elementEditor);
        operator.onDeath(dyingElement, rightOperand, elementEditor);
    }

    public boolean isProhibition() {
        return operator.isProhibition(leftOperand, rightOperand);
    }

    public boolean canBeForbidden() {
        return operator.canBeForbidden(rightOperand);
    }

    public boolean isIncompatible(Directive other) {
        return (leftOperand.equals(other.leftOperand) && operator.equals(other.operator))
                && (isProhibition() && other.canBeForbidden() || other.isProhibition() && canBeForbidden());
    }
}
