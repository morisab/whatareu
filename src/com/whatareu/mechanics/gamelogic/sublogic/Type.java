package com.whatareu.mechanics.gamelogic.sublogic;

import javax.swing.*;

import com.whatareu.mechanics.gameboard.Board;
import com.whatareu.mechanics.gamelogic.*;
import com.whatareu.mechanics.handler.EntityModifier;

import java.util.Objects;

public record Type(ImageIcon image, ImageIcon elemImage) implements OperandA, OperandB {
    public Type {
        Objects.requireNonNull(image);
        Objects.requireNonNull(elemImage);
    }

    @Override
    public OperandA getAsLeftOperand() {
        return this;
    }

    @Override
    public OperandB getAsRightOperand() {
        return this;
    }

    @Override
    public Operator getAsOperator() {
        return Operator.NULL_OPERATOR;
    }

    @Override
    public OperandBEnum operandType() {
        return OperandBEnum.TYPE;
    }

    @Override
    public boolean acceptedAsRight(Operator operator) {
        Objects.requireNonNull(operator);
        return operator.acceptAsRight(this);
    }

    @Override
    public void onRuleCreation(OperandA leftOperand, OperandB rightOperand, EntityModifier elementEditor) {
        Objects.requireNonNull(leftOperand);
        Objects.requireNonNull(rightOperand);
        Objects.requireNonNull(elementEditor);

        var oldType = leftOperand.getAsType();
        if (oldType == this) {
            return;
        }
        var toModify = elementEditor.elementsFiltered(e -> oldType.equals(e.type()));
        toModify.forEach(element -> {
            element.setType(this);
        });
    }

    @Override
    public Type getAsType() {
        return this;
    }

    @Override
    public void onDeath(Board dyingElement, EntityModifier elementEditor) {
        Objects.requireNonNull(dyingElement);
        Objects.requireNonNull(elementEditor);

        var clone = dyingElement.clone();

        Objects.requireNonNull(clone);

        clone.setType(this);
        clone.setState(true);

        elementEditor.addElement(clone);
    }
}
