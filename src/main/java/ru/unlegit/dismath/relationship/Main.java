package ru.unlegit.dismath.relationship;

import ru.unlegit.dismath.util.IntRange;

import java.util.stream.IntStream;

public final class Main {

    private static final IntRange B_RANGE = new IntRange(3, 7);

    public static void main(String[] args) {
        int[] baseSet = IntStream.rangeClosed(1, 10).toArray();

        Relationship A = Relationship.generate(baseSet, (left, right) -> (left + right) % 3 == 0);
        Relationship B = Relationship.generate(
                baseSet, (left, right) -> B_RANGE.contains(left) || B_RANGE.contains(right)
        );
        Relationship C = Relationship.generate(baseSet, (left, right) -> ((left * left) + (right * right)) < 100);

        Relationship one = A.power(2);
        Relationship two = A.inversion();
        Relationship three = one.subtract(B);
        Relationship four = three.or(two);

        Relationship D = four.composition(C);

        System.out.println();
        System.out.println(one);
        System.out.println(two);
        System.out.println(three);
        System.out.println(four);
        System.out.println(D);
        A.printMatrix();
        System.out.println();
        B.printMatrix();
        System.out.println();
        C.printMatrix();
        System.out.println();
        D.printMatrix();
    }
}