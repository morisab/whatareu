package com.whatareu.mechanics.gamelogic;

import javax.swing.*;

import com.whatareu.mechanics.Shift;
import com.whatareu.mechanics.gameboard.Board;
import com.whatareu.mechanics.gamelogic.sublogic.Type;
import com.whatareu.mechanics.gamelogic.sublogic.property.Attribute;
import com.whatareu.mechanics.handler.EntityModifier;
import com.whatareu.mechanics.handler.MotionTracker;
import com.whatareu.mechanics.handler.PropertyChecker;

public interface Operator extends DirectivePart {
    Operator NULL_OPERATOR = new Operator() {
        @Override
        public ImageIcon image() {
            return null;
        }

        @Override
        public boolean acceptAsRight(Type rightOperand) {
            return false;
        }

        @Override
        public boolean acceptAsRight(Attribute rightOperand) {
            return false;
        }

        @Override
        public Operator getAsOperator() {
            return this;
        }
    };

    @Override
    default OperandA getAsLeftOperand() {
        return OperandA.NULL_LEFT_OPERAND;
    }

    @Override
    default OperandB getAsRightOperand() {
        return OperandB.NULL_RIGHT_OPERAND;
    }

    default boolean onMove(OperandB rightOperand, Board trigger, Board receiver, PropertyChecker checker,
            Shift movement, MotionTracker observer) {
        return true;
    }

    default void onSuperposition(OperandB rightOperand, Board first, Board second, PropertyChecker checker) {

    }

    default void onRuleCreation(OperandA leftOperand, OperandB rightOperand, EntityModifier elementEditor) {

    }

    default void onDeath(Board dyingElement, OperandB rightOperand, EntityModifier elementEditor) {

    }

    boolean acceptAsRight(Type rightOperand);

    boolean acceptAsRight(Attribute rightOperand);

    default boolean isProhibition(OperandA leftOperand, OperandB rightOperand) {
        return false;
    }

    default boolean canBeForbidden(OperandB rightOperand) {
        return false;
    }
}
