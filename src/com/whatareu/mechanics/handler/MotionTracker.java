package com.whatareu.mechanics.handler;

import com.whatareu.mechanics.Shift;
import com.whatareu.mechanics.gameboard.Board;

public interface MotionTracker {
    boolean tryToMove(Board movingElement, Shift move);
}
