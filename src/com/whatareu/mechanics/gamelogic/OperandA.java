package com.whatareu.mechanics.gamelogic;

import javax.swing.ImageIcon;

import com.whatareu.mechanics.gamelogic.sublogic.Type;

public interface OperandA extends DirectivePart {
    OperandA NULL_LEFT_OPERAND = new OperandA() {
        @Override
        public ImageIcon image() {
            return null;
        }

        @Override
        public OperandA getAsLeftOperand() {
            return this;
        }

        @Override
        public Type getAsType() {
            return null;
        }
    };

    @Override
    default Operator getAsOperator() {
        return Operator.NULL_OPERATOR;
    }

    @Override
    default OperandB getAsRightOperand() {
        return OperandB.NULL_RIGHT_OPERAND;
    }

    Type getAsType();
}
