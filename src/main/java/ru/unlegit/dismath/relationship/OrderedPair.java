package ru.unlegit.dismath.relationship;

import lombok.NonNull;

public record OrderedPair(int left, int right) implements Comparable<OrderedPair> {

    @Override
    public String toString() {
        return "(%d,%d)".formatted(left, right);
    }

    public OrderedPair reverse() {
        return new OrderedPair(right, left);
    }

    public OrderedPair withLeft(int left) {
        return new OrderedPair(left, right);
    }

    public OrderedPair withRight(int right) {
        return new OrderedPair(left, right);
    }

    @Override
    public int compareTo(@NonNull OrderedPair other) {
        return left == other.left ? Integer.compare(right, other.right) : Integer.compare(left, other.left);
    }
}