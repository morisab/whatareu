package com.whatareu.mechanics.handler;

import com.whatareu.mechanics.gamelogic.OperandBEnum;
import com.whatareu.mechanics.gamelogic.sublogic.Type;

public interface PropertyChecker {
    boolean hasProperty(OperandBEnum property, Type type);
}
