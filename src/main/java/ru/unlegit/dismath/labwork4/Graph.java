package ru.unlegit.dismath.labwork4;

import it.unimi.dsi.fastutil.ints.IntRBTreeSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.ints.IntSortedSet;
import ru.unlegit.dismath.util.JavaUtil;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public record Graph(int dotAmount, List<Edge> edges, int[][] adjacencyMatrix) {

    public record Edge(int start, int end) {
    }

    public static Graph withEdges(int dotAmount, Edge... edges) {
        int[][] adjacencyMatrix = new int[dotAmount][dotAmount];

        for (Edge edge : edges) {
            adjacencyMatrix[edge.start - 1][edge.end - 1] = 1;
            adjacencyMatrix[edge.end - 1][edge.start - 1] = 1;
        }

        return new Graph(dotAmount, List.of(edges), adjacencyMatrix);
    }

    public static Graph fromIncidenceMatrix(int[][] incidenceMatrix) {
        int dotAmount = incidenceMatrix.length;
        Edge[] edges = new Edge[incidenceMatrix[0].length];

        for (int i = 0; i < edges.length; i++) {
            int first = -1, second = -1;

            for (int j = 0; j < dotAmount; j++) {
                if (incidenceMatrix[j][i] == 1) {
                    if (first == -1) {
                        first = j + 1;
                    } else if (second == -1) {
                        second = j + 1;
                    } else {
                        throw new IllegalArgumentException(
                                "invalid incident matrix (contains more than 2 incidents edges)"
                        );
                    }
                }
            }

            if (first == -1 || second == -1) {
                throw new IllegalArgumentException("invalid incident matrix (contains less than 2 incident edges)");
            }

            edges[i] = new Edge(Math.min(first, second), Math.max(first, second));
        }

        return withEdges(dotAmount, edges);
    }

    public int[][] buildIncidenceMatrix() {
        int[][] incidenceMatrix = new int[dotAmount][edges.size()];

        for (int i = 0; i < edges.size(); i++) {
            Edge edge = edges.get(i);

            incidenceMatrix[edge.start - 1][i] = 1;
            incidenceMatrix[edge.end - 1][i] = 1;
        }

        return incidenceMatrix;
    }

    public void getAllRoutes(int start, int end, int length) {
        int[] route = new int[length + 1];

        route[0] = start;

        getAllRoutes(buildIncidenceMatrix(), end, length, route, 1);
    }

    private void getAllRoutes(int[][] incidentMatrix, int end, int length, int[] route, int index) {
        int[] adjacent = new int[incidentMatrix[0].length];
        int adjacentLength = adjacentDots(incidentMatrix, adjacent, route[index - 1]);

        for (int x = 0; x < adjacentLength; x++) {
            route[index] = adjacent[x];

            if (index == length) {
                if (end == -1 || route[index] == end) {
                    System.out.println(Arrays.stream(route)
                            .limit(index + 1)
                            .mapToObj(String::valueOf)
                            .collect(Collectors.joining(",", "[", "]"))
                    );
                }
            } else {
                getAllRoutes(incidentMatrix, end, length, route, index + 1);
            }
        }
    }

    private int adjacentDots(int[][] incidentMatrix, int[] array, int dot) {
        int arraySize = 0;

        for (int column = 0; column < incidentMatrix[0].length; column++) {
            if (incidentMatrix[dot - 1][column] == 1) {
                for (int row = 0; row < incidentMatrix.length; row++) {
                    if (
                            incidentMatrix[row][column] == 1 && row != dot - 1 &&
                                    !JavaUtil.contains(array, arraySize, row + 1)
                    ) {
                        array[arraySize] = row + 1;
                        arraySize++;
                    }
                }
            }
        }

        return arraySize;
    }

    public int[][] getRouteAmountMatrix(int routeLength) {
        return JavaUtil.powMatrix(adjacencyMatrix, routeLength);
    }

    private IntSortedSet adjacentDots(int[][] incidentMatrix, int dot) {
        IntSortedSet result = new IntRBTreeSet();

        for (int col = 0; col < incidentMatrix[0].length; col++) {
            if (incidentMatrix[dot - 1][col] == 1) {
                for (int row = 0; row < incidentMatrix.length; row++) {
                    if (incidentMatrix[row][col] == 1 && row != dot - 1) {
                        result.add(row + 1);
                    }
                }
            }
        }

        return result;
    }

    private boolean isSubSet(IntSet subSet, IntSet set) {
        return subSet.intStream().allMatch(set::contains);
    }

    private void getAllSimpleMaxChains(int[][] incidentMatrix, int[] chain, int index, IntSet dots) {
        int[] gamma_W = adjacentDots(incidentMatrix, chain[index - 1]).intStream()
                .filter(value -> !dots.contains(value))
                .sorted()
                .toArray();

        for (int x : gamma_W) {
            chain[index] = x;

            if (isSubSet(adjacentDots(incidentMatrix, x), dots)) {
                System.out.println(Arrays.stream(chain)
                        .limit(index + 1)
                        .mapToObj(String::valueOf)
                        .collect(Collectors.joining(",", "[", "]"))
                );
            } else {
                dots.add(x);
                getAllSimpleMaxChains(incidentMatrix, chain, index + 1, dots);
                dots.remove(x);
            }
        }
    }

    public void getAllSimpleMaxChains(int start) {
        int[][] incidentMatrix = buildIncidenceMatrix();
        int[] chain = new int[incidentMatrix.length];
        IntSet dots = new IntRBTreeSet();

        chain[0] = start;
        dots.add(start);

        getAllSimpleMaxChains(incidentMatrix, chain, 1, dots);
    }
}