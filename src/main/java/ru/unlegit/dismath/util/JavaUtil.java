package ru.unlegit.dismath.util;

import lombok.experimental.UtilityClass;

import java.util.Arrays;
import java.util.stream.Collectors;

@UtilityClass
public class JavaUtil {

    public int[][] multiplyMatrices(int[][] left, int[][] right) {
        int rows = left.length;
        int[][] result = new int[rows][rows];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < rows; j++) {
                result[i][j] = 0;

                for (int k = 0; k < rows; k++) {
                    result[i][j] += left[i][k] * right[k][j];
                }
            }
        }

        return result;
    }

    public int[][] powMatrix(int[][] matrix, int power) {
        int[][] result = matrix;

        for (int i = 1; i < power; i++) {
            result = multiplyMatrices(result, matrix);
        }

        return result;
    }

    public String formatMatrix(int[][] matrix) {
        return Arrays.stream(matrix).map(row -> Arrays.stream(row)
                .mapToObj(String::valueOf)
                .collect(Collectors.joining(" ", "(", ")"))
        ).collect(Collectors.joining("\n"));
    }

    public boolean contains(int[] array, int size, int value) {
        return Arrays.stream(array)
                .limit(size)
                .anyMatch(arrayValue -> arrayValue == value);
    }

    public int getHalf(int value) {
        return (value >> 1) + ((value % 2 == 1) ? 1 : 0);
    }
}