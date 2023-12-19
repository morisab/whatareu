package com.whatareu.mechanics.gamelogic.sublogic.property;

import javax.swing.*;

import com.whatareu.mechanics.Shift;
import com.whatareu.mechanics.gameboard.Board;
import com.whatareu.mechanics.gamelogic.Operator;
import com.whatareu.mechanics.handler.MotionTracker;
import com.whatareu.mechanics.handler.PropertyChecker;
import com.whatareu.mechanics.gamelogic.OperandB;
import com.whatareu.mechanics.gamelogic.OperandBEnum;

import java.util.Objects;

public sealed interface Attribute extends OperandB {
    record Push() implements Attribute {
        private final static OperandBEnum type = OperandBEnum.PUSH;
        private final static ImageIcon icon = new ImageIcon("resources/assets/properties/PUSH/Prop_PUSH.png");

        @Override
        public OperandBEnum operandType() {
            return type;
        }

        @Override
        public ImageIcon image() {
            return icon;
        }

        @Override
        public boolean onMove(Board trigger, Board receiver, PropertyChecker checker, Shift movement,
                MotionTracker observer) {
            Objects.requireNonNull(receiver);
            Objects.requireNonNull(movement);
            Objects.requireNonNull(observer);
            Objects.requireNonNull(checker);
            if (checker.hasProperty(OperandBEnum.PUSH, receiver.type())) {
                return observer.tryToMove(receiver, movement);
            } else {
                return true;
            }
        }

        @Override
        public boolean acceptedAsRight(Operator operator) {
            Objects.requireNonNull(operator);
            return operator.acceptAsRight(this);
        }

        @Override
        public OperandB getAsRightOperand() {
            return this;
        }
    }

    record Stop() implements Attribute {
        private final static OperandBEnum type = OperandBEnum.STOP;
        private final static ImageIcon icon = new ImageIcon("resources/assets/properties/STOP/Prop_STOP.png");

        @Override
        public OperandBEnum operandType() {
            return type;
        }

        @Override
        public ImageIcon image() {
            return icon;
        }

        @Override
        public boolean onMove(Board trigger, Board receiver, PropertyChecker checker, Shift movement,
                MotionTracker observer) {
            Objects.requireNonNull(receiver);
            Objects.requireNonNull(checker);
            return !checker.hasProperty(OperandBEnum.STOP, receiver.type());
        }

        @Override
        public OperandB getAsRightOperand() {
            return this;
        }

        @Override
        public boolean acceptedAsRight(Operator operator) {
            Objects.requireNonNull(operator);
            return operator.acceptAsRight(this);
        }
    }

    record Defeat() implements Attribute {
        private final static OperandBEnum type = OperandBEnum.DEFEAT;
        private final static ImageIcon icon = new ImageIcon("resources/assets/properties/DEFEAT/Prop_DEFEAT.png");

        @Override
        public OperandBEnum operandType() {
            return type;
        }

        @Override
        public ImageIcon image() {
            return icon;
        }

        @Override
        public void onSuperposition(Board first, Board second, PropertyChecker checker) {
            Objects.requireNonNull(first);
            Objects.requireNonNull(second);
            Objects.requireNonNull(checker);
            if (checker.hasProperty(OperandBEnum.YOU, first.type())) {
                first.setState(false);
            } else if (checker.hasProperty(OperandBEnum.YOU, second.type())) {
                second.setState(false);
            }
        }

        @Override
        public OperandB getAsRightOperand() {
            return this;
        }

        @Override
        public boolean acceptedAsRight(Operator operator) {
            Objects.requireNonNull(operator);
            return operator.acceptAsRight(this);
        }
    }

    record You() implements Attribute {
        private final static OperandBEnum type = OperandBEnum.YOU;
        private final static ImageIcon icon = new ImageIcon("resources/assets/properties/YOU/Prop_YOU.png");

        @Override
        public OperandBEnum operandType() {
            return type;
        }

        @Override
        public ImageIcon image() {
            return icon;
        }

        @Override
        public OperandB getAsRightOperand() {
            return this;
        }

        @Override
        public boolean acceptedAsRight(Operator operator) {
            Objects.requireNonNull(operator);
            return operator.acceptAsRight(this);
        }
    }

    record Win() implements Attribute {
        private final static OperandBEnum type = OperandBEnum.WIN;
        private final static ImageIcon icon = new ImageIcon("resources/assets/properties/WIN/Prop_WIN.png");

        @Override
        public OperandBEnum operandType() {
            return type;
        }

        @Override
        public ImageIcon image() {
            return icon;
        }

        @Override
        public OperandB getAsRightOperand() {
            return this;
        }

        @Override
        public boolean acceptedAsRight(Operator operator) {
            Objects.requireNonNull(operator);
            return operator.acceptAsRight(this);
        }
    }
}
