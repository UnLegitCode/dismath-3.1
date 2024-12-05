package ru.unlegit.dismath.labwork3;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import ru.unlegit.dismath.relationship.DerivedRelationshipProperty;
import ru.unlegit.dismath.relationship.MatrixRelationship;
import ru.unlegit.dismath.relationship.RelationshipProperty;

import java.util.List;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class LabWork3 {

    private static final List<Point> M1_POINTS = List.of(
            new Point(-1, 1), new Point(0, 1), new Point(1, 1),
            new Point(-1, 0), new Point(0, 0), new Point(1, 0),
            new Point(-1, -1), new Point(0, -1), new Point(1, -1)
    );
    private static final List<Point> M2_POINTS = List.of(
            new Point(-2, 0), new Point(-1, 1), new Point(-1, 0), new Point(-1, -1),
            new Point(0, 2), new Point(0, 1), new Point(0, 0), new Point(0, -1),
            new Point(0, -2), new Point(1, 1), new Point(1, 0), new Point(1, -1),
            new Point(2, 0)
    );
    private static final BiPredicate<Point, Point> GENERATING_PROCEDURE = (left, right) -> left.y() < right.y();

    private static MatrixRelationship generateRelationship(List<Point> points) {
        int size = points.size();
        byte[][] matrix = new byte[size][size];

        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                if (GENERATING_PROCEDURE.test(points.get(x), points.get(y))) {
                    matrix[x][y] = 1;
                }
            }
        }

        return new MatrixRelationship(size, matrix);
    }

    private static MatrixRelationship getDominationRelationship(MatrixRelationship relationship) {
        return relationship.subtract(relationship.power(2));
    }

    private static void topologicalSort(List<Point> points) {
        MatrixRelationship relationship = getDominationRelationship(generateRelationship(points));
        IntList skippingValue = new IntArrayList();
        int levelNumber = 1;

        while (skippingValue.size() < relationship.size()) {
            IntList level = new IntArrayList();

            for (int column = 0; column < relationship.size(); column++) {
                if (skippingValue.contains(column)) continue;

                int finalColumn = column;
                boolean emptyColumn = IntStream.range(0, relationship.size())
                        .filter(row -> !skippingValue.contains(row))
                        .allMatch(row -> relationship.matrix()[row][finalColumn] == 0);

                if (emptyColumn) {
                    level.add(column);
                }
            }

            System.out.printf(
                    "Level №%d: %s%n", levelNumber++,
                    level.intStream()
                            .mapToObj(value -> String.valueOf(value + 1))
                            .collect(Collectors.joining(" "))
            );

            skippingValue.addAll(level);
        }
    }

    private static void describe(List<Point> points, String setName) {
        MatrixRelationship relationship = generateRelationship(points);

        System.out.printf("Set %s:%n", setName);
        System.out.println("Derived property: " + DerivedRelationshipProperty.findDerivedProperty(
                RelationshipProperty.findProperties(relationship, setName)
        ).getDisplayName());
        System.out.println("Domination relationship matrix: ");
        getDominationRelationship(relationship).printMatrix();
        System.out.println("Topological sorting levels:");
        topologicalSort(points);
    }

    public static void main(String[] args) {
        describe(M1_POINTS, "M1");
        System.out.println("-".repeat(48));
        describe(M2_POINTS, "M2");
    }
}