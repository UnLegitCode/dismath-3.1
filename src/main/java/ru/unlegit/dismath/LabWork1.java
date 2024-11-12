package ru.unlegit.dismath;

import ru.unlegit.dismath.relationship.*;
import ru.unlegit.dismath.util.IntRange;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class LabWork1 {

    //    private static final IntRange B_RANGE = new IntRange(3, 7);
//    private static final GeneratingProcedure GENERATING_PROCEDURE_A = (left, right) -> (left + right) % 3 == 0;
//    private static final GeneratingProcedure GENERATING_PROCEDURE_B = (left, right) ->
//            B_RANGE.contains(left) || B_RANGE.contains(right);
//    private static final GeneratingProcedure GENERATING_PROCEDURE_C = (left, right) ->
//            ((left * left) + (right * right)) < 100;
    private static final List<Integer> C_LEFT_VALUES = List.of(1, 2, 4, 8);
    private static final List<Integer> C_RIGHT_VALUES = List.of(3, 5, 7, 10);
    private static final GeneratingProcedure GENERATING_PROCEDURE_A = (left, right) ->
            (((left + right) % 2) == 0) && ((left + right) > 8);
    private static final GeneratingProcedure GENERATING_PROCEDURE_B = (left, right) -> (left + (right << 1)) > 20;
    private static final GeneratingProcedure GENERATING_PROCEDURE_C = (left, right) ->
            C_LEFT_VALUES.contains(left) && C_RIGHT_VALUES.contains(right);

    private static boolean isInRange(int value, int leftBound, int rightBound) {
        return value > leftBound && value > rightBound;
    }

    public static void main(String[] args) {
        int[] baseSet = IntStream.rangeClosed(1, 10).toArray();
        int size = baseSet.length;

//        MatrixRelationship matrixA = MatrixRelationship.generate(size, GENERATING_PROCEDURE_A);
//        MatrixRelationship matrixB = MatrixRelationship.generate(size, GENERATING_PROCEDURE_B);
//        MatrixRelationship matrixC = MatrixRelationship.generate(size, GENERATING_PROCEDURE_C);
//
//        Set<RelationshipProperty> aProperties = RelationshipProperty.findProperties(matrixA, "A");
//        Set<RelationshipProperty> bProperties = RelationshipProperty.findProperties(matrixB, "B");
//        Set<RelationshipProperty> cProperties = RelationshipProperty.findProperties(matrixC, "C");
//
//        System.out.println("\nBasic properties:");
//
//        System.out.println("A: ".concat(aProperties.stream()
//                .map(RelationshipProperty::getDisplayName)
//                .collect(Collectors.joining(", "))
//        ));
//        System.out.println("B: ".concat(bProperties.stream()
//                .map(RelationshipProperty::getDisplayName)
//                .collect(Collectors.joining(", "))
//        ));
//        System.out.println("C: ".concat(cProperties.stream()
//                .map(RelationshipProperty::getDisplayName)
//                .collect(Collectors.joining(", "))
//        ));
//
//        System.out.println("\nDerived properties:");
//
//        System.out.println("A: " + Optional.ofNullable(DerivedRelationshipProperty.findDerivedProperty(aProperties))
//                .map(DerivedRelationshipProperty::getDisplayName)
//                .orElse("отсутствует")
//        );
//        System.out.println("B: " + Optional.ofNullable(DerivedRelationshipProperty.findDerivedProperty(bProperties))
//                .map(DerivedRelationshipProperty::getDisplayName)
//                .orElse("отсутствует")
//        );
//        System.out.println("C: " + Optional.ofNullable(DerivedRelationshipProperty.findDerivedProperty(cProperties))
//                .map(DerivedRelationshipProperty::getDisplayName)
//                .orElse("отсутствует")
//        );

        MatrixRelationship A = MatrixRelationship.generate(size, (left, right) -> right - left > 5 || left - right > 5);
        MatrixRelationship B = MatrixRelationship.generate(size, (left, right) -> (10 * left + 2 * right + 1) % 3 == 0);
        MatrixRelationship C = MatrixRelationship.generate(size, (left, right) ->
                isInRange(right, left + 3, 11 - left) ||
                        isInRange(right, 9 - left, left + 1)
        );

        MatrixRelationship one = A.inversion();
        MatrixRelationship two = one.subtract(C);
        MatrixRelationship three = two.addition();
        MatrixRelationship four = A.or(B);

        MatrixRelationship D = three.composition(four);

        D.printMatrix();
    }
}