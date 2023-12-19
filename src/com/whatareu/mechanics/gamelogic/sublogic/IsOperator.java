package com.whatareu.mechanics.gamelogic.sublogic;

import javax.swing.*;

import com.whatareu.mechanics.Shift;
import com.whatareu.mechanics.gameboard.Board;
import com.whatareu.mechanics.gamelogic.OperandA;
import com.whatareu.mechanics.gamelogic.Operator;
import com.whatareu.mechanics.gamelogic.OperandB;
import com.whatareu.mechanics.gamelogic.OperandBEnum;
import com.whatareu.mechanics.gamelogic.sublogic.property.Attribute;
import com.whatareu.mechanics.handler.EntityModifier;
import com.whatareu.mechanics.handler.MotionTracker;
import com.whatareu.mechanics.handler.PropertyChecker;

import java.util.Objects;

public record IsOperator() implements Operator {
    private final static ImageIcon icon = new ImageIcon("resources/assets/operators/IS/Op_IS.png");

    @Override
    public boolean onMove(OperandB rightOperand, Board trigger, Board receiver, PropertyChecker checker, Shift movement,
            MotionTracker observer) {
        Objects.requireNonNull(rightOperand);
        Objects.requireNonNull(trigger);
        Objects.requireNonNull(receiver);
        Objects.requireNonNull(movement);
        Objects.requireNonNull(observer);
        return rightOperand.onMove(trigger, receiver, checker, movement, observer);
    }

    @Override
    public void onSuperposition(OperandB rightOperand, Board first, Board second, PropertyChecker checker) {
        Objects.requireNonNull(rightOperand);
        Objects.requireNonNull(first);
        Objects.requireNonNull(second);
        Objects.requireNonNull(checker);
        rightOperand.onSuperposition(first, second, checker);
    }

    @Override
    public void onRuleCreation(OperandA leftOperand, OperandB rightOperand, EntityModifier elementEditor) {
        Objects.requireNonNull(rightOperand);
        rightOperand.onRuleCreation(leftOperand, rightOperand, elementEditor);
    }

    @Override
    public boolean acceptAsRight(Type rightOperand) {
        Objects.requireNonNull(rightOperand);
        return true;
    }

    @Override
    public boolean acceptAsRight(Attribute rightOperand) {
        Objects.requireNonNull(rightOperand);
        return true;
    }

    @Override
    public Operator getAsOperator() {
        return this;
    }

    @Override
    public ImageIcon image() {
        return icon;
    }

    @Override
    public boolean isProhibition(OperandA leftOperand, OperandB rightOperand) {
        Objects.requireNonNull(leftOperand);
        Objects.requireNonNull(rightOperand);
        return leftOperand.equals(rightOperand);
    }

    @Override
    public boolean canBeForbidden(OperandB rightOperand) {
        Objects.requireNonNull(rightOperand);
        return rightOperand.operandType().equals(OperandBEnum.TYPE);
    }
}