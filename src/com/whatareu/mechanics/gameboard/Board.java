package com.whatareu.mechanics.gameboard;

import javax.swing.*;

import com.whatareu.mechanics.Shift;
import com.whatareu.mechanics.gamelogic.DirectivePart;
import com.whatareu.mechanics.gamelogic.sublogic.Type;

import java.util.Objects;

public final class Board implements Drawable, Cloneable {
    private boolean isAlive = true;
    private int x;
    private int y;
    private int lastTurnMoved = 0;
    private Type type;
    private DirectivePart rulePart;

    public Board(int x, int y, DirectivePart rulePart, Type type) {
        Objects.requireNonNull(rulePart);
        Objects.requireNonNull(type);
        this.x = x;
        this.y = y;
        this.rulePart = rulePart;
        this.type = type;
    }

    @Override
    public int x() {
        return x;
    }

    @Override
    public int y() {
        return y;
    }

    public void move(Shift move) {
        Objects.requireNonNull(move);
        x += move.vectorX();
        y += move.vectorY();
        lastTurnMoved++;
    }

    public boolean state() {
        return isAlive;
    }

    public void setState(boolean state) {
        isAlive = state;
    }

    @Override
    public ImageIcon image() {
        if (rulePart == DirectivePart.NULL_RULE_PART) {
            return type.elemImage();
        } else {
            return rulePart.image();
        }
    }

    public DirectivePart rulePart() {
        return rulePart;
    }

    public void setRulePart(DirectivePart rulePart) {
        Objects.requireNonNull(rulePart);
        this.rulePart = rulePart;
    }

    public Type type() {
        return type;
    }

    public void setType(Type type) {
        Objects.requireNonNull(type);
        this.type = type;
    }

    public int lastTurnMove() {
        return lastTurnMoved;
    }

    public Board clone() {
        try {
            return (Board) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }
}