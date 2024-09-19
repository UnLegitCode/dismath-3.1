package ru.unlegit.dismath.relationship;

import ru.unlegit.dismath.util.IntRange;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class Main {

    private static final IntRange B_RANGE = new IntRange(3, 7);
    private static final GeneratingProcedure GENERATING_PROCEDURE_A = (left, right) -> (left + right) % 3 == 0;
    private static final GeneratingProcedure GENERATING_PROCEDURE_B = (left, right) ->
            B_RANGE.contains(left) || B_RANGE.contains(right);
    private static final GeneratingProcedure GENERATING_PROCEDURE_C = (left, right) ->
            ((left * left) + (right * right)) < 100;

    public static void main(String[] args) {
        int[] baseSet = IntStream.rangeClosed(1, 10).toArray();

        OrderedPairListRelationship A = OrderedPairListRelationship.generate(baseSet, GENERATING_PROCEDURE_A);
        OrderedPairListRelationship B = OrderedPairListRelationship.generate(baseSet, GENERATING_PROCEDURE_B);
        OrderedPairListRelationship C = OrderedPairListRelationship.generate(baseSet, GENERATING_PROCEDURE_C);

        OrderedPairListRelationship one = A.power(2);
        OrderedPairListRelationship two = A.inversion();
        OrderedPairListRelationship three = one.subtract(B);
        OrderedPairListRelationship four = three.or(two);
        OrderedPairListRelationship D = four.composition(C);

        System.out.println("Ordered pair list implementation:");

        System.out.println("A = " + A);
        A.printMatrix();
        System.out.println("B = " + B);
        B.printMatrix();
        System.out.println("C = " + C);
        C.printMatrix();
        System.out.println("D = " + D);
        D.printMatrix();
        System.out.println();

        int size = baseSet.length;

        MatrixRelationship matrixA = MatrixRelationship.generate(size, GENERATING_PROCEDURE_A);
        MatrixRelationship matrixB = MatrixRelationship.generate(size, GENERATING_PROCEDURE_B);
        MatrixRelationship matrixC = MatrixRelationship.generate(size, GENERATING_PROCEDURE_C);

        MatrixRelationship matrixOne = matrixA.power(2);
        MatrixRelationship matrixTwo = matrixA.inversion();
        MatrixRelationship matrixThree = matrixOne.subtract(matrixB);
        MatrixRelationship matrixFour = matrixThree.or(matrixTwo);
        MatrixRelationship matrixD = matrixFour.composition(matrixC);

        System.out.println("Matrix implementation:");

        System.out.println("A = " + matrixA);
        matrixA.printMatrix();
        System.out.println("B = " + matrixB);
        matrixB.printMatrix();
        System.out.println("C = " + matrixC);
        matrixC.printMatrix();
        System.out.println("D = " + matrixD);
        matrixD.printMatrix();

        System.out.println("\nRelationship properties:");

        Set<RelationshipProperty> aProperties = RelationshipProperty.findProperties(matrixA, "A");
        Set<RelationshipProperty> bProperties = RelationshipProperty.findProperties(matrixB, "B");
        Set<RelationshipProperty> cProperties = RelationshipProperty.findProperties(matrixC, "C");

        System.out.println("\nBasic properties:");

        System.out.println("A: ".concat(aProperties.stream()
                .map(RelationshipProperty::getDisplayName)
                .collect(Collectors.joining(", "))
        ));
        System.out.println("B: ".concat(bProperties.stream()
                .map(RelationshipProperty::getDisplayName)
                .collect(Collectors.joining(", "))
        ));
        System.out.println("C: ".concat(cProperties.stream()
                .map(RelationshipProperty::getDisplayName)
                .collect(Collectors.joining(", "))
        ));

        System.out.println("\nDerived properties:");

        System.out.println("A: " + Optional.ofNullable(DerivedRelationshipProperty.findDerivedProperty(aProperties))
                .map(DerivedRelationshipProperty::getDisplayName)
                .orElse("отсутствует")
        );
        System.out.println("B: " + Optional.ofNullable(DerivedRelationshipProperty.findDerivedProperty(bProperties))
                .map(DerivedRelationshipProperty::getDisplayName)
                .orElse("отсутствует")
        );
        System.out.println("C: " + Optional.ofNullable(DerivedRelationshipProperty.findDerivedProperty(cProperties))
                .map(DerivedRelationshipProperty::getDisplayName)
                .orElse("отсутствует")
        );
    }
}