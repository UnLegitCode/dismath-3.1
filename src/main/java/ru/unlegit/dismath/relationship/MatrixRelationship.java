package ru.unlegit.dismath.relationship;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public record MatrixRelationship(int size, byte[][] matrix) implements Relationship<MatrixRelationship> {

    public static MatrixRelationship generate(int size, GeneratingProcedure generatingProcedure) {
        byte[][] matrix = new byte[size][size];

        for (int i = 0; i < size; i++) {
            byte[] row = matrix[i];

            for (int j = 0; j < size; j++) {
                if (generatingProcedure.process(i + 1, j + 1)) {
                    row[j] = 1;
                }
            }
        }

        return new MatrixRelationship(size, matrix);
    }

    @Override
    public String toString() {
        return IntStream.range(0, size)
                .boxed()
                .flatMap(left -> IntStream.range(0, size)
                        .filter(right -> matrix[left][right] == 1)
                        .mapToObj(right -> "{%d,%d}".formatted(left + 1, right + 1))
                ).collect(Collectors.joining(",", "{", "}"));
    }

    @Override
    public boolean inclusion(MatrixRelationship relationship) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (matrix[i][j] == 1 && relationship.matrix[i][j] == 0) {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public boolean equals(MatrixRelationship relationship) {
        for (int i = 0; i < size; i++) {
            if (!Arrays.equals(matrix[i], relationship.matrix[i])) return false;
        }

        return true;
    }

    @Override
    public boolean strictInclusion(MatrixRelationship relationship) {
        boolean foundDifference = false;

        for (int i = 0; i < size; i++) {
            byte[] leftRow = matrix[i];
            byte[] rightRow = relationship.matrix[i];

            for (int j = 0; j < size; j++) {
                if (leftRow[j] == 1 && rightRow[j] == 0) return false;

                if (!foundDifference && leftRow[j] != rightRow[j]) {
                    foundDifference = true;
                }
            }
        }

        return foundDifference;
    }

    @Override
    public MatrixRelationship or(MatrixRelationship relationship) {
        return generate(
                size,
                (left, right) -> (matrix[left - 1][right - 1] | relationship.matrix[left - 1][right - 1]) == 1
        );
    }

    @Override
    public MatrixRelationship and(MatrixRelationship relationship) {
        return generate(
                size,
                (left, right) -> (matrix[left - 1][right - 1] & relationship.matrix[left - 1][right - 1]) == 1
        );
    }

    @Override
    public MatrixRelationship subtract(MatrixRelationship relationship) {
        return generate(
                size,
                (left, right) -> (matrix[left - 1][right - 1] - relationship.matrix[left - 1][right - 1]) == 1
        );
    }

    @Override
    public MatrixRelationship subtractSymmetrically(MatrixRelationship relationship) {
        return generate(size, (left, right) -> {
            byte leftValue = matrix[left - 1][right - 1];
            byte rightValue = relationship.matrix[left - 1][right - 1];

            return ((leftValue - rightValue) == 1) || ((rightValue - leftValue) == 1);
        });
    }

    @Override
    public MatrixRelationship addition() {
        return generate(size, (left, right) -> matrix[left - 1][right - 1] == 0);
    }

    @Override
    public MatrixRelationship inversion() {
        return generate(size, (left, right) -> matrix[right - 1][left - 1] == 1);
    }

    @Override
    public MatrixRelationship composition(MatrixRelationship relationship) {
        return generate(size, (left, right) -> IntStream.range(0, size)
                .anyMatch(value -> (matrix[left - 1][value] & relationship.matrix[value][right - 1]) == 1)
        );
    }

    @Override
    public MatrixRelationship power(int power) {
        if (power < 0) throw new IllegalArgumentException("Power can't be negative");
        if (power == 0) return generate(size, GeneratingProcedure.IDENTITY);

        MatrixRelationship result = this;

        for (int i = 1; i < power; i++) {
            result = result.composition(this);
        }

        return result;
    }

    @Override
    public void printMatrix() {
        IntStream.range(0, size).forEach(left -> {
            System.out.print("(");

            IntStream.range(0, size).forEach(right -> {
                System.out.print(matrix[left][right]);

                if (right != size - 1) {
                    System.out.print(" ");
                }
            });

            System.out.println(")");
        });
    }

    public boolean contains(int left, int right) {
        return matrix[left][right] == 1;
    }

    public MatrixRelationship with(int left, int right, boolean state) {
        byte[][] newMatrix = matrix.clone();

        newMatrix[left][right] = (byte) (state ? 1 : 0);

        return new MatrixRelationship(size, newMatrix);
    }
}