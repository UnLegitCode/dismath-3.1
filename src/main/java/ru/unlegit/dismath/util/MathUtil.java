package ru.unlegit.dismath.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class MathUtil {

    public int getHalf(int value) {
        return (value >> 1) + ((value % 2 == 1) ? 1 : 0);
    }
}