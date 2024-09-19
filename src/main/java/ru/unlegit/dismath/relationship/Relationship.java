package ru.unlegit.dismath.relationship;

public interface Relationship<T extends Relationship<T>> {

    String toString();

    boolean inclusion(T relationship);

    boolean equals(T relationship);

    boolean strictInclusion(T relationship);

    T or(T relationship);

    T and(T relationship);

    T subtract(T relationship);

    T subtractSymmetrically(T relationship);


    T addition();

    T inversion();

    T composition(T relationship);

    T power(int power);

    void printMatrix();
}