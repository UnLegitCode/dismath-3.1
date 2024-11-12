package ru.unlegit.dismath;

import ru.unlegit.dismath.relationship.GeneratingProcedure;
import ru.unlegit.dismath.relationship.MatrixRelationship;
import ru.unlegit.dismath.relationship.RelationshipProperty;

import java.util.stream.Collectors;

public final class LabWork3 {

    public static void main(String[] args) {
        GeneratingProcedure generatingProcedure = (left, right) -> Math.abs(left - right) % 3 == 0;
        MatrixRelationship relationship = MatrixRelationship.generate(10, generatingProcedure);

        System.out.println("Матрица отношения:");
        relationship.printMatrix();

        System.out.println("\nСвойства отношения: ".concat(RelationshipProperty
                .findProperties(relationship, "R", false)
                .stream()
                .map(RelationshipProperty::getDisplayName)
                .collect(Collectors.joining(", "))
        ));

        System.out.println("Разбиение: ".concat(relationship.formatFactorSet()));
    }
}