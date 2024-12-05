package ru.unlegit.dismath.labwork4;

import it.unimi.dsi.fastutil.ints.IntArraySet;
import it.unimi.dsi.fastutil.ints.IntSet;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

@Getter
@AllArgsConstructor
public enum DotSequenceProperty {

    PATH("маршрут") {
        @Override
        public boolean check(int[] dots, int[][] incidentMatrix) {
            boolean isConnected = false;

            for (int dot = 1; dot < dots.length; dot++) {
                for (int edge = 0; edge < incidentMatrix[0].length; edge++) {
                    if (incidentMatrix[dots[dot - 1] - 1][edge] == 1 && incidentMatrix[dots[dot] - 1][edge] == 1) {
                        isConnected = true;
                        break;
                    }
                }
                if (!isConnected) return false;

                isConnected = false;
            }
            return true;
        }
    },
    CHAIN("цепь") {
        private boolean isElemInArray(int[] array, int size, int value) {
            return Arrays.stream(array, 0, size).anyMatch(arrayValue -> value == arrayValue);
        }

        @Override
        public boolean check(int[] dots, int[][] incidentMatrix) {
            boolean isConnected = false;
            int[] edges = new int[incidentMatrix[0].length];
            int edgeAmount = 0;

            for (int dot = 1; dot < dots.length; dot++) {
                for (int edge = 0; edge < incidentMatrix[0].length; edge++) {
                    if (incidentMatrix[dots[dot - 1] - 1][edge] == 1 && incidentMatrix[dots[dot] - 1][edge] == 1) {
                        isConnected = true;

                        if (isElemInArray(edges, edgeAmount, edge)) return false;

                        edges[edgeAmount++] = edge;

                        break;
                    }
                }
                if (!isConnected) return false;

                isConnected = false;
            }
            return true;
        }
    },
    SIMPLE_CHAIN("простая цепь") {
        private boolean isSet(int[] array) {
            return Arrays.stream(array).distinct().count() == array.length;
        }

        @Override
        public boolean check(int[] dots, int[][] incidentMatrix) {
            return isSet(dots) && PATH.check(dots, incidentMatrix);
        }
    },
    CYCLE("цикл") {
        @Override
        public boolean check(int[] dots, int[][] incidentMatrix) {
            return (dots[0] == dots[dots.length - 1]) && CHAIN.check(dots, incidentMatrix);
        }
    },
    SIMPLE_CYCLE("простой цикл") {
        private boolean isSet(int[] array, int size) {
            return Arrays.stream(array).limit(size).distinct().count() == size;
        }

        @Override
        public boolean check(int[] dots, int[][] incidentMatrix) {
            return (dots[0] == dots[dots.length - 1] && isSet(dots, dots.length - 1)) &&
                    CHAIN.check(dots, incidentMatrix);
        }
    };

    public static List<DotSequenceProperty> getProperties(int[] dots, int[][] incidentMatrix) {
        return Arrays.stream(values())
                .filter(property -> property.check(dots, incidentMatrix))
                .toList();
    }

    private final String displayName;

    public abstract boolean check(int[] dots, int[][] incidentMatrix);
}