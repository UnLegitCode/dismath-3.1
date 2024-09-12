package ru.unlegit.dismath.util;

public record IntRange(int start, int end) {

    public boolean contains(int value) {
        return value >= start && value <= end;
    }
}