package com.whatareu.mechanics.gamelogic;

import javax.swing.*;

import com.whatareu.mechanics.Shift;
import com.whatareu.mechanics.gameboard.Board;
import com.whatareu.mechanics.handler.EntityModifier;
import com.whatareu.mechanics.handler.MotionTracker;
import com.whatareu.mechanics.handler.PropertyChecker;

public interface OperandB extends DirectivePart {
  OperandB NULL_RIGHT_OPERAND = new OperandB() {
    @Override
    public ImageIcon image() {
      return null;
    }

    @Override
    public OperandBEnum operandType() {
      return OperandBEnum.NULL_OPERAND;
    }

    @Override
    public boolean acceptedAsRight(Operator operator) {
      return false;
    }

    @Override
    public OperandB getAsRightOperand() {
      return this;
    }
  };

  @Override
  default OperandA getAsLeftOperand() {
    return OperandA.NULL_LEFT_OPERAND;
  }

  @Override
  default Operator getAsOperator() {
    return Operator.NULL_OPERATOR;
  }

  OperandBEnum operandType();

  default boolean onMove(Board trigger, Board receiver, PropertyChecker checker, Shift movement,
      MotionTracker observer) {
    return true;
  }

  default void onSuperposition(Board first, Board second, PropertyChecker checker) {

  }

  default void onDeath(Board dyingElement, EntityModifier elementEditor) {

  }

  default void onRuleCreation(OperandA leftOperand, OperandB rightOperand, EntityModifier elementEditor) {

  }

  boolean acceptedAsRight(Operator operator);
}
