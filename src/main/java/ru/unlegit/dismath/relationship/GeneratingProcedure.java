package ru.unlegit.dismath.relationship;

@FunctionalInterface
public interface GeneratingProcedure {

    GeneratingProcedure UNIVERSE = (left, right) -> true;
    GeneratingProcedure IDENTITY = (left, right) -> left == right;

    boolean process(int left, int right);
}