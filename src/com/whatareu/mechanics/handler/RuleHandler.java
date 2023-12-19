package com.whatareu.mechanics.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.whatareu.mechanics.gameboard.Board;
import com.whatareu.mechanics.gamelogic.*;
import com.whatareu.mechanics.gamelogic.sublogic.Type;

public final class RuleHandler implements PropertyChecker {
    private final List<Directive> rules = new ArrayList<>();
    private final List<Directive> defaultRules;
    private final Layout model;

    public RuleHandler(Layout model, List<Directive> defaultRules) {
        Objects.requireNonNull(model);
        Objects.requireNonNull(defaultRules);

        this.model = model;
        this.defaultRules = defaultRules;
    }

    private static List<OperandA> toLeftOperands(List<Board> elements) {
        return elements.stream()
                .map(e -> e.rulePart().getAsLeftOperand())
                .filter(l -> l != OperandA.NULL_LEFT_OPERAND)
                .collect(Collectors.toList());
    }

    private static List<OperandB> toRightOperands(List<Board> elements) {
        return elements.stream()
                .map(e -> e.rulePart().getAsRightOperand())
                .filter(r -> r != OperandB.NULL_RIGHT_OPERAND)
                .collect(Collectors.toList());
    }

    private void build(List<OperandA> LeftOperands, Operator operator, List<OperandB> rightOperands) {
        Objects.requireNonNull(LeftOperands);
        Objects.requireNonNull(operator);
        Objects.requireNonNull(rightOperands);

        rightOperands.forEach(rightOperand -> {
            if (rightOperand.acceptedAsRight(operator)) {
                LeftOperands.forEach(LeftOperand -> rules.add(new Directive(LeftOperand, operator, rightOperand)));
            }
        });
    }

    private void buildOperatorRules(Operator operator, int x, int y) {
        Objects.requireNonNull(operator);
        var leftList = toLeftOperands(model.elementsAt(x, y - 1));
        var rightList = toRightOperands(model.elementsAt(x, y + 1));
        var upList = toLeftOperands(model.elementsAt(x - 1, y));
        var downList = toRightOperands(model.elementsAt(x + 1, y));

        build(leftList, operator, rightList);
        build(upList, operator, downList);
    }

    private void buildRules() {
        var operators = model.elementsFiltered(e -> e.rulePart().getAsOperator() != Operator.NULL_OPERATOR);
        operators.forEach(
                operator -> buildOperatorRules(operator.rulePart().getAsOperator(), operator.x(), operator.y()));
        rules.addAll(defaultRules);
    }

    private void removeForbiddenRules() {
        var toRemove = new ArrayList<>();
        for (var i = 0; i < rules.size(); i++) {
            for (var j = 0; j < rules.size(); j++) {
                var first = rules.get(i);
                var second = rules.get(j);
                if (first != second && first.isIncompatible(second)) {
                    toRemove.add(first.isProhibition() ? second : first);
                }
            }
        }
        rules.removeIf(toRemove::contains);
    }

    void update() {
        rules.clear();
        buildRules();
        removeForbiddenRules();
    }

    public List<Directive> rules() {
        return List.copyOf(rules);
    }

    public List<Directive> rulesOf(Type type) {
        return rules.stream()
                .filter(rule -> type.equals(rule.leftOperand()))
                .collect(Collectors.toList());
    }

    @Override
    public boolean hasProperty(OperandBEnum property, Type type) {
        return rules.stream()
                .filter(r -> type.equals(r.leftOperand()))
                .map(Directive::rightOperandType)
                .anyMatch(t -> t.equals(property));
    }
}
