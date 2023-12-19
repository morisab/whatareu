package com.whatareu.mechanics.gamelogic;

import javax.swing.*;

import com.whatareu.mechanics.ImageCheck;

public interface DirectivePart extends ImageCheck {
    DirectivePart NULL_RULE_PART = new DirectivePart() {
        @Override
        public ImageIcon image() {
            return null;
        }

        @Override
        public OperandA getAsLeftOperand() {
            return OperandA.NULL_LEFT_OPERAND;
        }

        @Override
        public Operator getAsOperator() {
            return Operator.NULL_OPERATOR;
        }

        @Override
        public OperandB getAsRightOperand() {
            return OperandB.NULL_RIGHT_OPERAND;
        }
    };

    OperandA getAsLeftOperand();

    Operator getAsOperator();

    OperandB getAsRightOperand();
}
