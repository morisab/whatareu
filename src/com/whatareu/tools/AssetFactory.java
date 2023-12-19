package com.whatareu.tools;

import javax.swing.*;

import com.whatareu.mechanics.gameboard.Board;
import com.whatareu.mechanics.gamelogic.Directive;
import com.whatareu.mechanics.gamelogic.DirectivePart;
import com.whatareu.mechanics.gamelogic.sublogic.IsOperator;
import com.whatareu.mechanics.gamelogic.sublogic.Type;
import com.whatareu.mechanics.gamelogic.sublogic.property.Attribute;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AssetFactory {

    private final Map<String, DirectivePart> ruleParts = new HashMap<>();
    private final Map<String, Type> types = new HashMap<>();

    AssetFactory() {
        types.put("TEXT", new Type(new ImageIcon("resources/assets/nouns/TEXT/Text_TEXT_0.png"), new ImageIcon()));
        ruleParts.put("NULL_RULE_PART", DirectivePart.NULL_RULE_PART);
    }

    private static DirectivePart createRulePart(String part) {
        Objects.requireNonNull(part);
        return switch (part) {
            case "IS" -> new IsOperator();

            case "YOU" -> new Attribute.You();
            case "DEFEAT" -> new Attribute.Defeat();
            case "WIN" -> new Attribute.Win();
            case "PUSH" -> new Attribute.Push();
            case "STOP" -> new Attribute.Stop();

            default -> null;
        };
    }

    Board provideElement(int x, int y, String stringRulePart, String stringType) {
        Objects.requireNonNull(stringRulePart);
        Objects.requireNonNull(stringType);

        var rulePart = ruleParts.get(stringRulePart);
        if (rulePart == null) {
            rulePart = createRulePart(stringRulePart);
            ruleParts.put(stringRulePart, rulePart);
        }
        var type = types.get(stringType);
        return new Board(x, y, rulePart, type);
    }

    void addType(String stringType) {
        Objects.requireNonNull(stringType);
        if (types.containsKey(stringType)) {
            return;
        }
        var dir = "resources/assets/nouns/" + stringType + "/";
        var nounPic = new ImageIcon(dir + "Text_" + stringType + "_0.png");
        var elemPic = new ImageIcon(dir + stringType + "_0.png");

        var type = new Type(nounPic, elemPic);
        types.put(stringType, type);
        ruleParts.put(stringType, type);
    }

    Directive provideRule(String leftOperand, String operator, String rightOperand) {
        Objects.requireNonNull(leftOperand);
        Objects.requireNonNull(operator);
        Objects.requireNonNull(rightOperand);

        addType(leftOperand);
        var left = types.get(leftOperand);
        DirectivePart right = types.get(rightOperand);
        if (right == null) {
            right = createRulePart(rightOperand);
            if (right == null) {
                addType(rightOperand);
                right = types.get(rightOperand);
            }
        }
        return new Directive(left.getAsLeftOperand(), createRulePart(operator).getAsOperator(),
                right.getAsRightOperand());
    }

}
